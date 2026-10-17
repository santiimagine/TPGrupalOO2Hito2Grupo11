package com.unla.epicentrogourmet.services;

import com.unla.epicentrogourmet.dtos.RegistroUnidadVentaDTO;
import com.unla.epicentrogourmet.dtos.UnidadVentaDTO;
import com.unla.epicentrogourmet.entities.UnidadVenta;

import java.util.List;

public interface IUnidadVentaService {
    UnidadVenta registrar(RegistroUnidadVentaDTO dto);

    List<UnidadVentaDTO> traerPorFestival(Integer festivalId);
}
