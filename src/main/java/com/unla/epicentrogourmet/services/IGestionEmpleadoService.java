package com.unla.epicentrogourmet.services;

import com.unla.epicentrogourmet.dtos.EmpleadoFormDTO;
import com.unla.epicentrogourmet.dtos.EmpleadoGestionDTO;
import com.unla.epicentrogourmet.dtos.FiltroEmpleadoDTO;

import java.util.List;
import java.util.Map;

public interface IGestionEmpleadoService {

    List<EmpleadoGestionDTO> traerActivos(String mailResponsable, FiltroEmpleadoDTO filtro);

    Map<Integer, String> traerUnidadesResponsable(String mailResponsable);

    Map<Integer, String> traerUnidadesVigentes(String mailResponsable);
    String darDeAlta (EmpleadoFormDTO empleadoFormDTO, String mailResponsable);

    EmpleadoFormDTO traerEmpleadoAModificar(Integer id, String mailResponsable);

    void modificar(Integer id, EmpleadoFormDTO dto, String mailResponsable);

    void darDeBaja(Integer id, String mailResponsable);

    List<EmpleadoGestionDTO> traerInactivos(String mailResponsable);

    void reactivar(Integer id, String mailResponsable);

}
