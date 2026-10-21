package com.unla.epicentrogourmet.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//DTO de reporte del plato estrella (no persiste). cantidadPedida = en cuantos pedidos aparecio el plato.
@Getter @Setter @NoArgsConstructor
public class PlatoDTO {
	private String nombrePlato;
	private double precioVenta;
	private int cantidadPedida;
}
