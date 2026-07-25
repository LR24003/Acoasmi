package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.SolicitudesCredito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudesCreditoRepository extends AcoasmiRepository<SolicitudesCredito, Long> {

    Page<SolicitudesCredito> findByEstadoPrestamo(String estadoPrestamo, Pageable pageable);

    List<SolicitudesCredito> findByUsuarioAsesor_Usuario(String usuarioAsesor);

    @Query("SELECT DISTINCT s FROM SolicitudesCredito s " +
            "LEFT JOIN FETCH s.creditoDetalle " +
            "LEFT JOIN FETCH s.garantias g " +
            "LEFT JOIN FETCH g.garantia " +
            "WHERE s.numeroSolicitud= :numeroSolicitud")
    Optional<SolicitudesCredito> findByNumeroSolicitudWithDetailsAndGarantias(@Param("numeroSolicitud") String numeroSolicitud);

    @Query("SELECT DISTINCT s FROM SolicitudesCredito s " +
            "LEFT JOIN FETCH s.referencias r " +
            "LEFT JOIN FETCH r.referencia " +
            "LEFT JOIN FETCH s.documentosAdjuntos " +
            "WHERE s.numeroSolicitud = :numeroSolicitud")
    Optional<SolicitudesCredito> findByNumeroSolicitudWithReferenciasAndDocumentos(@Param("numeroSolicitud") String numeroSolicitud);

    Optional<SolicitudesCredito> findTopByNumeroSolicitudStartingWithOrderByNumeroSolicitudDesc(String prefijo);
}
