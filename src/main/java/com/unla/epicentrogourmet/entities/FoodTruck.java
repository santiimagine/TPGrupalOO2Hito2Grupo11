package com.unla.epicentrogourmet.entities;

import com.unla.epicentrogourmet.helpers.Constantes;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class FoodTruck extends UnidadVenta {

	private String patente;

	private boolean requiereElectricidad;

	@Override
	public double calcularCanon() {

		double canon = getSuperficie() * Constantes.COSTO_M2;
		if(requiereElectricidad)
			canon += Constantes.PLUS_ELECTRICIDAD;

		return canon;

	}
}
