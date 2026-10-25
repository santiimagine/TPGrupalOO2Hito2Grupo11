package com.unla.epicentrogourmet.repositories;

import com.unla.epicentrogourmet.entities.Caja;
import com.unla.epicentrogourmet.entities.enums.EstadoCaja;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("cajaRepository")
public interface ICajaRepository extends JpaRepository<Caja, Integer> {

    Optional<Caja> findFirstByCajeroIdAndEstado(Integer cajeroId, EstadoCaja estado);

    Optional<Caja> findFirstByCajeroIdOrderByFechaDescIdDesc(Integer cajeroId);
}