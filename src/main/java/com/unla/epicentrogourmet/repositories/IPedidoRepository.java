package com.unla.epicentrogourmet.repositories;

import com.unla.epicentrogourmet.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("pedidoRepository")
public interface IPedidoRepository extends JpaRepository<Pedido, Integer> {
    //Devuelve [idUnidadVenta, recaudacion] de las unidades que tuvieron pedidos en el festival
    @Query("select p.unidadVenta.id, sum(i.cantidad * i.precioUnitario) "
            + "from Pedido p join p.items i "
            + "where p.festival.id = :festivalId "
            + "group by p.unidadVenta.id")
    List<Object[]> recaudacionPorUnidad(@Param("festivalId") Integer festivalId);

    //Cantidad de pedidos del festival
    long countByFestivalId(Integer festivalId);


}
