package com.unla.epicentrogourmet.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
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
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="mail", unique=true, nullable=false, length=100)
	private String mail;

	@Column(name="contrasenia", nullable=false, length=60)
	private String contrasenia;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@OneToMany(fetch=FetchType.LAZY, mappedBy="usuario")
	private Set<UserRol> roles = new HashSet<>();

	public Usuario(String mail, String contrasenia) {
		this.mail = mail;
		this.contrasenia = contrasenia;
	}

	public Usuario(String mail, String contrasenia, Set<UserRol> roles) {
		this.mail = mail;
		this.contrasenia = contrasenia;
		this.roles = roles;
	}
}
