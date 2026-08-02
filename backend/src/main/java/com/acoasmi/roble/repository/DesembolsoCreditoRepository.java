package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.DesembolsoCredito;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DesembolsoCreditoRepository extends AcoasmiRepository<DesembolsoCredito, Long> {

    Optional<DesembolsoCredito> findByNumeroDesembolso(String numeroDesembolso);

    List<DesembolsoCredito> findByNumeroSolicitud(String numeroSolicitud);

    List<DesembolsoCredito> findByPrestamoNumeroPrestamo(String numeroPrestamo);

    List<DesembolsoCredito> findByAsociadoNumeroAsociado(Integer numeroAsociado);

    List<DesembolsoCredito> findByFechaDesembolsoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    @Query("SELECT d FROM DesembolsoCredito d " +
            "LEFT JOIN FETCH d.asociado " +
            "LEFT JOIN FETCH d.prestamo " +
            "LEFT JOIN FETCH d.solicitud " +
            "LEFT JOIN FETCH d.factura " +
            "LEFT JOIN FETCH d.cuenta " +
            "LEFT JOIN FETCH d.deducciones " +
            "WHERE d.id = :id")
    Optional<DesembolsoCredito> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT d FROM DesembolsoCredito d " +
            "LEFT JOIN FETCH d.asociado " +
            "LEFT JOIN FETCH d.prestamo " +
            "LEFT JOIN FETCH d.solicitud " +
            "LEFT JOIN FETCH d.factura " +
            "LEFT JOIN FETCH d.cuenta " +
            "LEFT JOIN FETCH d.deducciones " +
            "WHERE d.numeroDesembolso = :numeroDesembolso")
    Optional<DesembolsoCredito> findByNumeroDesembolsoWithDetails(@Param("numeroDesembolso") String numeroDesembolso);
}
