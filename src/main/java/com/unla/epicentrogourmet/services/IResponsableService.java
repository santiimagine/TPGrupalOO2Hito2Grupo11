package com.unla.epicentrogourmet.services;

import java.util.List;

import com.unla.epicentrogourmet.dtos.FestivalDTO;
import com.unla.epicentrogourmet.entities.UnidadVenta;

public interface IResponsableService {

	//Festivales en los que el responsable tiene una unidad de venta (el mas reciente primero)
	List<FestivalDTO> traerFestivales(String mail);

	//La unidad del responsable en ese festival. Un responsable tiene una unidad por festival.
	//Lanza IllegalArgumentException si no tiene ninguna en ese festival.
	//Devuelve la entidad: usar siempre dentro de una transaccion de otro service.
	UnidadVenta traerUnidad(String mail, Integer festivalId);
}
