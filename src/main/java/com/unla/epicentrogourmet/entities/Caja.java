package com.unla.epicentrogourmet.entities;

import java.time.LocalDate;

import com.unla.epicentrogourmet.entities.enums.EstadoCaja;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Caja {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private LocalDate fecha;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unidad_venta_id")
	private UnidadVenta unidadVenta;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cajero_id")
	private Cajero cajero;

	@Enumerated(EnumType.STRING)
	private EstadoCaja estado;

	private double montoTotal;

	public boolean estaAbierta() {
		return this.estado == EstadoCaja.ABIERTA;
	}

	public boolean esDeHoy(){
		return LocalDate.now().equals(this.fecha);
	}

	public void registrarVenta(double monto){
		if (!estaAbierta())
			throw new IllegalStateException("La caja está cerrada.");
		setMontoTotal(this.montoTotal + monto);
	}

}
