package com.unla.epicentrogourmet.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Pedido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private LocalDate fecha;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="festival_id")
	private Festival festival;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="unidad_venta_id")
	private UnidadVenta unidadVenta;

	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="pedido_id")
	private Set<ItemPedido> items = new HashSet<>();

	public double calcularTotal(){
		return items.stream().mapToDouble(ItemPedido::calcularSubtotal).sum();
	}

}
