package com.unla.epicentrogourmet.services;

import java.time.LocalDate;

import com.unla.epicentrogourmet.dtos.RentabilidadDTO;

public interface IRentabilidadService {

	//Ganancia de la unidad del responsable en el festival:
	//(pedidos totales - costo de los platos) - sueldos del personal - canon
	RentabilidadDTO rentabilidadNeta(String mailResponsable, Integer festivalId);

	//Lo mismo pero solo con los pedidos entre las dos fechas (inclusive). Los sueldos y el canon se restan completos.
	//Lanza IllegalArgumentException si las fechas son invalidas o quedan fuera del festival.
	RentabilidadDTO rentabilidadNetaEntreFechas(String mailResponsable, Integer festivalId, LocalDate desde, LocalDate hasta);
}
