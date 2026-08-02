package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.Facturas;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FacturacionRepository extends AcoasmiRepository<Facturas, Long> {

    Optional<Facturas> findByCodigoGeneracionUuid(UUID codigoGeneracionUuid);

    Boolean existsByCodigoGeneracionUuid(UUID uuid);

    Boolean existsByNumeroControl(String numeroControl);

    @Query(value = "SELECT f FROM Facturas f " +
            "LEFT JOIN FETCH f.asociado " +
            "LEFT JOIN FETCH f.usuario u " +
            "WHERE u.usuario = :usuario",
            countQuery = "SELECT COUNT(f) FROM Facturas f WHERE f.usuario.usuario = :usuario")
    Page<Facturas> findByUsuarioEmisor(@Param("usuario") String usuario, Pageable pageable);

    @Query(value = "SELECT f FROM Facturas f " +
            "LEFT JOIN FETCH f.asociado a " +
            "WHERE a.numeroAsociado = :numeroAsociado",
            countQuery = "SELECT COUNT(f) FROM Facturas f WHERE f.asociado.numeroAsociado = :numeroAsociado")
    Page<Facturas> findByNumeroAsociado(@Param("numeroAsociado") Integer numeroAsociado, Pageable pageable);

    @Query(value = "SELECT f FROM Facturas f " +
            "LEFT JOIN FETCH f.asociado " +
            "WHERE f.fechaEmision BETWEEN :fechaInicio AND :fechaFin",
            countQuery = "SELECT COUNT(f) FROM Facturas f WHERE f.fechaEmision BETWEEN :fechaInicio AND :fechaFin")
    Page<Facturas> findByRangoFechas(@Param("fechaInicio") LocalDateTime fechaInicio,
                                     @Param("fechaFin") LocalDateTime fechaFin,
                                     Pageable pageable);

    @Query("SELECT f FROM Facturas f " +
            "LEFT JOIN FETCH f.detalles d " +
            "LEFT JOIN FETCH f.asociado " +
            "LEFT JOIN FETCH f.caja " +
            "WHERE f.id = :id")
    Optional<Facturas> findByIdConDetalles(@Param("id") Long id);
}