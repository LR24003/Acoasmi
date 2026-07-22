package com.acoasmi.roble.repository.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DetalleEstadoCuentaProjection {
    String getNumeroComprobante();
    LocalDate getFechaAplicacion();
    BigDecimal getDeposito();
    BigDecimal getRetiro();
    BigDecimal getIntereses();
    BigDecimal getCambioTasa();
    BigDecimal getSaldo();
}