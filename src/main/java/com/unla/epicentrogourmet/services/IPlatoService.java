package com.unla.epicentrogourmet.services;

import java.util.List;

import com.unla.epicentrogourmet.dtos.PlatoFormDTO;
import com.unla.epicentrogourmet.dtos.PlatoListadoDTO;

public interface IPlatoService {

	//Agrega un plato al menu de la unidad que el responsable tiene en ese festival
	PlatoListadoDTO alta(String mailResponsable, Integer festivalId, PlatoFormDTO dto);

	//Platos activos del menu de la unidad, filtrados por uno o varios atributos (todos opcionales)
	List<PlatoListadoDTO> listar(String mailResponsable, Integer festivalId, String nombre, Double precioMin, Double precioMax);
}
