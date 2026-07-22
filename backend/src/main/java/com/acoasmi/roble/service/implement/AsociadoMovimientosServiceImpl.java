package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.AsociadoMovimientosRequestDTO;
import com.acoasmi.roble.dto.response.AsociadoMovimientosResponseDTO;
import com.acoasmi.roble.dto.response.ComprobanteMovimientoDTO;
import com.acoasmi.roble.dto.response.DetalleMovimientoEstadoCuentaDTO;
import com.acoasmi.roble.dto.response.EstadoCuentaResponseDTO;
import com.acoasmi.roble.entity.*;
import com.acoasmi.roble.repository.*;
import com.acoasmi.roble.repository.projection.DetalleEstadoCuentaProjection;
import com.acoasmi.roble.service.AsociadoMovimientosService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AsociadoMovimientosServiceImpl extends AcoasmiServiceImpl<AsociadoMovimientos,
        AsociadoMovimientosRequestDTO, AsociadoMovimientosResponseDTO, Long>
        implements AsociadoMovimientosService {

    private final AsociadoMovimientosRepository movimientosRepository;
    private final AsociadoCuentasRepository cuentaRepository;
    private final UsuariosRepository usuariosRepository;
    private final AsociadosRepository asociadoRepository;
    private final ControlCajasRepository controlCajasRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public AsociadoMovimientosServiceImpl(AsociadoMovimientosRepository movimientosRepository,
                                          AsociadoCuentasRepository cuentaRepository,
                                          UsuariosRepository usuariosRepository,
                                          AsociadosRepository asociadoRepository,
                                          ControlCajasRepository controlCajasRepository) { // <-- Inyectado en constructor
        super(movimientosRepository, AsociadoMovimientos.class);
        this.movimientosRepository = movimientosRepository;
        this.cuentaRepository = cuentaRepository;
        this.usuariosRepository = usuariosRepository;
        this.asociadoRepository = asociadoRepository;
        this.controlCajasRepository = controlCajasRepository;
    }

    @Override
    @Transactional
    public AsociadoMovimientosResponseDTO create(AsociadoMovimientosRequestDTO requestDto) {
        AsociadoMovimientos movimientoGuardado = guardarMovimientoInterno(requestDto);
        return mapToResponseDTO(movimientoGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComprobanteMovimientoDTO> obtenerComprobantesAperturaPorAsociado(Integer numeroAsociado) {
        List<AsociadoCuentas> cuentas = cuentaRepository.findByAsociadoNumeroAsociado(numeroAsociado);

        return cuentas.stream()
                .map(cuenta -> movimientosRepository.findCuentaHistorialPorNumeroCuenta(cuenta.getNumeroCuenta()))
                .filter(moves -> !moves.isEmpty())
                .map(List::getLast)
                .map(this::construirComprobanteDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ComprobanteMovimientoDTO obtenerComprobantePorId(Long idMovimiento) {
        AsociadoMovimientos movimiento = movimientosRepository.findMovimientoConDetallesParaComprobante(idMovimiento)
                .orElseThrow(() -> new RuntimeException("No se encontró el movimiento con ID: " + idMovimiento));

        return construirComprobanteDTO(movimiento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsociadoMovimientosResponseDTO> obtenerHistorialPorNumeroCuenta(String numeroCuenta) {
        if (numeroCuenta == null || numeroCuenta.isBlank()) {
            throw new IllegalArgumentException("El número de cuenta no puede estar vacío.");
        }
        return movimientosRepository.findCuentaHistorialPorNumeroCuenta(numeroCuenta)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AsociadoMovimientosResponseDTO> obtenerHistorialPorNumeroCuentaPaginado(String numeroCuenta, Pageable pageable) {
        if (numeroCuenta == null || numeroCuenta.isBlank()) {
            throw new IllegalArgumentException("El número de cuenta no puede estar vacío.");
        }
        return movimientosRepository.findCuentaHistorialPorNumeroCuentaPaginado(numeroCuenta, pageable)
                .map(this::mapToResponseDTO);
    }

    private AsociadoMovimientos guardarMovimientoInterno(AsociadoMovimientosRequestDTO requestDto) {

        AsociadoCuentas cuenta = cuentaRepository.findByNumeroCuenta(requestDto.getNumeroCuenta())
                .orElseThrow(() -> new RuntimeException("La cuenta número '" + requestDto.getNumeroCuenta() + "' no existe."));

        if (cuenta.getEstado() == null || !cuenta.getEstado()) {
            throw new RuntimeException("La cuenta seleccionada se encuentra inactiva.");
        }

        AsociadoMovimientos movimiento = new AsociadoMovimientos();


        mapearDtoAEntidad(requestDto, movimiento);
        movimiento.setCuenta(cuenta);

        if (movimiento.getUsuario() == null) {
            throw new RuntimeException("Es necesario especificar un usuario cajero válido.");
        }

        ControlCajas sesionCajaActiva = controlCajasRepository
                .findByUsuarioCajeroIdAndFechaCierreIsNullAndEstadoTrue(movimiento.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException(
                        "El cajero '" + movimiento.getUsuario().getUsuario() + "' no tiene una caja abierta."
                ));

        movimiento.setCaja(sesionCajaActiva);

        BigDecimal saldoActual = cuenta.getSaldoActual() != null ? cuenta.getSaldoActual() : BigDecimal.ZERO;
        BigDecimal montoMovimiento = movimiento.getMonto() != null ? movimiento.getMonto() : BigDecimal.ZERO;
        BigDecimal nuevoSaldo = saldoActual;

        String tipoMov = movimiento.getTipoMovimiento() != null ? movimiento.getTipoMovimiento().toUpperCase().trim() : "";

        switch (tipoMov) {
            case "DEPOSITO":
            case "DESEMBOLSO_PRESTAMO":
            case "DEPOSITO_DESEMBOLSO":
            case "INTERES":
            case "INTERESES":
            case "CAPITALIZACION_INTERES":
                nuevoSaldo = saldoActual.add(montoMovimiento);
                break;

            case "RETIRO":
                if (saldoActual.compareTo(montoMovimiento) < 0) {
                    throw new IllegalArgumentException("Saldo insuficiente en la cuenta. Saldo disponible: $" + saldoActual);
                }
                nuevoSaldo = saldoActual.subtract(montoMovimiento);
                break;

            case "PAGO_PRESTAMO":
                break;

            default:
                throw new IllegalArgumentException("Tipo de movimiento no soportado: " + tipoMov);
        }

        cuenta.setSaldoActual(nuevoSaldo);
        cuentaRepository.save(cuenta);
        movimiento.setSaldoResultante(nuevoSaldo);

        AsociadoMovimientos movimientoGuardado = movimientosRepository.saveAndFlush(movimiento);

        Integer numeroDesembolsoCalculado = null;

        if ("DESEMBOLSO_PRESTAMO".equals(tipoMov) || "DEPOSITO_DESEMBOLSO".equals(tipoMov)) {
            if (movimientoGuardado.getIdPrestamo() != null) {
                numeroDesembolsoCalculado = movimientosRepository.countDesembolsosByPrestamoId(movimientoGuardado.getIdPrestamo());
            }
        }

        String numeroCajaAuto = sesionCajaActiva.getNumeroCaja();
        String comprobanteGenerado = generarNumeroComprobante(
                movimientoGuardado.getTipoMovimiento(),
                numeroCajaAuto,
                movimientoGuardado.getId(),
                movimientoGuardado.getFechaMovimiento(),
                movimientoGuardado.getIdPrestamo(),
                numeroDesembolsoCalculado
        );

        movimientoGuardado.setNumeroComprobante(comprobanteGenerado);
        movimientoGuardado = movimientosRepository.saveAndFlush(movimientoGuardado);

        entityManager.refresh(movimientoGuardado);
        entityManager.refresh(movimientoGuardado.getCuenta());

        return movimientoGuardado;
    }


    @Override
    @Transactional(readOnly = true)
    public EstadoCuentaResponseDTO obtenerEstadoCuenta(String numeroAsociado, String fechaInicio, String fechaFin) {

        Integer numAsociadoInt;
        try {
            numAsociadoInt = Integer.parseInt(numeroAsociado);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El número de asociado debe ser un valor numérico válido.");
        }

        Asociados asociado = asociadoRepository.findByNumeroAsociado(numAsociadoInt)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el asociado con número: " + numeroAsociado));

        List<AsociadoCuentas> cuentas = cuentaRepository.findByAsociadoNumeroAsociado(numAsociadoInt);
        AsociadoCuentas cuenta = cuentas.isEmpty() ? null : cuentas.getFirst();

        String nombreCompleto = ((asociado.getNombres() != null ? asociado.getNombres() : "") + " " +
                (asociado.getApellidos() != null ? asociado.getApellidos() : "")).trim();

        BigDecimal saldoInicial = BigDecimal.ZERO;

        if (cuenta != null) {
            List<BigDecimal> saldosAnteriores = movimientosRepository
                    .obtenerSaldoAnteriorAFecha(cuenta.getNumeroCuenta(), fechaInicio);

            if (!saldosAnteriores.isEmpty()) {
                saldoInicial = saldosAnteriores.getFirst();
            } else {
                saldoInicial = cuenta.getMontoApertura() != null ? cuenta.getMontoApertura() : BigDecimal.ZERO;
            }
        }

        List<DetalleEstadoCuentaProjection> proyecciones =
                movimientosRepository.obtenerDetalleEstadoCuentaPorAsociado(numeroAsociado, fechaInicio, fechaFin);

        List<DetalleMovimientoEstadoCuentaDTO> movimientosDTO = proyecciones.stream()
                .map(p -> DetalleMovimientoEstadoCuentaDTO.builder()
                        .numeroComprobante(p.getNumeroComprobante() != null ? p.getNumeroComprobante() : "")
                        .fechaAplicacion(p.getFechaAplicacion())
                        .deposito(p.getDeposito())
                        .retiro(p.getRetiro())
                        .intereses(p.getIntereses())
                        .cambioTasa(p.getCambioTasa())
                        .saldo(p.getSaldo())
                        .build())
                .collect(Collectors.toList());

        BigDecimal totalDepositos = movimientosDTO.stream()
                .map(m -> Objects.requireNonNullElse(m.getDeposito(), BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalRetiros = movimientosDTO.stream()
                .map(m -> Objects.requireNonNullElse(m.getRetiro(), BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalIntereses = movimientosDTO.stream()
                .map(m -> Objects.requireNonNullElse(m.getIntereses(), BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return EstadoCuentaResponseDTO.builder()
                .numeroAsociado(numeroAsociado)
                .nombreAsociado(nombreCompleto)
                .direccion(asociado.getDireccionComplementaria())
                .numeroCuenta(cuenta != null ? cuenta.getNumeroCuenta() : null)
                .tipoCuenta(cuenta != null ? cuenta.getTipoCuenta() : null)
                .tipoAhorro(cuenta != null ? cuenta.getTipoAhorro() : null)
                .fechaUltimaCapitalizacion(cuenta != null ? cuenta.getFechaUltimaCapitalizacion() : null)
                .montoApertura(cuenta != null ? cuenta.getMontoApertura() : null)
                .tasaInteres(cuenta != null ? cuenta.getTasaInteresAnual() : null)
                .plazoDias(cuenta != null ? parsePlazoDias(cuenta.getPlazoDias()) : null)
                .saldoInicial(saldoInicial)
                .fechaInicio(fechaInicio)
                .fechaFin(fechaFin)
                .totalDepositos(totalDepositos)
                .totalRetiros(totalRetiros)
                .totalIntereses(totalIntereses)
                .movimientos(movimientosDTO)
                .build();
    }

    private Integer parsePlazoDias(String plazoDiasStr) {
        if (plazoDiasStr == null || plazoDiasStr.isBlank()) {
            return null;
        }
        String soloNumeros = plazoDiasStr.replaceAll("\\D+", "");
        if (soloNumeros.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(soloNumeros);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    protected void mapearDtoAEntidad(AsociadoMovimientosRequestDTO requestDto, AsociadoMovimientos entity) {
        String tipoMov = requestDto.getTipoMovimiento().toUpperCase().trim();

        if (requestDto.getUsuarioCajero() != null && !requestDto.getUsuarioCajero().isBlank()) {
            Usuarios usuario = usuariosRepository.findByUsuarioIgnoreCaseAndEstadoTrue(requestDto.getUsuarioCajero())
                    .orElseThrow(() -> new RuntimeException("El usuario '" + requestDto.getUsuarioCajero() + "' no existe o está inactivo."));
            entity.setUsuario(usuario);
        }

        entity.setTipoMovimiento(tipoMov);
        entity.setMonto(requestDto.getMonto());
        entity.setDescripcionMovimiento(requestDto.getDescripcionMovimiento() != null ? requestDto.getDescripcionMovimiento().trim() : "");
        entity.setSaldoResultante(BigDecimal.ZERO);

        if ("PAGO_PRESTAMO".equals(tipoMov)) {
            if (requestDto.getIdPrestamo() == null) {
                throw new IllegalArgumentException("El campo 'idPrestamo' es obligatorio cuando el tipo de movimiento es PAGO_PRESTAMO.");
            }
            if (requestDto.getIdFacturaReferencia() == null) {
                throw new IllegalArgumentException("El campo 'idFacturaReferencia' es obligatorio para generar la factura del PAGO_PRESTAMO.");
            }

            entity.setIdPrestamo(requestDto.getIdPrestamo());
            entity.setIdFacturaReferencia(requestDto.getIdFacturaReferencia());
        } else {
            entity.setIdPrestamo(null);
            entity.setIdFacturaReferencia(null);
        }
    }

    @Override
    protected AsociadoMovimientosResponseDTO mapToResponseDTO(AsociadoMovimientos entidad) {
        String nombreCompleto = "N/A";
        String numeroCuenta = "N/A";
        String tipoCuenta = "N/A";

        if (entidad.getCuenta() != null) {
            numeroCuenta = entidad.getCuenta().getNumeroCuenta();
            tipoCuenta = entidad.getCuenta().getTipoCuenta();

            if (entidad.getCuenta().getAsociado() != null) {
                nombreCompleto = (entidad.getCuenta().getAsociado().getNombres() + " " +
                        entidad.getCuenta().getAsociado().getApellidos()).trim();
            }
        }

        String usuarioCajero = (entidad.getUsuario() != null) ? entidad.getUsuario().getUsuario() : "N/A";

        return new AsociadoMovimientosResponseDTO(
                entidad.getCaja().getNumeroCaja(),
                usuarioCajero,
                entidad.getId(),
                entidad.getNumeroComprobante(),
                nombreCompleto,
                numeroCuenta,
                tipoCuenta,
                entidad.getTipoMovimiento(),
                entidad.getMonto(),
                entidad.getSaldoResultante(),
                entidad.getFechaMovimiento(),
                entidad.getDescripcionMovimiento(),
                entidad.getCambioTasa()
        );
    }

    private ComprobanteMovimientoDTO construirComprobanteDTO(AsociadoMovimientos movimiento) {
        Integer numeroAsociado = null;
        String nombreCompleto = "N/A";

        if (movimiento.getCuenta() != null && movimiento.getCuenta().getAsociado() != null) {
            numeroAsociado = movimiento.getCuenta().getAsociado().getNumeroAsociado();
            nombreCompleto = (movimiento.getCuenta().getAsociado().getNombres() + " " +
                    movimiento.getCuenta().getAsociado().getApellidos()).trim();
        }

        String usuarioCajero = (movimiento.getUsuario() != null) ? movimiento.getUsuario().getUsuario() : "N/A";

        return ComprobanteMovimientoDTO.builder()
                .numeroComprobante(movimiento.getNumeroComprobante())
                .tipoMovimiento(movimiento.getTipoMovimiento())
                .numeroAsociado(numeroAsociado)
                .nombreCompletoAsociado(nombreCompleto)
                .monto(movimiento.getMonto())
                .usuarioCajero(usuarioCajero)
                .fechaMovimiento(movimiento.getFechaMovimiento())
                .idPrestamo(movimiento.getIdPrestamo())
                .idFacturaReferencia(movimiento.getIdFacturaReferencia())
                .build();
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
}