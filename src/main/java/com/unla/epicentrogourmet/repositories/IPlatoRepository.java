package com.unla.epicentrogourmet.repositories;

import com.unla.epicentrogourmet.entities.Plato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("platoRepository")
public interface IPlatoRepository extends JpaRepository<Plato, Integer> {
}
