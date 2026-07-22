package com.acoasmi.roble.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadoCuentaResponseDTO {

    private String numeroAsociado;
    private String nombreAsociado;
    private String direccion;
    private String numeroCuenta;
    private String tipoCuenta;
    private String tipoAhorro;
    private LocalDate fechaUltimaCapitalizacion;
    private BigDecimal montoApertura;
    private BigDecimal tasaInteres;
    private Integer plazoDias;
    private String fechaInicio;
    private String fechaFin;
    private BigDecimal saldoInicial;

    private List<DetalleMovimientoEstadoCuentaDTO> movimientos;

    private BigDecimal totalDepositos;
    private BigDecimal totalRetiros;
    private BigDecimal totalIntereses;
}