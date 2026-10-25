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

    //Una fila por plato vendido: [id del plato, nombre, unidades vendidas, ingresos, costo de produccion]
    //de la unidad en el festival, con pedidos entre las dos fechas (inclusive).
    @Query("select i.plato.id, i.plato.nombre, sum(i.cantidad), "
            + "sum(i.cantidad * i.precioUnitario), sum(i.cantidad * i.plato.costoProduccion) "
            + "from Pedido p join p.items i "
            + "where p.unidadVenta.id = :unidadId and p.festival.id = :festivalId "
            + "and p.fecha between :desde and :hasta "
            + "group by i.plato.id, i.plato.nombre")
    List<Object[]> totalesPorPlato(@Param("unidadId") Integer unidadId, @Param("festivalId") Integer festivalId,
                                   @Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);

    //Una fila por dia con ventas: [fecha, cantidad de pedidos, ingresos] de la unidad en el festival,
    //con pedidos entre las dos fechas (inclusive), de la fecha mas vieja a la mas nueva.
    @Query("select p.fecha, count(distinct p.id), sum(i.cantidad * i.precioUnitario) "
            + "from Pedido p join p.items i "
            + "where p.unidadVenta.id = :unidadId and p.festival.id = :festivalId "
            + "and p.fecha between :desde and :hasta "
            + "group by p.fecha order by p.fecha")
    List<Object[]> ventasPorDia(@Param("unidadId") Integer unidadId, @Param("festivalId") Integer festivalId,
                                @Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);
}
