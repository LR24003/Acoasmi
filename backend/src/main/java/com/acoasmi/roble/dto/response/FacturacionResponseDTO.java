package com.acoasmi.roble.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO que representa la respuesta detallada de una Factura / DTE registrado exitosamente")
public class FacturacionResponseDTO {

    @Schema(description = "ID único de la factura generado por el sistema", example = "1")
    private Long idFactura;

    @Schema(description = "Nombre o Razón Social de la empresa emisora", example = "ACOASMI DE R.L.")
    private String nombreEmpresa;

    @Schema(description = "NIT de la empresa emisora", example = "0614-010190-101-0")
    private String nitEmpresa;

    @Schema(description = "NRC de la empresa emisora", example = "123456-7")
    private String nrcEmpresa;

    @Schema(description = "Nombre del usuario cajero que registró la operación", example = "juancarlos.cajero")
    private String usuarioCajero;

    @Schema(description = "Número o identificador de la caja activa", example = "01")
    private String numeroCaja;

    @Schema(description = "Número institucional o código único asignado al asociado (Nulo si es Consumidor Final)", example = "10452")
    private Integer numeroAsociado;

    @Schema(description = "Nombre completo del asociado receptor (o 'CONSUMIDOR FINAL')", example = "José Perez Mendez")
    private String nombreCompletoAsociado;

    @Schema(description = "Número de documento de identificación del receptor (DUI / NIT)", example = "01234567-8")
    private String numDocumentoReceptor;

    @Schema(description = "Tipo de documento del receptor", example = "DUI")
    private String tipoDocumentoReceptor;

    @Schema(description = "Correo electrónico del receptor para envío de DTE", example = "jose.perez@email.com")
    private String emailReceptor;

    @Schema(description = "Tipo de Documento Tributario Electrónico (Hacienda)", example = "01")
    private String tipoDte;

    @Schema(description = "Código de generación único del DTE (UUID)", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID codigoGeneracionUuid;

    @Schema(description = "Número de control correlativo asignado internamente", example = "DTE-01-C01-00000001")
    private String numeroControl;

    @Schema(description = "Sello de recepción otorgado por el Ministerio de Hacienda", example = "20261234567890ABCDEF123456")
    private String selloRecepcionMh;

    @Schema(description = "Estado del DTE en el flujo de transmisión", example = "PROCESADO")
    private String estadoDte;

    @Schema(description = "Forma de pago registrada", example = "EFECTIVO")
    private String formaPago;

    @Schema(description = "Moneda de la transacción", example = "USD")
    private String moneda;

    @Schema(description = "Monto gravado de la factura", example = "100.00")
    private BigDecimal montoGravado;

    @Schema(description = "Monto exento de la factura", example = "0.00")
    private BigDecimal montoExento;

    @Schema(description = "Monto no sujeto a impuestos", example = "0.00")
    private BigDecimal montoNoSujeto;

    @Schema(description = "Subtotal antes de impuestos y descuentos", example = "100.00")
    private BigDecimal montoSubtotal;

    @Schema(description = "Monto total de descuentos aplicados", example = "0.00")
    private BigDecimal montoDescuento;

    @Schema(description = "Monto del IVA calculado", example = "13.00")
    private BigDecimal montoIva;

    @Schema(description = "Monto total final de la factura", example = "113.00")
    private BigDecimal montoTotal;

    @Schema(description = "Monto total expresado en letras", example = "CIENTO TRECE 00/100 USD")
    private String montoTotalLetras;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @Schema(
            description = "Fecha y hora exacta de cuándo se consolidó la factura en el servidor.",
            example = "20-07-2026 14:30:00"
    )
    private LocalDateTime fechaEmision;

    @Schema(description = "ID de la partida contable vinculada automáticamente en el libro diario", example = "45")
    private Long idPartidaContable;

    @Schema(description = "Lista detallada de los ítems, productos o servicios cobrados en la factura")
    private List<FacturaDetallesResponseDTO> detalles;
}