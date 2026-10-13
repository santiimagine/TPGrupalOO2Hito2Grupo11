package com.unla.epicentrogourmet.dtos;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class FestivalDTO {
	private Integer id;

	private String nombre;

	private String temporada;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechaInicio;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechaFin;

	public FestivalDTO(Integer id, String nombre, String temporada, LocalDate fechaInicio, LocalDate fechaFin) {
		this.setId(id);
		this.nombre = nombre;
		this.temporada = temporada;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
	}
}
//DTO para mostrar en la vista publica (home)