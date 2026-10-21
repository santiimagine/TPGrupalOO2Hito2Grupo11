package com.unla.epicentrogourmet.services;


import com.unla.epicentrogourmet.entities.Caja;

import java.util.Optional;

public interface ICajaService {

	Caja traerCajaActual(Integer cajeroId);

	void cerrarCaja(Integer cajaId, Integer cajeroId);
}