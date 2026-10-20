package com.unla.epicentrogourmet.services;

import com.unla.epicentrogourmet.dtos.PlatoFormDTO;
import com.unla.epicentrogourmet.dtos.PlatoListadoDTO;

public interface IPlatoService {

	//Agrega un plato al menu de la unidad que el responsable tiene en ese festival
	PlatoListadoDTO alta(String mailResponsable, Integer festivalId, PlatoFormDTO dto);
}
