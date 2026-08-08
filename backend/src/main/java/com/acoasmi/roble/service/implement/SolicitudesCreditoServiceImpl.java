package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.CreditoDetallesRequestDTO;
import com.acoasmi.roble.dto.request.CreditoGarantiasRequestDTO;
import com.acoasmi.roble.dto.request.SolicitudesCreditoRequestDTO;
import com.acoasmi.roble.dto.response.*;
import com.acoasmi.roble.entity.*;
import com.acoasmi.roble.enums.EstadoSolicitudCredito;
import com.acoasmi.roble.repository.*;
import com.acoasmi.roble.service.SolicitudesCreditoService;
import jakarta.persistence.EntityNotFoundException;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitudesCreditoServiceImpl
        extends AcoasmiServiceImpl<SolicitudesCredito, SolicitudesCreditoRequestDTO, SolicitudesCreditoResponseDTO, Long>
        implements SolicitudesCreditoService {

    private final SolicitudesCreditoRepository solicitudesCreditoRepository;
    private final UsuariosRepository usuariosRepository;
    private final TasaCreditosRepository tasaCreditosRepository;
    private final AsociadosRepository asociadosRepository;

    public SolicitudesCreditoServiceImpl(SolicitudesCreditoRepository solicitudesCreditoRepository,
                                         UsuariosRepository usuariosRepository,
                                         AsociadosRepository asociadosRepository,
                                         TasaCreditosRepository tasaCreditosRepository) {
        super(solicitudesCreditoRepository, SolicitudesCredito.class);
        this.solicitudesCreditoRepository = solicitudesCreditoRepository;
        this.usuariosRepository = usuariosRepository;
        this.tasaCreditosRepository = tasaCreditosRepository;
        this.asociadosRepository = asociadosRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public SolicitudesCreditoResponseDTO obtenerPorNumeroSolicitud(String numeroSolicitud) {
        if (numeroSolicitud == null || numeroSolicitud.isBlank()) {
            throw new IllegalArgumentException("El número de solicitud no puede estar vacío.");
        }

        SolicitudesCredito solicitud = solicitudesCreditoRepository
                .findByNumeroSolicitudWithDetailsAndGarantias(numeroSolicitud)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró ninguna solicitud con el número: " + numeroSolicitud));

        solicitudesCreditoRepository.findByNumeroSolicitudWithReferenciasAndDocumentos(numeroSolicitud);

        return mapToResponseDTO(solicitud);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SolicitudesCreditoResponseDTO> listarPorEstadoActualSolicitud(EstadoSolicitudCredito estadoActual, Pageable pageable) {
        return solicitudesCreditoRepository.findByEstadoActual(estadoActual, pageable)
                .map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudesCreditoResponseDTO> listarPorAsesor(String usuarioAsesor) {
        return solicitudesCreditoRepository.findByUsuarioAsesor_Usuario(usuarioAsesor)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SolicitudesCreditoResponseDTO create(SolicitudesCreditoRequestDTO requestDto) {
        if (requestDto.getNumeroSolicitud() != null && !requestDto.getNumeroSolicitud().isBlank() &&
                solicitudesCreditoRepository.findByNumeroSolicitudWithDetailsAndGarantias(requestDto.getNumeroSolicitud()).isPresent()) {
            throw new IllegalArgumentException("La solicitud con el número '" + requestDto.getNumeroSolicitud() + "' ya existe.");
        }

        SolicitudesCredito solicitud = new SolicitudesCredito();
        mapearDtoAEntidad(requestDto, solicitud);

        solicitud.setEstadoActual(EstadoSolicitudCredito.EN_ANALISIS_ASESOR);

        SolicitudesCredito solicitudGuardada = solicitudesCreditoRepository.save(solicitud);
        solicitudesCreditoRepository.flush();

        return mapToResponseDTO(solicitudGuardada);
    }

    @Transactional
    @Override
    public SolicitudesCreditoResponseDTO avanzarEstadoAprobacion(Long idSolicitud, String usernameResponsable, String observaciones) {
        SolicitudesCredito solicitud = solicitudesCreditoRepository.findById(idSolicitud)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud de crédito no encontrada con ID: " + idSolicitud));

        EstadoSolicitudCredito estadoActual = getEstadoSolicitudCredito(solicitud);

        EstadoSolicitudCredito siguienteEstado = EvaluadorFlujoCredito.determinarSiguienteEstadoAprobacion(
                estadoActual,
                solicitud.getMontoSolicitado()
        );

        Usuarios usuarioResponsable = usuariosRepository.findByUsuarioIgnoreCaseAndEstadoTrue(usernameResponsable)
                .orElseThrow(() -> new EntityNotFoundException("Usuario responsable no encontrado o inactivo: " + usernameResponsable));

        registrarHistorialAprobacion(solicitud, estadoActual, siguienteEstado, usuarioResponsable, observaciones);

        solicitud.setEstadoActual(siguienteEstado);

        return mapToResponseDTO(solicitudesCreditoRepository.save(solicitud));
    }

    private static EstadoSolicitudCredito getEstadoSolicitudCredito(SolicitudesCredito solicitud) {
        EstadoSolicitudCredito estadoActual = solicitud.getEstadoActual();

        if (estadoActual == EstadoSolicitudCredito.APROBADA ||
                estadoActual == EstadoSolicitudCredito.DENEGADA ||
                estadoActual == EstadoSolicitudCredito.DESEMBOLSADA ||
                estadoActual == EstadoSolicitudCredito.EN_APELACION ||
                estadoActual == EstadoSolicitudCredito.OBSERVADA) {
            throw new IllegalStateException("La solicitud en estado " + estadoActual + " no puede continuar el flujo de aprobación.");
        }
        return estadoActual;
    }

    @Transactional
    @Override
    public SolicitudesCreditoResponseDTO denegarSolicitud(Long idSolicitud, String usernameResponsable, String motivoRechazo) {
        SolicitudesCredito solicitud = solicitudesCreditoRepository.findById(idSolicitud)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud de crédito no encontrada con ID: " + idSolicitud));

        EstadoSolicitudCredito estadoAnterior = solicitud.getEstadoActual();

        if (estadoAnterior == EstadoSolicitudCredito.DENEGADA || estadoAnterior == EstadoSolicitudCredito.DESEMBOLSADA) {
            throw new IllegalStateException("No se puede rechazar una solicitud que ya está en estado " + estadoAnterior);
        }

        Usuarios usuarioResponsable = usuariosRepository.findByUsuarioIgnoreCaseAndEstadoTrue(usernameResponsable)
                .orElseThrow(() -> new EntityNotFoundException("Usuario responsable no encontrado o inactivo: " + usernameResponsable));

        registrarHistorialAprobacion(solicitud, estadoAnterior, EstadoSolicitudCredito.DENEGADA, usuarioResponsable, motivoRechazo);

        solicitud.setEstadoActual(EstadoSolicitudCredito.DENEGADA);

        return mapToResponseDTO(solicitudesCreditoRepository.save(solicitud));
    }

    private void registrarHistorialAprobacion(SolicitudesCredito solicitud,
                                              EstadoSolicitudCredito estadoAnterior,
                                              EstadoSolicitudCredito estadoNuevo,
                                              Usuarios usuarioResponsable,
                                              String observaciones) {
        if (solicitud.getHistorialAprobaciones() == null) {
            solicitud.setHistorialAprobaciones(new ArrayList<>());
        }

        HistorialAprobaciones historial = new HistorialAprobaciones();
        historial.setSolicitudCredito(solicitud);
        historial.setEstadoAnterior(estadoAnterior);
        historial.setEstadoNuevo(estadoNuevo);
        historial.setUsuarioResponsable(usuarioResponsable);
        historial.setRecomendacionesNivelAprobacion(observaciones);
        historial.setFechaAprobacion(LocalDateTime.now());
        historial.setDescripcionSolicitudCredito(
                String.format("Transición de %s a %s para la solicitud %s por un monto de $%s",
                        estadoAnterior, estadoNuevo, solicitud.getNumeroSolicitud(), solicitud.getMontoSolicitado())
        );

        solicitud.getHistorialAprobaciones().add(historial);
    }


    @Override
    protected void mapearDtoAEntidad(SolicitudesCreditoRequestDTO request, SolicitudesCredito solicitud) {

        if (request.getNombreCompletoAsociado() != null && !request.getNombreCompletoAsociado().isBlank()) {
            Asociados asociado = asociadosRepository
                    .findFirstByNombreCompletoAsociadoContainingIgnoreCase(request.getNombreCompletoAsociado().trim())
                    .orElseThrow(() -> new EntityNotFoundException("No se encontró ningún asociado registrado con el nombre: " + request.getNombreCompletoAsociado()));
            solicitud.setAsociado(asociado);
        }

        if (request.getMontoSolicitado() != null) {
            solicitud.setMontoSolicitado(request.getMontoSolicitado());
        }
        if (request.getPlazoMeses() != null) {
            solicitud.setPlazoMeses(request.getPlazoMeses());
        }
        if (request.getDestinoCredito() != null) {
            solicitud.setDestinoCredito(request.getDestinoCredito());
        }

        if (request.getUsuarioAsesor() != null && !request.getUsuarioAsesor().isBlank()) {
            Usuarios asesor = usuariosRepository.findByUsuarioIgnoreCaseAndEstadoTrue(request.getUsuarioAsesor())
                    .orElseThrow(() -> new EntityNotFoundException("El usuario asesor '" + request.getUsuarioAsesor() + "' no fue encontrado."));
            solicitud.setUsuarioAsesor(asesor);
        }

        if (request.getNombreProducto() != null || request.getTasaInteresAnual() != null) {
            TasasCreditos tasa = null;

            if (request.getNombreProducto() != null && !request.getNombreProducto().isBlank()) {
                if (request.getTasaInteresAnual() != null) {
                    tasa = tasaCreditosRepository.findFirstByNombreProductoIgnoreCaseAndTasaInteresAnualAndEstadoTrue(
                                    request.getNombreProducto().trim(), request.getTasaInteresAnual())
                            .orElseThrow(() -> new EntityNotFoundException("No se encontró la línea de crédito '" + request.getNombreProducto() +
                                    "' con la tasa de interés del " + request.getTasaInteresAnual() + "%."));
                } else {
                    tasa = tasaCreditosRepository.findFirstByNombreProductoIgnoreCaseAndEstadoTrue(request.getNombreProducto().trim())
                            .orElseThrow(() -> new EntityNotFoundException("La línea de crédito '" + request.getNombreProducto() + "' especificada no existe o no está activa."));
                }
            } else if (request.getTasaInteresAnual() != null) {
                tasa = tasaCreditosRepository.findFirstByTasaInteresAnualAndEstadoTrue(request.getTasaInteresAnual())
                        .orElseThrow(() -> new EntityNotFoundException("La tasa de préstamo especificada no existe."));
            }

            if (tasa != null) {
                if (request.getFrecuenciaPago() != null && tasa.getFrecuenciasPago() != null) {
                    boolean frecuenciaPermitida = tasa.getFrecuenciasPago().stream()
                            .anyMatch(f -> f.equalsIgnoreCase(request.getFrecuenciaPago()));

                    if (!frecuenciaPermitida) {
                        throw new IllegalArgumentException("La frecuencia de pago '" + request.getFrecuenciaPago() +
                                "' no está permitida para la línea de crédito '" + tasa.getNombreProducto() + "'.");
                    }
                }
                solicitud.setTasaReferencia(tasa);
            }
        }

        if (request.getGarantias() != null && !request.getGarantias().isEmpty()) {
            mapearGarantias(request, solicitud);
        }
        if (request.getReferencias() != null && !request.getReferencias().isEmpty()) {
            mapearReferencias(request, solicitud);
        }
        if (request.getDocumentosAdjuntos() != null && !request.getDocumentosAdjuntos().isEmpty()) {
            mapearDocumentos(request, solicitud);
        }

        if (request.getAnalisisAsesor() != null) {
            CreditoDetalles detalle = getCreditoDetalles(request, solicitud);
            solicitud.setCreditoDetalle(detalle);
        }

        if (request.getNumeroSolicitud() != null && !request.getNumeroSolicitud().isBlank()) {
            solicitud.setNumeroSolicitud(request.getNumeroSolicitud());
        } else if (solicitud.getNumeroSolicitud() == null || solicitud.getNumeroSolicitud().isBlank()) {
            solicitud.setNumeroSolicitud(generarNumeroSolicitudCorrelativo());
        }
    }

    private void mapearGarantias(SolicitudesCreditoRequestDTO request, SolicitudesCredito solicitud) {
        if (request.getGarantias() == null) return;

        if (solicitud.getGarantias() == null) {
            solicitud.setGarantias(new java.util.HashSet<>());
        }

        if (request.getGarantias().isEmpty()) {
            solicitud.getGarantias().clear();
            return;
        }

        for (CreditoGarantiasRequestDTO gDto : request.getGarantias()) {
            if (gDto.getTipoGarantia() == null && gDto.getValorEstimado() == null && gDto.getDescripcion() == null) {
                continue;
            }

            CreditoGarantias nuevaGarantia = new CreditoGarantias();

            if (gDto.getTipoGarantia() != null) nuevaGarantia.setTipoGarantia(gDto.getTipoGarantia());
            if (gDto.getValorEstimado() != null) nuevaGarantia.setValorEstimado(gDto.getValorEstimado());
            if (gDto.getDireccionGarantia() != null) nuevaGarantia.setDireccionGarantia(limpiarTexto(gDto.getDireccionGarantia()));
            if (gDto.getDescripcion() != null) nuevaGarantia.setDescripcion(limpiarTexto(gDto.getDescripcion()));

            nuevaGarantia.setSolicitudCredito(solicitud);

            boolean esFiador = "FIADOR".equalsIgnoreCase(gDto.getTipoGarantia());
            if (esFiador) {
                if (gDto.getNombreFiador() != null) nuevaGarantia.setNombreFiador(limpiarTexto(gDto.getNombreFiador()));
                if (gDto.getIdentificacionFiador() != null) nuevaGarantia.setIdentificacionFiador(limpiarTexto(gDto.getIdentificacionFiador()));
                if (gDto.getTelefonoFiador() != null) nuevaGarantia.setTelefonoFiador(limpiarTexto(gDto.getTelefonoFiador()));
                if (gDto.getIngresosFiador() != null) nuevaGarantia.setIngresosFiador(gDto.getIngresosFiador());
            }

            SolicitudesGarantiaRelacion garantiaRelacion = new SolicitudesGarantiaRelacion();
            garantiaRelacion.setMontoComprometido(gDto.getValorEstimado());
            if (gDto.getDescripcion() != null) {
                garantiaRelacion.setObservaciones(limpiarTexto(gDto.getDescripcion()));
            }

            garantiaRelacion.setSolicitudCredito(solicitud);
            garantiaRelacion.setGarantia(nuevaGarantia);

            solicitud.getGarantias().add(garantiaRelacion);
        }
    }

    private String limpiarTexto(String texto) {
        return (texto != null && !texto.isBlank()) ? texto.trim() : null;
    }

    private void mapearReferencias(SolicitudesCreditoRequestDTO request, SolicitudesCredito solicitud) {
        if (request.getReferencias() != null) {
            if (solicitud.getReferencias() == null) {
                solicitud.setReferencias(new java.util.HashSet<>());
            } else {
                solicitud.getReferencias().clear();
            }

            request.getReferencias().forEach(rDto -> {
                CreditoReferencias referencia = new CreditoReferencias();
                referencia.setNombreCompleto(limpiarTexto(rDto.getNombreCompleto()));
                referencia.setTelefono(limpiarTexto(rDto.getTelefono()));
                referencia.setDireccion(limpiarTexto(rDto.getDireccion()));
                referencia.setTipoReferencia(rDto.getTipoReferencia() != null ? rDto.getTipoReferencia() : "PERSONAL");

                referencia.setSolicitudCredito(solicitud);

                SolicitudesCreditoRelacion relacion = new SolicitudesCreditoRelacion();
                relacion.setParentescoRelacion(rDto.getParentesco());

                relacion.setSolicitudCredito(solicitud);
                relacion.setReferencia(referencia);

                solicitud.getReferencias().add(relacion);
            });
        }
    }

    private void mapearDocumentos(SolicitudesCreditoRequestDTO request, SolicitudesCredito solicitud) {
        if (request.getDocumentosAdjuntos() != null) {
            if (solicitud.getDocumentosAdjuntos() == null) {
                solicitud.setDocumentosAdjuntos(new java.util.HashSet<>());
            } else {
                solicitud.getDocumentosAdjuntos().clear();
            }

            request.getDocumentosAdjuntos().forEach(dDto -> {
                if (dDto.getTipoDocumento() != null && !dDto.getTipoDocumento().isBlank()) {
                    CreditoDocumentosAdjuntos doc = new CreditoDocumentosAdjuntos();
                    doc.setTipoDocumento(dDto.getTipoDocumento().toUpperCase().trim());
                    doc.setSolicitudCredito(solicitud);
                    doc.setEstado(true);

                    solicitud.getDocumentosAdjuntos().add(doc);
                }
            });
        }
    }

    private static @NonNull CreditoDetalles getCreditoDetalles(SolicitudesCreditoRequestDTO request, SolicitudesCredito solicitud) {
        CreditoDetallesRequestDTO analisis = request.getAnalisisAsesor();

        CreditoDetalles detalle = solicitud.getCreditoDetalle();
        if (detalle == null) {
            detalle = new CreditoDetalles();
        }

        detalle.setDescripcionCredito(analisis.getDescripcionCredito());
        detalle.setValoracionProyecto(analisis.getValoracionProyecto());
        detalle.setValoracionAsociado(analisis.getValoracionAsociado());
        detalle.setDescripcionGarantia(analisis.getDescripcionGarantia());
        detalle.setHistorialCreditosPrevios(analisis.getHistorialCreditosPrevios());
        detalle.setRecomendaciones(analisis.getRecomendaciones());

        detalle.setSolicitudCredito(solicitud);
        return detalle;
    }

    private synchronized String generarNumeroSolicitudCorrelativo() {
        String prefijoBase = "SOLI-01-";

        return solicitudesCreditoRepository
                .findTopByNumeroSolicitudStartingWithOrderByNumeroSolicitudDesc(prefijoBase)
                .map(ultimaSolicitud -> {
                    String ultimoNumero = ultimaSolicitud.getNumeroSolicitud();
                    String parteCorrelativa = ultimoNumero.substring(prefijoBase.length());
                    int siguienteCorrelativo = Integer.parseInt(parteCorrelativa) + 1;
                    return String.format("%s%04d", prefijoBase, siguienteCorrelativo);
                })
                .orElse(prefijoBase + "0001");
    }


    @Override
    protected SolicitudesCreditoResponseDTO mapToResponseDTO(SolicitudesCredito solicitud) {

        CreditoDetalles detalle = solicitud.getCreditoDetalle();

        CreditoDetallesResponseDTO detallesDto = (detalle != null) ? CreditoDetallesResponseDTO.builder()
                .idDetalle(detalle.getId())
                .descripcionCredito(detalle.getDescripcionCredito())
                .valoracionProyecto(detalle.getValoracionProyecto())
                .valoracionAsociado(detalle.getValoracionAsociado())
                .descripcionGarantia(detalle.getDescripcionGarantia())
                .historialCreditosPrevios(detalle.getHistorialCreditosPrevios())
                .recomendaciones(detalle.getRecomendaciones())
                .fechaEvaluacion(detalle.getFechaEvaluacion())
                .build() : null;

        List<SolicitudGarantiaRelacionResponseDTO> garantiasDto = solicitud.getGarantias() != null ?
                solicitud.getGarantias().stream().map(g -> {
                    CreditoGarantiasResponseDTO garantiaInnerDto = null;
                    if (g.getGarantia() != null) {
                        garantiaInnerDto = CreditoGarantiasResponseDTO.builder()
                                .idGarantia(g.getGarantia().getId())
                                .tipoGarantia(g.getGarantia().getTipoGarantia())
                                .valorEstimado(g.getGarantia().getValorEstimado())
                                .direccionGarantia(g.getGarantia().getDireccionGarantia())
                                .descripcion(g.getGarantia().getDescripcion())
                                .nombreFiador(g.getGarantia().getNombreFiador())
                                .identificacionFiador(g.getGarantia().getIdentificacionFiador())
                                .telefonoFiador(g.getGarantia().getTelefonoFiador())
                                .ingresosFiador(g.getGarantia().getIngresosFiador())
                                .build();
                    }

                    return SolicitudGarantiaRelacionResponseDTO.builder()
                            .idSolicitudGarantia(g.getId())
                            .montoComprometido(g.getMontoComprometido())
                            .observaciones(g.getObservaciones())
                            .garantia(garantiaInnerDto)
                            .build();
                }).collect(Collectors.toList()) : Collections.emptyList();

        List<CreditoReferenciasResponseDTO> referenciasDto = solicitud.getReferencias() != null ?
                solicitud.getReferencias().stream().map(r -> {
                    CreditoReferencias ref = r.getReferencia();
                    return CreditoReferenciasResponseDTO.builder()
                            .idReferencia(r.getId())
                            .nombreCompleto(ref != null ? ref.getNombreCompleto() : null)
                            .parentesco(r.getParentescoRelacion())
                            .telefono(ref != null ? ref.getTelefono() : null)
                            .direccion(ref != null ? ref.getDireccion() : null)
                            .tipoReferencia(ref != null ? ref.getTipoReferencia() : null)
                            .build();
                }).collect(Collectors.toList()) : Collections.emptyList();

        List<CreditoDocumentosAdjuntosResponseDTO> documentosDto = solicitud.getDocumentosAdjuntos() != null ?
                solicitud.getDocumentosAdjuntos().stream()
                        .filter(d -> Boolean.TRUE.equals(d.getEstado()))
                        .map(d -> CreditoDocumentosAdjuntosResponseDTO.builder()
                                .idDocumentoAdjunto(d.getId())
                                .numeroSolicitud(solicitud.getNumeroSolicitud())
                                .tipoDocumento(d.getTipoDocumento())
                                .rutaArchivoStorage(d.getRutaArchivoStorage())
                                .fechaSubida(d.getFechaSubida())
                                .estado(d.getEstado())
                                .build()
                        ).collect(Collectors.toList()) : Collections.emptyList();

        Integer numeroAsociado = (solicitud.getAsociado() != null)
                ? solicitud.getAsociado().getNumeroAsociado()
                : null;

        String nombreAsociado;
        if (solicitud.getAsociado() != null) {
            String nombres = solicitud.getAsociado().getNombres() != null ? solicitud.getAsociado().getNombres() : "";
            String apellidos = solicitud.getAsociado().getApellidos() != null ? solicitud.getAsociado().getApellidos() : "";
            nombreAsociado = (nombres + " " + apellidos).trim();
        } else {
            nombreAsociado = "Sin Asociado";
        }

        List<HistorialAprobacionesResponseDTO> historialDto = solicitud.getHistorialAprobaciones() != null ?
                solicitud.getHistorialAprobaciones().stream().map(h -> HistorialAprobacionesResponseDTO.builder()
                        .idHistorialAprob(h.getId())
                        .numeroAsociado(numeroAsociado)
                        .nombreCompletoAsociado(nombreAsociado)
                        .numeroSolicitud(solicitud.getNumeroSolicitud())
                        .estadoAnterior(h.getEstadoAnterior() != null ? h.getEstadoAnterior().name() : null)
                        .estadoNuevo(h.getEstadoNuevo() != null ? h.getEstadoNuevo().name() : null)
                        .usuarioResponsable(h.getUsuarioResponsable() != null ? h.getUsuarioResponsable().getUsuario() : "Sin Asignar")
                        .descripcionSolicitudCredito(h.getDescripcionSolicitudCredito())
                        .valoracionesNivelAprobacion(h.getValoracionesNivelAprobacion())
                        .recomendacionesNivelAprobacion(h.getRecomendacionesNivelAprobacion())
                        .fechaAprobacion(h.getFechaAprobacion())
                        .build()
                ).collect(Collectors.toList()) : Collections.emptyList();

        return SolicitudesCreditoResponseDTO.builder()
                .idSolicitudLinea(solicitud.getId())
                .numeroSolicitud(solicitud.getNumeroSolicitud())
                .nombreCompletoAsociado(nombreAsociado)
                .usuarioAsesor(solicitud.getUsuarioAsesor() != null ? solicitud.getUsuarioAsesor().getUsuario() : "Sin Asignar")
                .nombreProducto(solicitud.getTasaReferencia() != null ? solicitud.getTasaReferencia().getNombreProducto() : null)
                .montoSolicitado(solicitud.getMontoSolicitado())
                .plazoMeses(solicitud.getPlazoMeses())
                .frecuenciaPago(solicitud.getTasaReferencia() != null ? solicitud.getTasaReferencia().getFrecuenciasPago().toString() : null)
                .tasaReferencia(solicitud.getTasaReferencia() != null ? solicitud.getTasaReferencia().getTasaInteresAnual() : null)
                .destinoCredito(solicitud.getDestinoCredito())
                .estadoActual(solicitud.getEstadoActual() != null ? solicitud.getEstadoActual().name() : null)
                .estado(solicitud.getEstado())
                .fechaSolicitud(solicitud.getFechaSolicitud())
                .analisisAsesor(detallesDto)
                .garantias(garantiasDto)
                .referencias(referenciasDto)
                .documentosAdjuntos(documentosDto)
                .historialAprobaciones(historialDto)
                .build();
    }
}