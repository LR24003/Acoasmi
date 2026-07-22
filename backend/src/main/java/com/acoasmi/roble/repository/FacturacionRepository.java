package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.Facturas;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FacturacionRepository extends AcoasmiRepository<Facturas, Long> {

    Optional<Facturas> findByCodigoGeneracionUuid(UUID codigoGeneracionUuid);

    Optional<Facturas> findByNumeroControl(String numeroControl);

    @Query("SELECT f FROM Facturas f WHERE f.caja.usuarioCajero = :usuario")
    Page<Facturas> findByUsuarioCajero(@Param("usuario") String usuario, Pageable pageable);

    @Query("SELECT f FROM Facturas f WHERE f.asociado.numeroAsociado = :numeroAsociado")
    Page<Facturas> findByNumeroAsociado(@Param("numeroAsociado") Integer numeroAsociado, Pageable pageable);
}