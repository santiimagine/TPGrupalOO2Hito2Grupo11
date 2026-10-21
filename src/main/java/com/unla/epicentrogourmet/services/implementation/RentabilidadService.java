package com.unla.epicentrogourmet.services.implementation;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unla.epicentrogourmet.dtos.RentabilidadDTO;
import com.unla.epicentrogourmet.entities.Festival;
import com.unla.epicentrogourmet.entities.UnidadVenta;
import com.unla.epicentrogourmet.repositories.IRentabilidadRepository;
import com.unla.epicentrogourmet.services.IEmpleadoService;
import com.unla.epicentrogourmet.services.IRentabilidadService;
import com.unla.epicentrogourmet.services.IReporteService;
import com.unla.epicentrogourmet.services.IResponsableService;

@Service("rentabilidadService")
public class RentabilidadService implements IRentabilidadService {

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
