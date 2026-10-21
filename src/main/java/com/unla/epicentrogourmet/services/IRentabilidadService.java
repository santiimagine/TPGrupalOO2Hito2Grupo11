package com.unla.epicentrogourmet.services;

import java.time.LocalDate;
import java.util.List;

import com.unla.epicentrogourmet.dtos.RentabilidadDTO;
import com.unla.epicentrogourmet.dtos.RentabilidadPlatoDTO;
import com.unla.epicentrogourmet.dtos.VentaDiariaDTO;

public interface IRentabilidadService {

	//Ganancia de la unidad del responsable en el festival:
	//(pedidos totales - costo de los platos) - sueldos del personal - canon
	RentabilidadDTO rentabilidadNeta(String mailResponsable, Integer festivalId);

	//Lo mismo pero solo con los pedidos entre las dos fechas (inclusive). Los sueldos y el canon se restan completos.
	//Lanza IllegalArgumentException si las fechas son invalidas o quedan fuera del festival.
	RentabilidadDTO rentabilidadNetaEntreFechas(String mailResponsable, Integer festivalId, LocalDate desde, LocalDate hasta);

	//Ganancia de cada plato vendido por la unidad (ingresos - costo de produccion), de mayor a menor ganancia.
	//Con las dos fechas en null se usan todos los pedidos del festival; si se indica una sola o el rango es invalido
	//lanza IllegalArgumentException.
	List<RentabilidadPlatoDTO> rentabilidadPorPlato(String mailResponsable, Integer festivalId, LocalDate desde, LocalDate hasta);

	//Ventas de la unidad dia por dia (cantidad de pedidos e ingresos), de la fecha mas vieja a la mas nueva.
	//Solo aparecen los dias con pedidos. Con las dos fechas en null se usa todo el festival; si se indica una sola
	//o el rango es invalido lanza IllegalArgumentException.
	List<VentaDiariaDTO> ventasPorDia(String mailResponsable, Integer festivalId, LocalDate desde, LocalDate hasta);
}
