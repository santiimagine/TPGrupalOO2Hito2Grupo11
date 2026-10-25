package com.unla.epicentrogourmet.repositories;

import java.util.List;
import java.util.Optional;

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

    //Platos activos del menu de la unidad. Los filtros son opcionales: si llegan en null no se aplican.
    @Query("select p from UnidadVenta u join u.platos p "
            + "where u.id = :unidadId and p.activo = true "
            + "and (:nombre is null or lower(p.nombre) like lower(concat('%', :nombre, '%'))) "
            + "and (:precioMin is null or p.precioVenta >= :precioMin) "
            + "and (:precioMax is null or p.precioVenta <= :precioMax) "
            + "order by p.nombre")
    List<Plato> filtrar(@Param("unidadId") Integer unidadId, @Param("nombre") String nombre,
                        @Param("precioMin") Double precioMin, @Param("precioMax") Double precioMax);

    //Devuelve [idPlato, nombre, precioVenta, cantidad de pedidos en los que aparece, unidades vendidas]
    //de la unidad en el festival. El primero es el plato estrella: mas pedidos, y si empatan, mas unidades vendidas.
    @Query("select i.plato.id, i.plato.nombre, i.plato.precioVenta, count(distinct p.id), sum(i.cantidad) "
            + "from Pedido p join p.items i "
            + "where p.unidadVenta.id = :unidadId and p.festival.id = :festivalId "
            + "group by i.plato.id, i.plato.nombre, i.plato.precioVenta "
            + "order by count(distinct p.id) desc, sum(i.cantidad) desc, i.plato.nombre asc")
    List<Object[]> platosPorCantidadDePedidos(@Param("unidadId") Integer unidadId,
                                              @Param("festivalId") Integer festivalId);

    //Plato con ese id solo si pertenece al menu de la unidad (asi un responsable no toca platos de otro)
    @Query("select p from UnidadVenta u join u.platos p where u.id = :unidadId and p.id = :platoId")
    Optional<Plato> buscarEnUnidad(@Param("unidadId") Integer unidadId, @Param("platoId") Integer platoId);

    //Cantidad de OTROS platos de la unidad con ese nombre (activos o dados de baja), para no repetir nombres al modificar
    @Query("select count(p) from UnidadVenta u join u.platos p "
            + "where u.id = :unidadId and lower(p.nombre) = lower(:nombre) and p.id <> :platoId")
    long contarOtrosPorNombreEnUnidad(@Param("unidadId") Integer unidadId, @Param("nombre") String nombre,
                                      @Param("platoId") Integer platoId);

    //Platos dados de baja (eliminados logicamente) del menu de la unidad
    @Query("select p from UnidadVenta u join u.platos p where u.id = :unidadId and p.activo = false order by p.nombre")
    List<Plato> listarBajas(@Param("unidadId") Integer unidadId);
}
