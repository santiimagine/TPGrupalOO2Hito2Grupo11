package com.unla.epicentrogourmet.services;

import com.unla.epicentrogourmet.dtos.ReporteVentaDTO;
import com.unla.epicentrogourmet.dtos.ReporteMayoresCanonDTO;
import com.unla.epicentrogourmet.dtos.ResumenFestivalDTO;

import java.util.List;

public interface IReporteService {
    List<ReporteVentaDTO> rankingPorRecaudacion(Integer festivalId);

    ReporteVentaDTO recaudacionTotal(Integer festivalId);

    List<ReporteMayoresCanonDTO> mayoresCanon(Integer festivalId, int cantidad);

    double calcularCanon(Integer unidadVentaId);

    ResumenFestivalDTO resumenFestival(Integer festivalId);
}
