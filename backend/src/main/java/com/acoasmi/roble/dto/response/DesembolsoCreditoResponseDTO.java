package com.acoasmi.roble.dto.response;

import com.acoasmi.roble.enums.FormaPago;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta tras el procesamiento del desembolso de Crédito")
public class DesembolsoCreditoResponseDTO {

    @Schema(description = "ID único del desembolso generado", example = "100")
    private Long id;

    @Schema(description = "Numero de la caja que procesa el desembolso", example = "CAJA-01")
    private String numeroCaja;

    @Schema(description = "Numero Correlativo del préstamo luego del desembolso", example = "1")
    private String numeroPrestamo;

    @Schema(description = "ID único de la factura generada en la transacción", example = "450")
    private Long idFactura;

    @Schema(description = "Número correlativo o código del asociado", example = "1850")
    private Integer numeroAsociado;

    @Schema(description = "Nombre completo del asociado", example = "Juan Carlos Pérez")
    private String nombreCompletoAsociado;

    @Schema(description = "Número correlativo del desembolso", example = "DESE-00-0001")
    private String numeroDesembolso;

    @Schema(description = "Número correlativo de la solicitud del crédito", example = "SOLI-01-0002")
    private String numeroSolicitud;

    @Schema(description = "Monto total aprobado en la solicitud", example = "1500.00")
    private BigDecimal montoBrutoDesembolso;

    @Schema(description = "Porcentaje de tasa de interés anual", example = "12.50", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal tasaInteresAnual;

    @Schema(description = "Plazo de pago expresado en meses", example = "24", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer plazoMeses;

    @Schema(description = "Frecuencia con la que se realizarán los pagos", example = "MENSUAL", requiredMode = Schema.RequiredMode.REQUIRED)
    private String frecuenciaPago;

    @Schema(description = "Suma total de las deducciones aplicadas", example = "150.00")
    private BigDecimal totalDeducciones;

    @Schema(description = "Monto líquido o neto entregado al asociado", example = "1350.00")
    private BigDecimal montoNetoEntregado;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    @Schema(description = "Fecha y hora en que se registró el desembolso", example = "02-08-2026 11:05")
    private LocalDateTime fechaDesembolso;

    @Schema(description = "Forma de pago utilizada", example = "EFECTIVO")
    private FormaPago formaPago;

    @Schema(description = "Número de cuenta de ahorro receptora (si aplica)", example = "1011-1850-1")
    private String numeroCuentaDestino;

    @Schema(description = "Número de comprobante emitido", example = "D-00-0001")
    private String numeroComprobante;

    @Schema(description = "Observaciones registradas en el desembolso", example = "Descuento de crédito vigente")
    private String observaciones;

    @Builder.Default
    @Schema(description = "Listado detallado de las deducciones procesadas")
    private List<DesembolsoDeduccionesResponseDTO> deducciones = new ArrayList<>();

    @Schema(description = "Responsable que proceso el desembolso", example = "Descuento de crédito vigente")
    private String usuarioCajero;
}
