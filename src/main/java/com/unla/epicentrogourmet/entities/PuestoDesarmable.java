package com.unla.epicentrogourmet.entities;

import com.unla.epicentrogourmet.helpers.Constantes;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class PuestoDesarmable extends UnidadVenta {

	private int cantidadCarpas;

	private int tiempoMontaje;

	@Override
	public double calcularCanon() {
		return getSuperficie() * Constantes.COSTO_M2 - tiempoMontaje * Constantes.COSTO_MONTAJE;
	}
}
