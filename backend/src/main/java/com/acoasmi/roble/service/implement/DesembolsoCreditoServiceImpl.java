package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.DesembolsoCreditoRequestDTO;
import com.acoasmi.roble.dto.request.DesembolsoDeduccionesRequestDTO;
import com.acoasmi.roble.dto.response.DesembolsoCreditoResponseDTO;
import com.acoasmi.roble.dto.response.DesembolsoDeduccionesResponseDTO;
import com.acoasmi.roble.entity.*;
import com.acoasmi.roble.enums.FormaPago;
import com.acoasmi.roble.repository.*;
import com.acoasmi.roble.service.DesembolsoCreditoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DesembolsoCreditoServiceImpl
        extends AcoasmiServiceImpl<DesembolsoCredito, DesembolsoCreditoRequestDTO, DesembolsoCreditoResponseDTO, Long>
        implements DesembolsoCreditoService {

    private final DesembolsoCreditoRepository desembolsoRepository;
    private final AsociadosRepository asociadosRepository;
    private final PrestamosConcedidosRepository prestamosRepository;
    private final SolicitudesCreditoRepository solicitudesRepository;
    private final AsociadoCuentasRepository cuentasRepository;
    private final FacturacionRepository facturacionRepository;

    public DesembolsoCreditoServiceImpl(
            DesembolsoCreditoRepository desembolsoRepository,
            AsociadosRepository asociadosRepository,
            PrestamosConcedidosRepository prestamosRepository,
            SolicitudesCreditoRepository solicitudesRepository,
            AsociadoCuentasRepository cuentasRepository,
            FacturacionRepository facturacionRepository) {
        super(desembolsoRepository, DesembolsoCredito.class);
        this.desembolsoRepository = desembolsoRepository;
        this.asociadosRepository = asociadosRepository;
        this.prestamosRepository = prestamosRepository;
        this.solicitudesRepository = solicitudesRepository;
        this.cuentasRepository = cuentasRepository;
        this.facturacionRepository = facturacionRepository;
    }

    @Override
    protected void mapearDtoAEntidad(DesembolsoCreditoRequestDTO dto, DesembolsoCredito entity) {
        Asociados asociado = asociadosRepository.findByNumeroAsociado(dto.getNumeroAsociado())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el asociado número: " + dto.getNumeroAsociado()));

        PrestamosConcedidos prestamo = prestamosRepository.findByNumeroPrestamo(dto.getNumeroPrestamo())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el préstamo número: " + dto.getNumeroPrestamo()));

        SolicitudesCredito solicitud = solicitudesRepository.findByNumeroSolicitud(dto.getNumeroSolicitud())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la solicitud número: " + dto.getNumeroSolicitud()));

        AsociadoCuentas cuentaDestino = null;
        if (FormaPago.ABONO_EN_CUENTA_AHORRO.equals(dto.getFormaPago())) {
            if (dto.getNumeroCuentaDestino() == null || dto.getNumeroCuentaDestino().isBlank()) {
                throw new IllegalArgumentException("Debe proporcionar un número de cuenta destino para abono en cuenta.");
            }
            cuentaDestino = cuentasRepository.findByNumeroCuenta(dto.getNumeroCuentaDestino())
                    .orElseThrow(() -> new EntityNotFoundException("No se encontró la cuenta destino: " + dto.getNumeroCuentaDestino()));
        }

        entity.setAsociado(asociado);
        entity.setPrestamo(prestamo);
        entity.setSolicitud(solicitud);
        entity.setCuenta(cuentaDestino);

        entity.setNumeroDesembolso(dto.getNumeroDesembolso());
        entity.setMontoBrutoDesembolso(dto.getMontoBrutoDesembolso());
        entity.setFormaPago(dto.getFormaPago());
        entity.setNumeroCuentaDestino(dto.getNumeroCuentaDestino());
        entity.setObservaciones(dto.getObservaciones());
        entity.setFechaDesembolso(LocalDateTime.now());
        entity.setEstado(true);

        if (dto.getNumeroComprobante() != null && !dto.getNumeroComprobante().isBlank()) {
            entity.setNumeroComprobante(dto.getNumeroComprobante());
        } else {
            String tipoMovimiento = "DESEMBOLSO_PRESTAMO";
            String caja = (entity.getCaja() != null) ? entity.getCaja().getNumeroCaja() : "01";
            Integer numDesembolso = (dto.getNumeroDesembolso() != null && !dto.getNumeroDesembolso().isBlank())
                    ? Integer.parseInt(dto.getNumeroDesembolso())
                    : 1;

            entity.setNumeroComprobante(generarNumeroComprobante(
                    tipoMovimiento,
                    caja,
                    null,
                    entity.getFechaDesembolso(),
                    prestamo.getId(),
                    numDesembolso
            ));
        }

        BigDecimal totalDeducciones = BigDecimal.ZERO;

        if (entity.getDeducciones() == null) {
            entity.setDeducciones(new ArrayList<>());
        } else {
            entity.getDeducciones().clear();
        }

        if (dto.getDeducciones() != null && !dto.getDeducciones().isEmpty()) {
            for (DesembolsoDeduccionesRequestDTO dedDto : dto.getDeducciones()) {
                DesembolsoDeducciones deduccion = new DesembolsoDeducciones();
                deduccion.setDesembolso(entity);
                deduccion.setTipoDeduccion(dedDto.getTipoDeduccion());
                deduccion.setMonto(dedDto.getMonto());
                deduccion.setDescripcion(dedDto.getDescripcion());
                deduccion.setEstado(true);

                entity.getDeducciones().add(deduccion);
                totalDeducciones = totalDeducciones.add(dedDto.getMonto());
            }
        }

        entity.setTotalDeducciones(totalDeducciones);
        entity.setMontoNetoEntregado(dto.getMontoBrutoDesembolso().subtract(totalDeducciones));
    }

    private String generarNumeroComprobante(
            String tipoMovimiento,
            String caja,
            Long idMovimiento,
            LocalDateTime fechaMovimiento,
            Long idPrestamo,
            Integer numDesembolso
    ) {
        String numCaja = (caja != null && !caja.isBlank()) ? caja : "01";
        Long correlativo = (idMovimiento != null) ? idMovimiento : 0L;

        String tipoClean = (tipoMovimiento != null) ? tipoMovimiento.toUpperCase().trim() : "";

        switch (tipoClean) {
            case "DESEMBOLSO_PRESTAMO", "DEPOSITO_DESEMBOLSO":
                Long prestamoId = (idPrestamo != null) ? idPrestamo : 0L;
                int desembolsoCorrelativo = (numDesembolso != null && numDesembolso > 0) ? numDesembolso : 1;

                return String.format("D/%d/%d", prestamoId, desembolsoCorrelativo);

            case "INTERES", "INTERESES", "CAPITALIZACION_INTERES":
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMyyyy");
                String mesAno = (fechaMovimiento != null) ? fechaMovimiento.format(formatter) : LocalDateTime.now().format(formatter);
                return String.format("IN-%s/%d", mesAno, correlativo);

            default:
                return String.format("%s-%06d", numCaja, correlativo);
        }
    }

    @Override
    protected DesembolsoCreditoResponseDTO mapToResponseDTO(DesembolsoCredito entity) {
        if (entity == null) return null;

        List<DesembolsoDeduccionesResponseDTO> deduccionesDto = entity.getDeducciones() != null
                ? entity.getDeducciones().stream()
                .map(d -> DesembolsoDeduccionesResponseDTO.builder()
                        .tipoDeduccion(d.getTipoDeduccion())
                        .monto(d.getMonto())
                        .descripcion(d.getDescripcion())
                        .build())
                .collect(Collectors.toList())
                : new ArrayList<>();

        return DesembolsoCreditoResponseDTO.builder()
                .id(entity.getId())
                .numeroCaja(entity.getCaja() != null ? entity.getCaja().getNumeroCaja() : null)
                .numeroDesembolso(entity.getNumeroDesembolso())
                .numeroPrestamo(entity.getPrestamo() != null ? entity.getPrestamo().getNumeroPrestamo() : null)
                .numeroSolicitud(entity.getSolicitud() != null ? entity.getSolicitud().getNumeroSolicitud() : null)
                .numeroAsociado(entity.getAsociado() != null ? entity.getAsociado().getNumeroAsociado() : null)
                .nombreCompletoAsociado(construirNombreCompleto(entity.getAsociado()))
                .montoBrutoDesembolso(entity.getMontoBrutoDesembolso())
                .tasaInteresAnual(entity.getPrestamo() != null ? entity.getPrestamo().getTasaInteresAnual() : null)
                .plazoMeses(entity.getPrestamo() != null ? entity.getPrestamo().getPlazoMeses() : null)
                .frecuenciaPago(entity.getPrestamo() != null && entity.getPrestamo().getFrecuenciaPago() != null
                        ? entity.getPrestamo().getFrecuenciaPago().name() : null)
                .totalDeducciones(entity.getTotalDeducciones())
                .montoNetoEntregado(entity.getMontoNetoEntregado())
                .fechaDesembolso(entity.getFechaDesembolso())
                .formaPago(entity.getFormaPago())
                .numeroCuentaDestino(entity.getNumeroCuentaDestino())
                .numeroComprobante(entity.getNumeroComprobante())
                .observaciones(entity.getObservaciones())
                .usuarioCajero(entity.getUsuario() != null ? entity.getUsuario().getUsuario() : null)
                .deducciones(deduccionesDto)
                .build();
    }

    private String construirNombreCompleto(Asociados asociado) {
        if (asociado == null) {
            return null;
        }
        String nombres = asociado.getNombres() != null ? asociado.getNombres() : "";
        String apellidos = asociado.getApellidos() != null ? asociado.getApellidos() : "";
        return (nombres + " " + apellidos).trim();
    }

    @Override
    @Transactional(readOnly = true)
    public DesembolsoCreditoResponseDTO obtenerPorNumeroDesembolso(String numeroDesembolso) {
        DesembolsoCredito entity = desembolsoRepository.findByNumeroDesembolsoWithDetails(numeroDesembolso)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el desembolso con correlativo: " + numeroDesembolso));
        return mapToResponseDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DesembolsoCreditoResponseDTO> obtenerPorNumeroPrestamo(String numeroPrestamo) {
        return desembolsoRepository.findByPrestamoNumeroPrestamo(numeroPrestamo)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DesembolsoCreditoResponseDTO> obtenerPorNumeroAsociado(Integer numeroAsociado) {
        return desembolsoRepository.findByAsociadoNumeroAsociado(numeroAsociado)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DesembolsoCreditoResponseDTO> obtenerPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return desembolsoRepository.findByFechaDesembolsoBetween(fechaInicio, fechaFin)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
}