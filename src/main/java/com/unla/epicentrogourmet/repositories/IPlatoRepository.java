package com.unla.epicentrogourmet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unla.epicentrogourmet.entities.Plato;

@Repository("platoRepository")
public interface IPlatoRepository extends JpaRepository<Plato, Integer> {

    //Cantidad de platos de la unidad con ese nombre (sin distinguir mayusculas), para no repetir nombres
    @Query("select count(p) from UnidadVenta u join u.platos p "
            + "where u.id = :unidadId and lower(p.nombre) = lower(:nombre)")
    long contarPorNombreEnUnidad(@Param("unidadId") Integer unidadId, @Param("nombre") String nombre);
}
