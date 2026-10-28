package com.unla.epicentrogourmet.repositories;

import com.unla.epicentrogourmet.entities.Empleado;
import com.unla.epicentrogourmet.entities.UnidadVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Repository("empleadoRepository")
public interface IEmpleadoRepository extends JpaRepository<Empleado, Integer> {

    @Query("SELECT e FROM Empleado e WHERE e.usuario.mail = :mail")
    Optional<Empleado> traerPorMail(@Param("mail") String mail);

    @Query("SELECT u FROM UnidadVenta u JOIN u.personal p WHERE p.id = :empleadoId")
    Collection<UnidadVenta> traerUnidades(@Param("empleadoId") Integer empleadoId);

    @Query("SELECT DISTINCT e FROM UnidadVenta u JOIN u.personal e " +
            "WHERE u.responsable.mail = :mail AND e.activo = true "+
            "AND (:textoBusqueda IS NULL OR LOWER(CONCAT(e.nombre, ' ',e.apellido)) LIKE :textoBusqueda OR e.dni LIKE :textoBusqueda)"+
            "AND (:desde IS NULL OR e.fechaIngreso >= :desde) " +
            "AND (:hasta IS NULL OR e.fechaIngreso <= :hasta) " +
            "AND (:unidadId IS NULL OR u.id = :unidadId) " +
            "AND (:tipo IS NULL OR (:tipo = 'COCINERO' AND TYPE(e) = Cocinero) OR (:tipo = 'CAJERO' AND TYPE(e) = Cajero)) "+
            "ORDER BY e.apellido, e.nombre")
    List<Empleado> filtrarActivosPorResponsable(@Param("mail") String mail,
                                                @Param("textoBusqueda") String textoBusqueda,
                                                @Param("desde") LocalDate desde,
                                                @Param("hasta") LocalDate hasta,
                                                @Param("unidadId") Integer unidadId,
                                                @Param("tipo") String tipo);

    @Query("SELECT COUNT (u) FROM UnidadVenta u JOIN u.personal e " +
            "WHERE e.id = :id AND u.responsable.mail = :mail")
    long perteneceAResponsable(@Param("id") Integer id, @Param("mail") String mail);

    boolean existsByDniAndIdNot(String dni, Integer id);

    @Query("SELECT DISTINCT e FROM UnidadVenta u JOIN u.personal e "
            + "WHERE u.responsable.mail = :mail AND e.activo = false "
            + "ORDER BY e.apellido, e.nombre")
    List<Empleado> traerInactivosPorResponsable(@Param("mail") String mail);

    Optional<Empleado> findByDni(String dni);

    @Query("SELECT u FROM UnidadVenta u JOIN u.personal e "+
            "WHERE e.id = :empleadoId AND :hoy BETWEEN u.festival.fechaInicio AND u.festival.fechaFin")
    List<UnidadVenta> traerUnidadesEnCurso(@Param("empleadoId") Integer empleadoId, @Param("hoy") LocalDate hoy);

}

