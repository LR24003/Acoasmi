package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.GenerarPlanPagosRequestDTO;
import com.acoasmi.roble.dto.request.PrestamosConcedidosRequestDTO;
import com.acoasmi.roble.dto.request.SimularPlanPagosRequestDTO;
import com.acoasmi.roble.dto.response.CuotaPlanPagosResponseDTO;
import com.acoasmi.roble.dto.response.PlanPagosResponseDTO;
import com.acoasmi.roble.dto.response.PrestamosConcedidosResponseDTO;
import com.acoasmi.roble.entity.Asociados;
import com.acoasmi.roble.entity.PlanPagos;
import com.acoasmi.roble.entity.PrestamosConcedidos;
import com.acoasmi.roble.entity.SolicitudesCredito;
import com.acoasmi.roble.repository.AsociadosRepository;
import com.acoasmi.roble.repository.PlanPagosRepository;
import com.acoasmi.roble.repository.PrestamosConcedidosRepository;
import com.acoasmi.roble.repository.SolicitudesCreditoRepository;
import com.acoasmi.roble.service.PrestamoConcedidoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrestamoConcedidoServiceImpl
        extends AcoasmiServiceImpl<PrestamosConcedidos, PrestamosConcedidosRequestDTO, PrestamosConcedidosResponseDTO, Long>
        implements PrestamoConcedidoService {

    private final PrestamosConcedidosRepository prestamoRepository;
    private final PlanPagosRepository planPagosRepository;
    private final AsociadosRepository asociadosRepository;
    private final SolicitudesCreditoRepository solicitudesRepository;

    public PrestamoConcedidoServiceImpl(
            PrestamosConcedidosRepository prestamoRepository,
            PlanPagosRepository planPagosRepository,
            AsociadosRepository asociadosRepository,
            SolicitudesCreditoRepository solicitudesRepository) {
        super(prestamoRepository, PrestamosConcedidos.class);
        this.prestamoRepository = prestamoRepository;
        this.planPagosRepository = planPagosRepository;
        this.asociadosRepository = asociadosRepository;
        this.solicitudesRepository = solicitudesRepository;
    }

    @Override
    protected PrestamosConcedidosResponseDTO mapToResponseDTO(PrestamosConcedidos p) {
        if (p == null) return null;

        return PrestamosConcedidosResponseDTO.builder()
                .id(p.getId())
                .numeroAsociado(p.getAsociado() != null ? (p.getAsociado().getNumeroAsociado()) : null)
                .nombreCompletoAsociado(p.getAsociado() != null
                        ? (p.getAsociado().getNombres() + " " + p.getAsociado().getApellidos()).trim()
                        : null)
                .numeroSolicitud(p.getCredito() != null ? p.getCredito().getNumeroSolicitud() : null)
                .numeroPrestamo(p.getNumeroPrestamo())
                .montoConcedido(p.getMontoConcedido())
                .saldoCapitalActual(p.getSaldoCapitalActual())
                .tasaInteresAnual(p.getTasaInteresAnual())
                .plazoMeses(p.getPlazoMeses())
                .frecuenciaPago(p.getFrecuenciaPago() != null ? p.getFrecuenciaPago().name() : null)
                .estadoPrestamo(p.getEstadoPrestamo())
                .fechaDesembolso(p.getFechaDesembolso())
                .tasaMoraAnual(p.getTasaMoraAnual())
                .saldoInteresPendiente(p.getSaldoInteresPendiente())
                .saldoMoraAcumulada(p.getSaldoMoraAcumulada())
                .montoSeguroDeuda(p.getMontoSeguroDeuda())
                .montoAportacion(p.getMontoAportacion())
                .montoAhorroSimultaneo(p.getMontoAhorroSimultaneo())
                .montoCuotaGestion(p.getMontoCuotaGestion())
                .fechaProximoPago(p.getFechaProximoPago())
                .fechaUltimoPago(p.getFechaUltimoPago())
                .diasAtraso(p.getDiasAtraso())
                .build();
    }

    @Override
    protected void mapearDtoAEntidad(PrestamosConcedidosRequestDTO request, PrestamosConcedidos prestamo) {

        Asociados asociado = asociadosRepository.findByNumeroAsociado(request.getNumeroAsociado())
                .orElseThrow(() -> new EntityNotFoundException("Asociado no encontrado: " + request.getNumeroAsociado()));

        SolicitudesCredito solicitud = solicitudesRepository.findByNumeroSolicitud(request.getNumeroSolicitud())
                .orElseThrow(() -> new EntityNotFoundException("Solicitud de crédito no encontrada: " + request.getNumeroSolicitud()));

        prestamo.setAsociado(asociado);
        prestamo.setCredito(solicitud);
        prestamo.setNumeroPrestamo(request.getNumeroPrestamo());
        prestamo.setMontoConcedido(request.getMontoConcedido());

        if (prestamo.getSaldoCapitalActual() == null) {
            prestamo.setSaldoCapitalActual(request.getMontoConcedido());
        }

        prestamo.setTasaInteresAnual(request.getTasaInteresAnual());
        prestamo.setPlazoMeses(request.getPlazoMeses());
        prestamo.setFrecuenciaPago(request.getFrecuenciaPago());

        if (prestamo.getEstadoPrestamo() == null) {
            prestamo.setEstadoPrestamo(PrestamosConcedidos.EstadoPrestamo.AL_DIA);
        }

        if (prestamo.getFechaDesembolso() == null) {
            prestamo.setFechaDesembolso(LocalDateTime.now());
        }

        prestamo.setTasaMoraAnual(request.getTasaMoraAnual());

        if (prestamo.getSaldoInteresPendiente() == null) prestamo.setSaldoInteresPendiente(BigDecimal.ZERO);
        if (prestamo.getSaldoMoraAcumulada() == null) prestamo.setSaldoMoraAcumulada(BigDecimal.ZERO);

        prestamo.setMontoSeguroDeuda(request.getMontoSeguroDeuda());
        prestamo.setMontoAportacion(request.getMontoAportacion());
        prestamo.setMontoAhorroSimultaneo(request.getMontoAhorroSimultaneo());
        prestamo.setMontoCuotaGestion(request.getMontoCuotaGestion());

        if (prestamo.getFechaProximoPago() == null) {
            prestamo.setFechaProximoPago(calcularFechaPrimerPago(LocalDate.now(), request.getFrecuenciaPago()));
        }

        if (prestamo.getDiasAtraso() == null) {
            prestamo.setDiasAtraso(0);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PrestamosConcedidosResponseDTO obtenerPorNumeroPrestamo(String numeroPrestamo) {
        PrestamosConcedidos prestamo = prestamoRepository.findByNumeroPrestamoConRelaciones(numeroPrestamo)
                .orElseThrow(() -> new EntityNotFoundException("Préstamo no encontrado: " + numeroPrestamo));
        return mapToResponseDTO(prestamo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamosConcedidosResponseDTO> obtenerPorNumeroAsociado(String numeroAsociado) {
        return prestamoRepository.findByNumeroAsociado(numeroAsociado)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PlanPagosResponseDTO simularPlanPagos(SimularPlanPagosRequestDTO request) {
        PrestamosConcedidos temp = new PrestamosConcedidos();
        temp.setNumeroPrestamo(null);
        temp.setMontoConcedido(request.getMontoConcedido());
        temp.setTasaInteresAnual(request.getTasaInteresAnual());
        temp.setPlazoMeses(request.getPlazoMeses());
        temp.setFrecuenciaPago(request.getFrecuenciaPago());
        temp.setMontoSeguroDeuda(request.getMontoSeguroDeuda());
        temp.setMontoAportacion(request.getMontoAportacion());
        temp.setMontoAhorroSimultaneo(request.getMontoAhorroSimultaneo());
        temp.setMontoCuotaGestion(request.getMontoCuotaGestion());

        if (request.getNumeroAsociado() != null) {
            Asociados asociado = asociadosRepository.findByNumeroAsociado(request.getNumeroAsociado())
                    .orElseThrow(() -> new EntityNotFoundException("Asociado no encontrado con el número: " + request.getNumeroAsociado()));
            temp.setAsociado(asociado);
        }

        List<PlanPagos> cuotasSimuladas = calcularEntidadesPlanPagos(temp, LocalDate.now());
        return construirPlanPagosResponseDTO(temp, cuotasSimuladas);
    }

    @Override
    @Transactional
    public PlanPagosResponseDTO generarYGuardarPlanPagos(GenerarPlanPagosRequestDTO request) {
        PrestamosConcedidos prestamo = prestamoRepository.findByNumeroPrestamo(request.getNumeroPrestamo())
                .orElseThrow(() -> new EntityNotFoundException("Préstamo no encontrado: " + request.getNumeroPrestamo()));

        planPagosRepository.deleteByPrestamoNumeroPrestamo(prestamo.getNumeroPrestamo());

        LocalDate fechaInicio = prestamo.getFechaDesembolso() != null
                ? prestamo.getFechaDesembolso().toLocalDate()
                : LocalDate.now();

        List<PlanPagos> cuotasEntidad = calcularEntidadesPlanPagos(prestamo, fechaInicio);
        planPagosRepository.saveAll(cuotasEntidad);

        return obtenerPlanPagosPorNumeroPrestamo(prestamo.getNumeroPrestamo());
    }

    @Override
    @Transactional(readOnly = true)
    public PlanPagosResponseDTO obtenerPlanPagosPorNumeroPrestamo(String numeroPrestamo) {
        PrestamosConcedidos prestamo = prestamoRepository.findByNumeroPrestamo(numeroPrestamo)
                .orElseThrow(() -> new EntityNotFoundException("Préstamo no encontrado: " + numeroPrestamo));

        List<PlanPagos> cuotas = planPagosRepository.findByNumeroPrestamoOrderByNumeroCuotaAsc(numeroPrestamo);
        return construirPlanPagosResponseDTO(prestamo, cuotas);
    }


    private List<PlanPagos> calcularEntidadesPlanPagos(PrestamosConcedidos prestamo, LocalDate fechaBase) {
        List<PlanPagos> cuotas = new ArrayList<>();
        int pagosPorAno = obtenerPagosPorAno(prestamo.getFrecuenciaPago());
        int totalCuotas = calcularTotalCuotas(prestamo.getPlazoMeses(), prestamo.getFrecuenciaPago());

        double tasaPeriodica = prestamo.getTasaInteresAnual().doubleValue() / 100.0 / pagosPorAno;
        double montoDouble = prestamo.getMontoConcedido().doubleValue();

        double cuotaBaseDouble;
        if (tasaPeriodica > 0) {
            cuotaBaseDouble = montoDouble * (tasaPeriodica / (1 - Math.pow(1 + tasaPeriodica, -totalCuotas)));
        } else {
            cuotaBaseDouble = montoDouble / totalCuotas;
        }

        BigDecimal cuotaBase = BigDecimal.valueOf(cuotaBaseDouble).setScale(2, RoundingMode.HALF_UP);
        BigDecimal saldoRestante = prestamo.getMontoConcedido();
        LocalDate fechaVencimiento = fechaBase;

        BigDecimal montoPrestamo = prestamo.getMontoConcedido() != null ? prestamo.getMontoConcedido() : BigDecimal.ZERO;
        BigDecimal numCuotasBD = new BigDecimal(totalCuotas);

        int mesesPeriodo = obtenerMesesPorPeriodo(prestamo.getFrecuenciaPago());

        BigDecimal seguro;
        if (prestamo.getMontoSeguroDeuda() != null && prestamo.getMontoSeguroDeuda().compareTo(BigDecimal.ZERO) > 0) {
            seguro = prestamo.getMontoSeguroDeuda();
        } else {
            BigDecimal seguroTotal = montoPrestamo.multiply(new BigDecimal("0.01"));
            seguro = seguroTotal.divide(numCuotasBD, 2, RoundingMode.HALF_UP);
        }

        BigDecimal aportacion;
        if (prestamo.getMontoAportacion() != null && prestamo.getMontoAportacion().compareTo(BigDecimal.ZERO) > 0) {
            aportacion = prestamo.getMontoAportacion();
        } else {
            BigDecimal aportacionBaseMensual = new BigDecimal("5.00");
            aportacion = aportacionBaseMensual.multiply(new BigDecimal(mesesPeriodo));
        }

        BigDecimal ahorro;
        if (prestamo.getMontoAhorroSimultaneo() != null && prestamo.getMontoAhorroSimultaneo().compareTo(BigDecimal.ZERO) > 0) {
            ahorro = prestamo.getMontoAhorroSimultaneo();
        } else {
            BigDecimal ahorroTotal = montoPrestamo.multiply(new BigDecimal("0.03"));
            ahorro = ahorroTotal.divide(numCuotasBD, 2, RoundingMode.HALF_UP);
        }
        
        BigDecimal gestion = prestamo.getMontoCuotaGestion() != null ? prestamo.getMontoCuotaGestion() : BigDecimal.ZERO;

        for (int i = 1; i <= totalCuotas; i++) {
            fechaVencimiento = calcularFechaPrimerPago(fechaVencimiento, prestamo.getFrecuenciaPago());

            assert saldoRestante != null;
            BigDecimal abonoInteres = saldoRestante.multiply(BigDecimal.valueOf(tasaPeriodica)).setScale(2, RoundingMode.HALF_UP);
            BigDecimal abonoCapital = cuotaBase.subtract(abonoInteres);

            if (i == totalCuotas || abonoCapital.compareTo(saldoRestante) > 0) {
                abonoCapital = saldoRestante;
                cuotaBase = abonoCapital.add(abonoInteres);
                saldoRestante = BigDecimal.ZERO;
            } else {
                saldoRestante = saldoRestante.subtract(abonoCapital);
            }

            BigDecimal cargosAdicionales = seguro.add(aportacion).add(ahorro).add(gestion);
            BigDecimal totalCuota = cuotaBase.add(cargosAdicionales);

            PlanPagos p = new PlanPagos();
            p.setPrestamo(prestamo);
            p.setNumeroCuota(i);
            p.setFechaVencimientoProyectada(fechaVencimiento);
            p.setAbonoCapital(abonoCapital);
            p.setAbonoInteres(abonoInteres);
            p.setMontoCuotaBase(cuotaBase);
            p.setSaldoCapitalRestante(saldoRestante);
            p.setSeguroProgramado(seguro);
            p.setAportacionProgramada(aportacion);
            p.setAhorroProgramado(ahorro);
            p.setCuotaGestionProgramada(gestion);
            p.setTotalCuota(totalCuota);
            p.setSaldoCuotaPendiente(totalCuota);
            p.setEstadoCuota(PlanPagos.EstadoCuota.PENDIENTE);

            cuotas.add(p);
        }

        return cuotas;
    }

    private int obtenerMesesPorPeriodo(PrestamosConcedidos.FrecuenciaPago frecuencia) {
        if (frecuencia == null) return 1;

        return switch (frecuencia) {
            case MENSUAL       -> 1;
            case BIMENSUAL     -> 2;
            case TRIMESTRAL    -> 3;
            case CUATRIMESTRAL -> 4;
            case SEMESTRAL     -> 6;
            case ANUAL         -> 12;
        };
    }

    private int obtenerPagosPorAno(PrestamosConcedidos.FrecuenciaPago frecuencia) {
        if (frecuencia == null) return 12;

        return switch (frecuencia) {
            case MENSUAL       -> 12;
            case BIMENSUAL     -> 6;
            case TRIMESTRAL    -> 4;
            case CUATRIMESTRAL -> 3;
            case SEMESTRAL     -> 2;
            case ANUAL         -> 1;
        };
    }

    private int calcularTotalCuotas(Integer plazoMeses, PrestamosConcedidos.FrecuenciaPago frecuencia) {
        if (plazoMeses == null || plazoMeses <= 0) return 0;
        if (frecuencia == null) return plazoMeses;

        return switch (frecuencia) {
            case MENSUAL       -> plazoMeses;
            case BIMENSUAL     -> plazoMeses / 2;
            case TRIMESTRAL    -> plazoMeses / 3;
            case CUATRIMESTRAL -> plazoMeses / 4;
            case SEMESTRAL     -> plazoMeses / 6;
            case ANUAL         -> plazoMeses / 12;
        };
    }

    private LocalDate calcularFechaPrimerPago(LocalDate fechaActual, PrestamosConcedidos.FrecuenciaPago frecuencia) {
        if (frecuencia == null) return fechaActual.plusMonths(1);

        return switch (frecuencia) {
            case MENSUAL       -> fechaActual.plusMonths(1);
            case BIMENSUAL     -> fechaActual.plusMonths(2);
            case TRIMESTRAL    -> fechaActual.plusMonths(3);
            case CUATRIMESTRAL -> fechaActual.plusMonths(4);
            case SEMESTRAL     -> fechaActual.plusMonths(6);
            case ANUAL         -> fechaActual.plusYears(1);
        };
    }

    private PlanPagosResponseDTO construirPlanPagosResponseDTO(PrestamosConcedidos prestamo, List<PlanPagos> cuotas) {
        BigDecimal totalIntereses = cuotas.stream()
                .map(PlanPagos::getAbonoInteres)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalGeneral = cuotas.stream()
                .map(PlanPagos::getTotalCuota)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<CuotaPlanPagosResponseDTO> listaCuotasDto = cuotas.stream()
                .map(this::mapToCuotaResponseDTO)
                .toList();

        Integer numeroAsociado = (prestamo.getAsociado() != null)
                ? prestamo.getAsociado().getNumeroAsociado()
                : null;

        String nombreAsociado = (prestamo.getAsociado() != null)
                ? (prestamo.getAsociado().getNombres() + " " + prestamo.getAsociado().getApellidos()).trim()
                : null;

        return PlanPagosResponseDTO.builder()
                .numeroAsociado(numeroAsociado)
                .nombreCompletoAsociado(nombreAsociado)
                .numeroPrestamo(prestamo.getNumeroPrestamo())
                .montoFinanciado(prestamo.getMontoConcedido())
                .tasaInteresAnual(prestamo.getTasaInteresAnual())
                .totalCuotas(cuotas.size())
                .totalInteresesProyectados(totalIntereses)
                .totalGeneralProyectado(totalGeneral)
                .cuotas(listaCuotasDto)
                .build();
    }

    private CuotaPlanPagosResponseDTO mapToCuotaResponseDTO(PlanPagos p) {
        if (p == null) return null;

        return CuotaPlanPagosResponseDTO.builder()
                .numeroCuota(p.getNumeroCuota())
                .fechaVencimientoProyectada(p.getFechaVencimientoProyectada())
                .montoCuotaBase(p.getMontoCuotaBase())
                .abonoCapital(p.getAbonoCapital())
                .abonoInteres(p.getAbonoInteres())
                .seguroProgramado(p.getSeguroProgramado())
                .aportacionProgramada(p.getAportacionProgramada())
                .ahorroProgramado(p.getAhorroProgramado())
                .cuotaGestionProgramada(p.getCuotaGestionProgramada())
                .totalCuota(p.getTotalCuota())
                .saldoCapitalRestante(p.getSaldoCapitalRestante())
                .estadoCuota(p.getEstadoCuota() != null ? p.getEstadoCuota().name() : null)
                .build();
    }
}