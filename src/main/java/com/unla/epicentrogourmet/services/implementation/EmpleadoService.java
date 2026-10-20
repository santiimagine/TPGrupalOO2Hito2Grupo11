package com.unla.epicentrogourmet.services.implementation;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import com.unla.epicentrogourmet.dtos.*;
import com.unla.epicentrogourmet.entities.*;
import com.unla.epicentrogourmet.helpers.Constantes;
import com.unla.epicentrogourmet.repositories.IEmpleadoRepository;
import com.unla.epicentrogourmet.services.IEmpleadoService;

import jakarta.transaction.Transactional;

@Service
public class EmpleadoService
        implements IEmpleadoService {

    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final IEmpleadoRepository empleadoRepository;

    public EmpleadoService(IEmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }


    @Override
    @Transactional
    public EmpleadoDTO traerPorMail(String mail) {
        Empleado e = empleadoRepository.traerPorMail(mail)
                .orElseThrow(() -> new RuntimeException("No hay un empleado asociado al usuario " + mail));
        return toEmpleadoDTO(e);
    }

    @Override
    @Transactional
    public List<AsignacionDTO> traerAsignaciones(Integer empleadoId) {
        return empleadoRepository.traerUnidades(empleadoId).stream()
                .map(this::toAsignacionDTO)
                .toList();
    }

    @Override
    @Transactional
    public AsignacionDTO traerAsignacionActual(Integer empleadoId) {
        return traerAsignaciones(empleadoId).stream()
                .filter(AsignacionDTO::isActivo)
                .findFirst()
                .orElse(null);
    }

    // ===================================================================
    // Descargar recibo de sueldo (PDF)
    // ===================================================================

    @Override
    @Transactional
    public byte[] generarPdf(Integer empleadoId, Integer festivalId) {
        Empleado empleado = empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));

        // Si el festival no está entre sus asignaciones, nunca trabajó en él
        AsignacionDTO asignacion = traerAsignaciones(empleadoId).stream()
                .filter(a -> Objects.equals(a.getFestivalId(), festivalId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No trabajaste en ese festival"));

        // La vista oculta el botón, pero hay que validarlo acá: puede alguien escribir la url a mano.
        if (asignacion.isActivo()) {
            throw new IllegalStateException("El recibo se habilita cuando finaliza el festival");
        }

        double total = calcularSueldo(empleadoId, festivalId);

        EmpleadoDTO dto = toEmpleadoDTO(empleado);
        long dias = ChronoUnit.DAYS.between(asignacion.getFechaInicio(), asignacion.getFechaFin()) + 1;

        return armarPdf(dto, asignacion, dias, total);
    }

    @Override
    @Transactional
    public double calcularSueldo(Integer empleadoId, Integer festivalId) {
        Empleado empleado = empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));
        AsignacionDTO asignacion = traerAsignaciones(empleadoId).stream()
                .filter(a -> Objects.equals(a.getFestivalId(), festivalId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("El empleado no trabajó en ese festival"));
        long dias = ChronoUnit.DAYS.between(asignacion.getFechaInicio(), asignacion.getFechaFin()) + 1;
        return calcularSueldoFestival(toEmpleadoDTO(empleado), asignacion.getFechaFin(), dias);
    }


    // Liquidación: factor por duración del festival x (sueldo base + plus o antiguedad)
    private double calcularSueldoFestival(EmpleadoDTO empleado, LocalDate finFestival, long dias) {
        double monto;
        if ("Cajero".equals(empleado.getTipoEmpleado())) {
            int anios = Math.max(Period.between(empleado.getFechaIngreso(), finFestival).getYears(), 0);
            monto = Constantes.SUELDO_BASE + anios * Constantes.ANTIGUEDAD_POR_ANIO;
        } else {
            Number plus = empleado.getPlusCategoria();
            monto = Constantes.SUELDO_BASE + (plus != null ? plus.doubleValue() : 0);
        }
        return factorPorDuracion(dias) * monto;
    }

    // Hasta 15 días: 50% | de 16 a 30: 100% | más de 30: (int(dias/15) + 1) * 50%
    private double factorPorDuracion(long dias) {
        if (dias <= 15) {
            return 0.5;
        }
        if (dias <= 30) {
            return 1.0;
        }
        return ((dias / 15) + 1) * 0.5;
    }

    // Plantilla de la tabla en PDF
    private byte[] armarPdf(EmpleadoDTO empleado, AsignacionDTO asignacion, long dias, double total) {
        NumberFormat moneda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-AR"));
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        Document documento = new Document(PageSize.A4, 50, 50, 50, 50);

        try {
            PdfWriter.getInstance(documento, salida);
            documento.open();

            documento.add(new Paragraph("Epicentro Gourmet", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)));
            Paragraph subtitulo = new Paragraph("Recibo de sueldo", FontFactory.getFont(FontFactory.HELVETICA, 12));
            subtitulo.setSpacingAfter(20);
            documento.add(subtitulo);

            PdfPTable tabla = new PdfPTable(2);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[] {35, 65});

            agregarFila(tabla, "Empleado", empleado.getApellido() + ", " + empleado.getNombre());
            agregarFila(tabla, "DNI", String.valueOf(empleado.getDni()));
            agregarFila(tabla, "Puesto", empleado.getTipoEmpleado());
            agregarFila(tabla, "Fecha de ingreso", empleado.getFechaIngreso().format(FECHA));
            agregarFila(tabla, "Festival", asignacion.getFestival());
            agregarFila(tabla, "Unidad de venta", asignacion.getUnidadVenta());
            agregarFila(tabla, "Período", asignacion.getFechaInicio().format(FECHA) + " al " + asignacion.getFechaFin().format(FECHA));
            agregarFila(tabla, "Duración", dias + " días");
            agregarFila(tabla, "Total a cobrar", moneda.format(total));

            documento.add(tabla);
        } catch (DocumentException e) {
            throw new IllegalStateException("No se pudo generar el recibo en PDF", e);
        } finally {
            documento.close();
        }
        return salida.toByteArray();
    }

    private void agregarFila(PdfPTable tabla, String etiqueta, String valor) {
        tabla.addCell(celda(etiqueta, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11)));
        tabla.addCell(celda(valor, FontFactory.getFont(FontFactory.HELVETICA, 11)));
    }

    private PdfPCell celda(String texto, Font fuente) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setPadding(8);
        return celda;
    }



    private AsignacionDTO toAsignacionDTO(UnidadVenta u) {
        Festival f = u.getFestival();
        return new AsignacionDTO(
                f.getNombre(),
                u.getNombreComercial(),
                f.getFechaInicio(),
                f.getFechaFin(),
                f.estaActivo(),
                f.getId());
    }

    private EmpleadoDTO toEmpleadoDTO(Empleado e) {
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setId(e.getId());
        dto.setNombre(e.getNombre());
        dto.setApellido(e.getApellido());
        dto.setDni(e.getDni());
        dto.setFechaNacimiento(e.getFechaNacimiento());
        dto.setFechaIngreso(e.getFechaIngreso());
        if (e instanceof Cocinero c) {
            dto.setTipoEmpleado("Cocinero");
            dto.setEspecialidad(c.getEspecialidad() == null ? null : c.getEspecialidad());
            dto.setPlusCategoria(c.getCategoria().getPlus());
        } else if (e instanceof Cajero c) {
            dto.setTipoEmpleado("Cajero");
            dto.setTurno(c.getTurno() == null ? null : switch (c.getTurno()) {
                case MANIANA -> "Mañana";
                case NOCHE -> "Noche";
            });
        }
        return dto;
    }
}