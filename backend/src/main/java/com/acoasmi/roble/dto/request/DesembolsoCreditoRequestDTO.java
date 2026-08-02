package com.acoasmi.roble.dto.request;

import com.acoasmi.roble.enums.FormaPago;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la creación de un desembolso de Crédito basado en códigos de negocio")
public class DesembolsoCreditoRequestDTO {

    @NotNull(message = "El número de asociado es obligatorio")
    @Schema(description = "Número o código correlativo del asociado", example = "1850")
    private Integer numeroAsociado;

    @NotBlank(message = "El número de préstamo es obligatorio")
    @Size(max = 30, message = "El número de préstamo no puede exceder 30 caracteres")
    @Schema(description = "Número correlativo o código del préstamo", example = "PRES-2026-0001")
    private String numeroPrestamo;

    @NotBlank(message = "El número de solicitud es obligatorio")
    @Size(max = 20, message = "El número de solicitud no puede exceder 20 caracteres")
    @Schema(description = "Número correlativo de la solicitud del crédito", example = "SOLI-01-0002")
    private String numeroSolicitud;

    @NotNull(message = "El ID de la sesión de caja es obligatorio para emitir la factura")
    @Schema(description = "Id único de la caja que procesa el desembolso", example = "1")
    private Long idSesionCaja;

    @NotBlank(message = "El número de desembolso es obligatorio")
    @Size(max = 20, message = "El número de desembolso no puede exceder 20 caracteres")
    @Schema(description = "Número correlativo de desembolso", example = "DESE-00-0001")
    private String numeroDesembolso;

    @NotNull(message = "El monto bruto es obligatorio")
    @Positive(message = "El monto bruto debe ser mayor a cero")
    @Digits(integer = 10, fraction = 2, message = "El monto no puede exceder 10 enteros y 2 decimales")
    @Schema(description = "Monto total aprobado en la solicitud", example = "1500.00")
    private BigDecimal montoBrutoDesembolso;

    @NotNull(message = "La forma de pago es obligatoria")
    @Schema(description = "Forma de pago en la que se entregará el desembolso", example = "EFECTIVO")
    private FormaPago formaPago;

    @Size(max = 50, message = "El número de cuenta destino no puede exceder 50 caracteres")
    @Schema(description = "Número de cuenta de ahorro del asociado (Requerido si formaPago es ABONO_EN_CUENTA_AHORRO)", example = "1011-1850-1")
    private String numeroCuentaDestino;

    @Size(max = 50, message = "El número de comprobante no puede exceder 50 caracteres")
    @Schema(description = "Número de comprobante según detalles del desembolso", example = "D-00-0001")
    private String numeroComprobante;

    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    @Schema(description = "Observaciones para realizar el desembolso", example = "Descuento de crédito vigente")
    private String observaciones;

    @NotBlank(message = "El tipo de DTE es obligatorio")
    @Schema(description = "Tipo de DTE a emitir según facturación electrónica", example = "01")
    private String tipoDte;

    @Valid
    @NotNull(message = "La lista de deducciones no debe ser nula")
    @Builder.Default
    @Schema(description = "Listado de deducciones del desembolso según aplique")
    private List<DesembolsoDeduccionesRequestDTO> deducciones = new ArrayList<>();
}