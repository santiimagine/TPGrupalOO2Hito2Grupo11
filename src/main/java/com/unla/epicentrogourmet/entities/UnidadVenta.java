package com.unla.epicentrogourmet.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UnidadVenta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nombreComercial;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="festival_id")
	private Festival festival;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="responsable_id")
	private Usuario responsable;

	private double superficie;

	private String codigo;

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="unidad_venta_personal",
			joinColumns=@JoinColumn(name="unidad_venta_id"),
			inverseJoinColumns=@JoinColumn(name="empleado_id"))
	private Set<Empleado> personal = new HashSet<>();

	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="unidad_venta_id")
	private Set<Plato> platos = new HashSet<>();

	@OneToMany(fetch=FetchType.LAZY, mappedBy="unidadVenta")
	private Set<Pedido> pedidos = new HashSet<>();

	public abstract double calcularCanon();

}
