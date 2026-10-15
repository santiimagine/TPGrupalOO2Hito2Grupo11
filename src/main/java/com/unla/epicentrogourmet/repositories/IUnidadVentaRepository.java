package com.unla.epicentrogourmet.repositories;

import com.unla.epicentrogourmet.entities.UnidadVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("unidadVentaRepository")
public interface IUnidadVentaRepository extends JpaRepository<UnidadVenta, Integer> {

    //Devuelve las unidades de venta del responsable logueado
    List<UnidadVenta> findByResponsableMail (String mail);

}
