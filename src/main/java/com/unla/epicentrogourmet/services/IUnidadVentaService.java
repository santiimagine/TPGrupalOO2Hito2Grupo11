package com.unla.epicentrogourmet.services;

import com.unla.epicentrogourmet.dtos.RegistroUnidadVentaDTO;
import com.unla.epicentrogourmet.entities.UnidadVenta;

public interface IUnidadVentaService {
    UnidadVenta registrar(RegistroUnidadVentaDTO dto);
}
