package com.unla.epicentrogourmet.services;

import com.unla.epicentrogourmet.dtos.EmpleadoGestionDTO;

import java.util.List;

public interface IGestionEmpleadoService {

    List<EmpleadoGestionDTO> traerActivos(String mailResponsable);

}
