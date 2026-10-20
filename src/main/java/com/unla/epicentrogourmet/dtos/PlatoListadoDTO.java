package com.unla.epicentrogourmet.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//DTO para mostrar un plato en las vistas del responsable
@Getter @Setter @NoArgsConstructor
public class PlatoListadoDTO {
	private Integer id;
	private String nombre;
	private double precioVenta;
	private double costoProduccion;
}
