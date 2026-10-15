package com.unla.epicentrogourmet.entities;

import com.unla.epicentrogourmet.entities.enums.Turno;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Cajero extends Empleado {

	@Enumerated(EnumType.STRING)
	private Turno turno;

	@Override
	public double calcularSueldo(Festival festival) {
		return 0; //Se resuelve en CU 16
	}
}
