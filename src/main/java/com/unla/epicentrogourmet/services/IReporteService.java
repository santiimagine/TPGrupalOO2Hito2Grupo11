package com.unla.epicentrogourmet.services;

import com.unla.epicentrogourmet.dtos.ReporteVentaDTO;

import java.util.List;

public interface IReporteService {
    List<ReporteVentaDTO> rankingPorRecaudacion(Integer festivalId);

    ReporteVentaDTO recaudacionTotal(Integer festivalId);
}
