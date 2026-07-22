package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.AsociadoMovimientos;
import com.acoasmi.roble.repository.projection.DetalleEstadoCuentaProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AsociadoMovimientosRepository extends AcoasmiRepository<AsociadoMovimientos, Long> {

    @Query("SELECT m FROM AsociadoMovimientos m WHERE m.cuenta.numeroCuenta = :numeroCuenta " +
            "ORDER BY m.fechaMovimiento DESC")
    List<AsociadoMovimientos> findCuentaHistorialPorNumeroCuenta(@Param("numeroCuenta") String numeroCuenta);


    @Query("SELECT m FROM AsociadoMovimientos m WHERE m.cuenta.numeroCuenta = :numeroCuenta " +
            "ORDER BY m.fechaMovimiento DESC")
    Page<AsociadoMovimientos> findCuentaHistorialPorNumeroCuentaPaginado(@Param("numeroCuenta") String numeroCuenta, Pageable pageable);


    @Query("SELECT m FROM AsociadoMovimientos m " +
            "JOIN FETCH m.cuenta c " +
            "JOIN FETCH c.asociado a " +
            "WHERE m.id = :idMovimiento")
    Optional<AsociadoMovimientos> findMovimientoConDetallesParaComprobante(@Param("idMovimiento") Long idMovimiento);

    @Query(value = """
SELECT
    m.numero_comprobante AS numeroComprobante,
    m.fecha_movimiento::date AS fechaAplicacion,
    CASE
        WHEN m.tipo_movimiento IN ('DEPOSITO', 'APORTACIONES', 'APORTACION', 'ABONO') THEN m.monto
        ELSE 0.00
    END AS deposito,
    CASE
        WHEN m.tipo_movimiento IN ('RETIRO', 'DEBITO', 'PAGO_PRESTAMO') THEN m.monto
        ELSE 0.00
    END AS retiro,
    CASE
        WHEN m.tipo_movimiento IN ('INTERESES', 'INTERES') THEN m.monto
        ELSE 0.00
    END AS intereses,
    COALESCE(m.cambio_tasa, 0.00) AS cambioTasa,
    m.saldo_resultante AS saldo
FROM asociado_movimientos m
INNER JOIN asociado_cuentas c ON m.id_cuenta = c.id_cuenta
INNER JOIN asociados a ON c.id_asociado = a.id_asociado
WHERE CAST(a.numero_asociado AS text) = :numeroAsociado
  AND m.fecha_movimiento >= CAST(to_date(:fechaInicio, 'DD-MM-YYYY') AS timestamp)
  AND m.fecha_movimiento <= (to_date(:fechaFin, 'DD-MM-YYYY') + INTERVAL '1 day - 1 microsecond')
ORDER BY m.fecha_movimiento ASC, m.id_movimiento ASC
""", nativeQuery = true)
    List<DetalleEstadoCuentaProjection> obtenerDetalleEstadoCuentaPorAsociado(
            @Param("numeroAsociado") String numeroAsociado,
            @Param("fechaInicio") String fechaInicio,
            @Param("fechaFin") String fechaFin
    );

    @Query("SELECT m.saldoResultante FROM AsociadoMovimientos m " +
            "WHERE m.cuenta.numeroCuenta = :numeroCuenta " +
            "AND m.fechaMovimiento < CAST(:fechaInicio AS LocalDate) " +
            "ORDER BY m.fechaMovimiento DESC, m.id DESC")
    List<BigDecimal> obtenerSaldoAnteriorAFecha(@Param("numeroCuenta") String numeroCuenta,
                                                @Param("fechaInicio") String fechaInicio);

    @Query("SELECT COUNT(m) FROM AsociadoMovimientos m " +
            "WHERE m.idPrestamo = :idPrestamo " +
            "AND UPPER(m.tipoMovimiento) IN ('DESEMBOLSO_PRESTAMO', 'DEPOSITO_DESEMBOLSO')")
    int countDesembolsosByPrestamoId(@Param("idPrestamo") Long idPrestamo);
}