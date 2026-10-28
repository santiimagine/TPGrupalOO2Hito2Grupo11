package com.unla.epicentrogourmet.services;


import com.unla.epicentrogourmet.entities.Caja;
import com.unla.epicentrogourmet.entities.Cajero;
import com.unla.epicentrogourmet.entities.UnidadVenta;

import java.util.Optional;

public interface ICajaService {

	Caja traerCajaActual(Integer cajeroId);

	void cerrarCaja(Integer cajaId, Integer cajeroId);

	Caja traerOAbrirCajaDelDia (Cajero cajero, UnidadVenta unidadVenta);
}