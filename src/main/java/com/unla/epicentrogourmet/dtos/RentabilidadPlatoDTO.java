package com.unla.epicentrogourmet.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//DTO de reporte: ganancia de un plato en las ventas de una unidad (no persiste)
@Getter @Setter @NoArgsConstructor
public class RentabilidadPlatoDTO {
	private String nombrePlato;
	private long unidadesVendidas;
	private double ingresos;
	private double costo;
	private double ganancia;
}
