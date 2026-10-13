package com.unla.epicentrogourmet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unla.epicentrogourmet.entities.Usuario;

@Repository("usuarioRepository")
public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {

	@Query("SELECT u FROM Usuario u JOIN FETCH u.roles WHERE u.mail = (:mail)")
	public abstract Usuario findByMailAndFetchRolesEagerly(@Param("mail") String mail);
}
