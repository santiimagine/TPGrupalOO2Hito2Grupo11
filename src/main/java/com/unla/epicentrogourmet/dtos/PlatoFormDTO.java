package com.unla.epicentrogourmet.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//DTO del formulario de alta de plato. Las validaciones de carga viajan en las anotaciones.
@Getter @Setter @NoArgsConstructor
public class PlatoFormDTO {

	@NotBlank(message = "El nombre es obligatorio")
	@Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
	private String nombre;

	@NotNull(message = "El precio de venta es obligatorio")
	@Positive(message = "El precio de venta debe ser mayor a 0")
	private Double precioVenta;

	@NotNull(message = "El costo de producción es obligatorio")
	@PositiveOrZero(message = "El costo de producción no puede ser negativo")
	private Double costoProduccion;
}
