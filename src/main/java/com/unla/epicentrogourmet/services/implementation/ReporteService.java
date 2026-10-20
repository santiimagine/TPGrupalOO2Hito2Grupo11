package com.unla.epicentrogourmet.services.implementation;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unla.epicentrogourmet.dtos.ReporteVentaDTO;
import com.unla.epicentrogourmet.entities.FoodTruck;
import com.unla.epicentrogourmet.entities.UnidadVenta;
import com.unla.epicentrogourmet.repositories.IPedidoRepository;
import com.unla.epicentrogourmet.repositories.IUnidadVentaRepository;
import com.unla.epicentrogourmet.services.IReporteService;
import com.unla.epicentrogourmet.dtos.ReporteMayoresCanonDTO;

@Service("reporteService")
public class ReporteService implements IReporteService {

    private final IUnidadVentaRepository unidadVentaRepository;

    private final IPedidoRepository pedidoRepository;

    public ReporteService(IUnidadVentaRepository unidadVentaRepository, IPedidoRepository pedidoRepository) {
        this.unidadVentaRepository = unidadVentaRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteVentaDTO> rankingPorRecaudacion(Integer festivalId) {
        Map<Integer, Double> recaudacionPorUnidad = new HashMap<>();
        for (Object[] fila : pedidoRepository.recaudacionPorUnidad(festivalId)) {
            recaudacionPorUnidad.put(((Number) fila[0]).intValue(), ((Number) fila[1]).doubleValue());
        }

        return unidadVentaRepository.findByFestivalId(festivalId).stream()
                .map(unidad -> toReporteVentaDTO(unidad, recaudacionPorUnidad.getOrDefault(unidad.getId(), 0.0)))
                .sorted(Comparator.comparingDouble(ReporteVentaDTO::getRecaudacionTotal).reversed())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReporteVentaDTO recaudacionTotal(Integer festivalId) {
        double total = pedidoRepository.recaudacionPorUnidad(festivalId).stream()
                .mapToDouble(fila -> ((Number) fila[1]).doubleValue())
                .sum();
        ReporteVentaDTO dto = new ReporteVentaDTO();
        dto.setRecaudacionTotal(total);
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteMayoresCanonDTO> mayoresCanon(Integer festivalId, int cantidad) {
        return unidadVentaRepository.findByFestivalId(festivalId).stream()
                .map(this::toReporteMayoresCanonDTO)
                .sorted(Comparator.comparingDouble(ReporteMayoresCanonDTO::getCanon).reversed())
                .limit(cantidad)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public double calcularCanon(Integer unidadVentaId) {
        return unidadVentaRepository.findById(unidadVentaId)
                .orElseThrow(() -> new IllegalArgumentException("La unidad de venta no existe"))
                .calcularCanon();
    }

    private String tipoDeUnidad(UnidadVenta u) {
        return u instanceof FoodTruck ? "Food truck" : "Puesto desarmable";
    }

    private ReporteMayoresCanonDTO toReporteMayoresCanonDTO(UnidadVenta u) {
        ReporteMayoresCanonDTO dto = new ReporteMayoresCanonDTO();
        dto.setNombreComercial(u.getNombreComercial());
        dto.setCodigo(u.getCodigo());
        dto.setTipoUnidad(tipoDeUnidad(u));
        dto.setCanon(u.calcularCanon());
        return dto;
    }

    private ReporteVentaDTO toReporteVentaDTO(UnidadVenta u, double recaudacion) {
        ReporteVentaDTO dto = new ReporteVentaDTO();
        dto.setNombreComercial(u.getNombreComercial());
        dto.setCodigo(u.getCodigo());
        dto.setTipoUnidad(tipoDeUnidad(u));
        dto.setRecaudacionTotal(recaudacion);
        return dto;
    }
}