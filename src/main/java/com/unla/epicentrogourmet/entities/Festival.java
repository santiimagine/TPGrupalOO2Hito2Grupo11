package com.unla.epicentrogourmet.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Festival {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nombre;

	private String temporada;

	private LocalDate fechaInicio;

	private LocalDate fechaFin;

	@OneToMany(fetch=FetchType.LAZY, mappedBy="festival")
	private Set<UnidadVenta> unidades = new HashSet<>();
}
