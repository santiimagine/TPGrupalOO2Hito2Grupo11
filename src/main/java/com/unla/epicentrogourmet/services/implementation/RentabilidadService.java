package com.unla.epicentrogourmet.services.implementation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unla.epicentrogourmet.dtos.RentabilidadDTO;
import com.unla.epicentrogourmet.dtos.RentabilidadPlatoDTO;
import com.unla.epicentrogourmet.entities.Festival;
import com.unla.epicentrogourmet.entities.UnidadVenta;
import com.unla.epicentrogourmet.repositories.IRentabilidadRepository;
import com.unla.epicentrogourmet.services.IEmpleadoService;
import com.unla.epicentrogourmet.services.IRentabilidadService;
import com.unla.epicentrogourmet.services.IReporteService;
import com.unla.epicentrogourmet.services.IResponsableService;

@Service("rentabilidadService")
public class RentabilidadService implements IRentabilidadService {

    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final IResponsableService responsableService;

    private final IRentabilidadRepository rentabilidadRepository;

    private final IEmpleadoService empleadoService;

    private final IReporteService reporteService;

    public RentabilidadService(IResponsableService responsableService, IRentabilidadRepository rentabilidadRepository,
                               IEmpleadoService empleadoService, IReporteService reporteService) {
        this.responsableService = responsableService;
        this.rentabilidadRepository = rentabilidadRepository;
        this.empleadoService = empleadoService;
        this.reporteService = reporteService;
    }

    @Override
    @Transactional(readOnly = true)
    public RentabilidadDTO rentabilidadNeta(String mailResponsable, Integer festivalId) {
        UnidadVenta unidad = responsableService.traerUnidad(mailResponsable, festivalId);
        Festival festival = unidad.getFestival();
        return calcular(unidad, festival.getFechaInicio(), festival.getFechaFin());
    }

    @Override
    @Transactional(readOnly = true)
    public RentabilidadDTO rentabilidadNetaEntreFechas(String mailResponsable, Integer festivalId,
                                                       LocalDate desde, LocalDate hasta) {
        UnidadVenta unidad = responsableService.traerUnidad(mailResponsable, festivalId);
        Festival festival = unidad.getFestival();

        validarRango(festival, desde, hasta);
        return calcular(unidad, desde, hasta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentabilidadPlatoDTO> rentabilidadPorPlato(String mailResponsable, Integer festivalId,
                                                           LocalDate desde, LocalDate hasta) {
        UnidadVenta unidad = responsableService.traerUnidad(mailResponsable, festivalId);
        Festival festival = unidad.getFestival();

        // Sin fechas se usa todo el festival. Con fechas, se validan igual que en la rentabilidad entre fechas.
        if (desde == null && hasta == null) {
            desde = festival.getFechaInicio();
            hasta = festival.getFechaFin();
        } else {
            validarRango(festival, desde, hasta);
        }

        return rentabilidadRepository.totalesPorPlato(unidad.getId(), festival.getId(), desde, hasta).stream()
                .map(this::toPlatoDTO)
                .sorted(Comparator.comparingDouble(RentabilidadPlatoDTO::getGanancia).reversed()
                        .thenComparing(RentabilidadPlatoDTO::getNombrePlato))
                .toList();
    }

    // Validaciones de carga del rango de fechas
    private void validarRango(Festival festival, LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) {
            throw new IllegalArgumentException("Indicá las dos fechas");
        }
        if (desde.isAfter(hasta)) {
            throw new IllegalArgumentException("La fecha desde no puede ser posterior a la fecha hasta");
        }
        if (desde.isBefore(festival.getFechaInicio()) || hasta.isAfter(festival.getFechaFin())) {
            throw new IllegalArgumentException("Las fechas tienen que estar dentro del festival ("
                    + FECHA.format(festival.getFechaInicio()) + " al " + FECHA.format(festival.getFechaFin()) + ")");
        }
    }

    // fila = [id del plato, nombre, unidades vendidas, ingresos, costo de produccion]
    private RentabilidadPlatoDTO toPlatoDTO(Object[] fila) {
        RentabilidadPlatoDTO dto = new RentabilidadPlatoDTO();
        dto.setNombrePlato((String) fila[1]);
        dto.setUnidadesVendidas(((Number) fila[2]).longValue());
        dto.setIngresos(((Number) fila[3]).doubleValue());
        dto.setCosto(((Number) fila[4]).doubleValue());
        dto.setGanancia(dto.getIngresos() - dto.getCosto());
        return dto;
    }

    //Rentabilidad con los pedidos entre las dos fechas. Los sueldos y el canon se restan completos.
    private RentabilidadDTO calcular(UnidadVenta unidad, LocalDate desde, LocalDate hasta) {
        Festival festival = unidad.getFestival();

        Object[] totales = rentabilidadRepository.totales(unidad.getId(), festival.getId(), desde, hasta).get(0);
        double ingresos = ((Number) totales[0]).doubleValue();
        double costoPlatos = ((Number) totales[1]).doubleValue();
        long cantidadPedidos = ((Number) totales[2]).longValue();

        // La liquidacion de cada empleado la calcula EmpleadoService, asi hay una sola logica
        double sueldos = unidad.getPersonal().stream()
                .mapToDouble(e -> empleadoService.calcularSueldo(e.getId(), festival.getId()))
                .sum();
        double canon = reporteService.calcularCanon(unidad.getId());

        RentabilidadDTO dto = new RentabilidadDTO();
        dto.setUnidadVenta(unidad.getNombreComercial());
        dto.setFestival(festival.getNombre());
        dto.setDesde(desde);
        dto.setHasta(hasta);
        dto.setCantidadPedidos(cantidadPedidos);
        dto.setCantidadPersonal(unidad.getPersonal().size());
        dto.setIngresos(ingresos);
        dto.setCostoPlatos(costoPlatos);
        dto.setSueldos(sueldos);
        dto.setCanon(canon);
        dto.setRentabilidadNeta(ingresos - costoPlatos - sueldos - canon);
        return dto;
    }
}
