package com.unla.epicentrogourmet.repositories;

import com.unla.epicentrogourmet.entities.Caja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("cajaRepository")
public interface ICajaRepository extends JpaRepository<Caja, Integer> {
}
