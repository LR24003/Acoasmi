package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.PrestamosConcedidos;
import com.acoasmi.roble.entity.PrestamosConcedidos.EstadoPrestamo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrestamosConcedidosRepository extends AcoasmiRepository<PrestamosConcedidos, Long> {

    Optional<PrestamosConcedidos> findByNumeroPrestamo(String numeroPrestamo);

    @Query("SELECT p FROM PrestamosConcedidos p WHERE p.asociado.numeroAsociado = :numeroAsociado")
    List<PrestamosConcedidos> findByNumeroAsociado(@Param("numeroAsociado") String numeroAsociado);

    List<PrestamosConcedidos> findByEstadoPrestamo(EstadoPrestamo estadoPrestamo);

    @Query("SELECT p FROM PrestamosConcedidos p " +
            "JOIN FETCH p.asociado a " +
            "JOIN FETCH p.credito s " +
            "WHERE p.numeroPrestamo = :numeroPrestamo")
    Optional<PrestamosConcedidos> findByNumeroPrestamoConRelaciones(@Param("numeroPrestamo") String numeroPrestamo);
}
