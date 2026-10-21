package com.unla.epicentrogourmet.dtos;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//DTO de reporte de rentabilidad de una unidad en un festival (no persiste), con el desglose del calculo
@Getter @Setter @NoArgsConstructor
public class RentabilidadDTO {
	private String unidadVenta;
	private String festival;
	private LocalDate desde;
	private LocalDate hasta;
	private long cantidadPedidos;
	private int cantidadPersonal;
	private double ingresos;
	private double costoPlatos;
	private double sueldos;
	private double canon;
	private double rentabilidadNeta;
}
