package com.unla.epicentrogourmet.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.unla.epicentrogourmet.entities.enums.Rol;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name="user_rol", uniqueConstraints=@UniqueConstraint(columnNames= {"rol", "usuario_id"}))
public class UserRol {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="usuario_id", nullable=false)
	private Usuario usuario;

	@Enumerated(EnumType.STRING)
	@Column(name="rol", nullable=false, length=100)
	private Rol rol;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	public UserRol(Integer id, Usuario usuario, Rol rol) {
		this.id = id;
		this.usuario = usuario;
		this.rol = rol;
	}
}
