package com.unla.epicentrogourmet.dtos;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//DTO de reporte: ventas de una unidad en un dia (no persiste)
@Getter @Setter @NoArgsConstructor
public class VentaDiariaDTO {
	private LocalDate fecha;
	private long cantidadPedidos;
	private double ingresos;
}
