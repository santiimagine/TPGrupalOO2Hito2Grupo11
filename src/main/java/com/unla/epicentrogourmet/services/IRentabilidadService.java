package com.unla.epicentrogourmet.services;

import com.unla.epicentrogourmet.dtos.RentabilidadDTO;

public interface IRentabilidadService {

	//Ganancia de la unidad del responsable en el festival:
	//(pedidos totales - costo de los platos) - sueldos del personal - canon
	RentabilidadDTO rentabilidadNeta(String mailResponsable, Integer festivalId);
}
