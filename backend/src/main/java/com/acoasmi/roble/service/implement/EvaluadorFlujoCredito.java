package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.enums.EstadoSolicitudCredito;
import java.math.BigDecimal;

public class EvaluadorFlujoCredito {

    private static final BigDecimal LIMITE_MAXIMO_GERENCIA = new BigDecimal("2000.00");
    private static final BigDecimal LIMITE_MAXIMO_COMITE = new BigDecimal("7500.00");

    public static EstadoSolicitudCredito determinarSiguienteEstadoAprobacion(EstadoSolicitudCredito estadoActual, BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto del crédito debe ser mayor a cero.");
        }

        return switch (estadoActual) {
            case EN_ANALISIS_ASESOR -> EstadoSolicitudCredito.EN_REVISION_GERENCIA;

            case EN_REVISION_GERENCIA -> {
                if (monto.compareTo(LIMITE_MAXIMO_GERENCIA) > 0) {
                    yield EstadoSolicitudCredito.EN_REVISION_COMITE_CREDITOS;
                }
                yield EstadoSolicitudCredito.APROBADA;
            }

            case EN_REVISION_COMITE_CREDITOS -> {
                if (monto.compareTo(LIMITE_MAXIMO_COMITE) > 0) {
                    yield EstadoSolicitudCredito.EN_REVISION_CONSEJO_ADMINISTRACION;
                }
                yield EstadoSolicitudCredito.APROBADA;
            }

            case EN_REVISION_CONSEJO_ADMINISTRACION -> EstadoSolicitudCredito.APROBADA;

            default -> throw new IllegalStateException("Transición de aprobación no válida para el estado: " + estadoActual);
        };
    }
}