package com.unla.epicentrogourmet.repositories;

import com.unla.epicentrogourmet.entities.Caja;
import com.unla.epicentrogourmet.entities.enums.EstadoCaja;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository("cajaRepository")
public interface ICajaRepository extends JpaRepository<Caja, Integer> {

    Optional<Caja> findFirstByCajeroIdAndEstadoOrderByFechaDescIdDesc(Integer cajeroId, EstadoCaja estado);

    Optional<Caja> findFirstByCajeroIdOrderByFechaDescIdDesc(Integer cajeroId);

    Optional<Caja> findFirstByCajeroIdAndUnidadVentaIdAndFecha(Integer cajeroId, Integer unidadVentaId, LocalDate fecha);

    List<Caja> findByCajeroIdAndEstadoAndFechaBefore(Integer cajeroId, EstadoCaja estado, LocalDate fecha);

    List<Caja> findByCajeroIdOrderByFechaDescIdDesc(Integer cajeroId);

}