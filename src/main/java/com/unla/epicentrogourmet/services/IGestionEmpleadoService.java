package com.unla.epicentrogourmet.services;

import com.unla.epicentrogourmet.dtos.EmpleadoFormDTO;
import com.unla.epicentrogourmet.dtos.EmpleadoGestionDTO;

import java.util.List;
import java.util.Map;

public interface IGestionEmpleadoService {

    List<EmpleadoGestionDTO> traerActivos(String mailResponsable);

    Map<Integer, String> traerUnidadesVigentes(String mailResponsable);
    String darDeAlta (EmpleadoFormDTO empleadoFormDTO, String mailResponsable);

}
