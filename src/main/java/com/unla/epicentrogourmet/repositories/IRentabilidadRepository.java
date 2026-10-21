package com.unla.epicentrogourmet.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.unla.epicentrogourmet.entities.Pedido;

//Consultas de los totales que usa la rentabilidad. Se separa de IPedidoRepository para no mezclar reportes.
public interface IRentabilidadRepository extends Repository<Pedido, Integer> {

    //Devuelve una fila: [ingresos, costo de los platos vendidos, cantidad de pedidos]
    //de la unidad en el festival, con pedidos entre las dos fechas (inclusive).
    //Ingresos = cantidad * precio guardado en el item; costo = cantidad * costo de produccion del plato.
    @Query("select coalesce(sum(i.cantidad * i.precioUnitario), 0.0), "
            + "coalesce(sum(i.cantidad * i.plato.costoProduccion), 0.0), count(distinct p.id) "
            + "from Pedido p join p.items i "
            + "where p.unidadVenta.id = :unidadId and p.festival.id = :festivalId "
            + "and p.fecha between :desde and :hasta")
    List<Object[]> totales(@Param("unidadId") Integer unidadId, @Param("festivalId") Integer festivalId,
                           @Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);
}
