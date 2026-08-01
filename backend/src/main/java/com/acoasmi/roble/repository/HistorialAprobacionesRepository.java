package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.HistorialAprobaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialAprobacionesRepository extends AcoasmiRepository<HistorialAprobaciones, Long> {

    @Query("SELECT h FROM HistorialAprobaciones h " +
            "LEFT JOIN FETCH h.usuarioResponsable " +
            "WHERE h.solicitudCredito.numeroSolicitud = :numeroSolicitud " +
            "ORDER BY h.fechaAprobacion DESC")
    List<HistorialAprobaciones> findByNumeroSolicitudOrderByFechaDesc(@Param("numeroSolicitud") String numeroSolicitud);

    @Query("SELECT h FROM HistorialAprobaciones h " +
            "LEFT JOIN FETCH h.solicitudCredito s " +
            "LEFT JOIN FETCH s.asociado a " +
            "LEFT JOIN FETCH h.usuarioResponsable " +
            "WHERE a.numeroAsociado = :numeroAsociado " +
            "ORDER BY h.fechaAprobacion DESC")
    List<HistorialAprobaciones> findByNumeroAsociadoOrderByFechaDesc(@Param("numeroAsociado") Integer numeroAsociado);

    @Query("SELECT h FROM HistorialAprobaciones h " +
            "WHERE UPPER(h.usuarioResponsable.usuario) = UPPER(:usuario) " +
            "ORDER BY h.fechaAprobacion DESC")
    List<HistorialAprobaciones> findByUsuarioResponsableIgnoreCase(@Param("usuario") String usuario);
}