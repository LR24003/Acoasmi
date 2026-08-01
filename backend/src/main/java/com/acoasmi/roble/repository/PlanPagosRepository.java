package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.PlanPagos;
import com.acoasmi.roble.entity.PlanPagos.EstadoCuota;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanPagosRepository extends AcoasmiRepository<PlanPagos, Long> {

    @Query("SELECT p FROM PlanPagos p WHERE p.prestamo.numeroPrestamo = :numeroPrestamo ORDER BY p.numeroCuota ASC")
    List<PlanPagos> findByNumeroPrestamoOrderByNumeroCuotaAsc(@Param("numeroPrestamo") String numeroPrestamo);


    @Query("SELECT p FROM PlanPagos p WHERE p.prestamo.numeroPrestamo = :numeroPrestamo AND p.numeroCuota = :numeroCuota")
    Optional<PlanPagos> findByNumeroPrestamoAndNumeroCuota(
            @Param("numeroPrestamo") String numeroPrestamo,
            @Param("numeroCuota") Integer numeroCuota
    );


    @Query("SELECT p FROM PlanPagos p WHERE p.prestamo.numeroPrestamo = :numeroPrestamo AND p.estadoCuota = :estadoCuota ORDER BY p.numeroCuota ASC")
    List<PlanPagos> findByNumeroPrestamoAndEstadoCuota(
            @Param("numeroPrestamo") String numeroPrestamo,
            @Param("estadoCuota") EstadoCuota estadoCuota
    );

    @Query("SELECT p FROM PlanPagos p WHERE p.prestamo.numeroPrestamo = :numeroPrestamo AND p.estadoCuota = 'PENDIENTE' ORDER BY p.numeroCuota ASC LIMIT 1")
    Optional<PlanPagos> findProximaCuotaPendiente(@Param("numeroPrestamo") String numeroPrestamo);

    void deleteByPrestamoNumeroPrestamo(String numeroPrestamo);
}