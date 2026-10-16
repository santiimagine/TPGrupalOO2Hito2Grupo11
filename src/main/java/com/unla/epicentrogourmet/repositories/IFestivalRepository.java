package com.unla.epicentrogourmet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unla.epicentrogourmet.entities.Festival;

import java.time.LocalDate;
import java.util.List;

@Repository("festivalRepository")
public interface IFestivalRepository extends JpaRepository<Festival, Integer> {

    List<Festival> findByFechaFinGreaterThanEqual(LocalDate fecha);

}
