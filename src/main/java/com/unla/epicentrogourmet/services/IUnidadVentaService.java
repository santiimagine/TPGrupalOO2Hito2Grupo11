package com.unla.epicentrogourmet.services;

import com.unla.epicentrogourmet.dtos.RegistroUnidadVentaDTO;
import com.unla.epicentrogourmet.dtos.UnidadVentaDTO;
import com.unla.epicentrogourmet.entities.UnidadVenta;
import com.unla.epicentrogourmet.dtos.PersonalDTO;

import java.util.List;

public interface IUnidadVentaService {
    UnidadVenta registrar(RegistroUnidadVentaDTO dto);

    List<UnidadVentaDTO> traerPorFestival(Integer festivalId);

    List<PersonalDTO> traerPersonal(Integer unidadVentaId);


}
