package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.CreditoDetallesRequestDTO;
import com.acoasmi.roble.dto.request.SolicitudesCreditoRequestDTO;
import com.acoasmi.roble.dto.response.*;
import com.acoasmi.roble.entity.*;
import com.acoasmi.roble.repository.*;
import com.acoasmi.roble.service.SolicitudesCreditoService;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new RuntimeException("No se encontró ninguna solicitud con el número: " + numeroSolicitud));

        // Carga en memoria de colecciones referencias y documentos
        solicitudesCreditoRepository.findByNumeroSolicitudWithReferenciasAndDocumentos(numeroSolicitud);

        return mapToResponseDTO(solicitud);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SolicitudesCreditoResponseDTO> listarPorEstadoPrestamo(String estadoPrestamo, Pageable pageable) {
        return solicitudesCreditoRepository.findByEstadoPrestamo(estadoPrestamo, pageable)
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
            throw new RuntimeException("La solicitud con el número '" + requestDto.getNumeroSolicitud() + "' ya existe.");
        }

        SolicitudesCredito solicitud = new SolicitudesCredito();
        mapearDtoAEntidad(requestDto, solicitud);

        if (solicitud.getEstadoSolicitud() == null) {
            solicitud.setEstadoSolicitud("RECIBIDA");
        }
        if (solicitud.getEstadoPrestamo() == null) {
            solicitud.setEstadoPrestamo("PENDIENTE");
        }

        SolicitudesCredito solicitudGuardada = solicitudesCreditoRepository.save(solicitud);
        solicitudesCreditoRepository.flush();

        return mapToResponseDTO(solicitudGuardada);
    }

    @Override
    @Transactional
    public SolicitudesCreditoResponseDTO update(Long id, SolicitudesCreditoRequestDTO requestDto) {
        SolicitudesCredito solicitud = solicitudesCreditoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud de crédito no encontrada con el ID: " + id));

        mapearDtoAEntidad(requestDto, solicitud);

        return mapToResponseDTO(solicitudesCreditoRepository.save(solicitud));
    }

    @Override
    protected void mapearDtoAEntidad(SolicitudesCreditoRequestDTO request, SolicitudesCredito solicitud) {

        if (request.getNombreCompletoAsociado() != null && !request.getNombreCompletoAsociado().isBlank()) {
            Asociados asociado = asociadosRepository
                    .findFirstByNombreCompletoAsociadoContainingIgnoreCase(request.getNombreCompletoAsociado().trim())
                    .orElseThrow(() -> new RuntimeException("No se encontró ningún asociado registrado con el nombre: " + request.getNombreCompletoAsociado()));

            solicitud.setAsociado(asociado);
        }

        solicitud.setMontoSolicitado(request.getMontoSolicitado());
        solicitud.setPlazoMeses(request.getPlazoMeses());
        solicitud.setDestinoCredito(request.getDestinoCredito());

        if (request.getEstadoPrestamo() != null && !request.getEstadoPrestamo().isBlank()) {
            solicitud.setEstadoPrestamo(request.getEstadoPrestamo());
        }

        if (request.getUsuarioAsesor() != null && !request.getUsuarioAsesor().isBlank()) {
            Usuarios asesor = usuariosRepository.findByUsuarioIgnoreCaseAndEstadoTrue(request.getUsuarioAsesor())
                    .orElseThrow(() -> new RuntimeException("El usuario asesor '" + request.getUsuarioAsesor() + "' no fue encontrado."));
            solicitud.setUsuarioAsesor(asesor);
        }

        if (request.getTasaInteresAnual() != null) {
            TasasCreditos tasa = tasaCreditosRepository.findFirstByTasaInteresAnualAndEstadoTrue(request.getTasaInteresAnual())
                    .orElseThrow(() -> new RuntimeException("La tasa de préstamo especificada no existe."));

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

        mapearGarantias(request, solicitud);
        mapearReferencias(request, solicitud);
        mapearDocumentos(request, solicitud);

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
        if (request.getGarantias() != null) {
            if (solicitud.getGarantias() == null) {
                solicitud.setGarantias(new java.util.HashSet<>());
            } else {
                solicitud.getGarantias().clear();
            }

            request.getGarantias().forEach(gDto -> {
                SolicitudesGarantiaRelacion garantia = new SolicitudesGarantiaRelacion();
                garantia.setMontoComprometido(gDto.getValorEstimado());
                garantia.setObservaciones(gDto.getDescripcion());
                garantia.setSolicitudCredito(solicitud);
                solicitud.getGarantias().add(garantia);
            });
        }
    }

    private void mapearReferencias(SolicitudesCreditoRequestDTO request, SolicitudesCredito solicitud) {
        if (request.getReferencias() != null) {
            if (solicitud.getReferencias() == null) {
                solicitud.setReferencias(new java.util.HashSet<>());
            } else {
                solicitud.getReferencias().clear();
            }

            request.getReferencias().forEach(rDto -> {
                SolicitudesCreditoRelacion relacion = new SolicitudesCreditoRelacion();
                relacion.setParentescoRelacion(rDto.getParentesco());
                relacion.setSolicitudCredito(solicitud);

                CreditoReferencias referencia = new CreditoReferencias();
                referencia.setNombreCompleto(rDto.getNombreCompleto());
                referencia.setTelefono(rDto.getTelefono());
                referencia.setDireccion(rDto.getDireccion());
                referencia.setTipoReferencia(rDto.getTipoReferencia() != null ? rDto.getTipoReferencia() : "PERSONAL");

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
                .descripcionCredito(detalle.getDescripcionCredito())
                .valoracionProyecto(detalle.getValoracionProyecto())
                .valoracionAsociado(detalle.getValoracionAsociado())
                .descripcionGarantia(detalle.getDescripcionGarantia())
                .historialCreditosPrevios(detalle.getHistorialCreditosPrevios())
                .recomendaciones(detalle.getRecomendaciones())
                .build() : null;

        List<SolicitudGarantiaRelacionResponseDTO> garantiasDto = solicitud.getGarantias() != null ?
                solicitud.getGarantias().stream().map(g -> SolicitudGarantiaRelacionResponseDTO.builder()
                        .idSolicitudGarantia(g.getId())
                        .montoComprometido(g.getMontoComprometido())
                        .observaciones(g.getObservaciones())
                        .build()
                ).collect(Collectors.toList()) : Collections.emptyList();

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

        String nombreAsociado = "Sin Asociado";
        if (solicitud.getAsociado() != null) {
            String nombres = solicitud.getAsociado().getNombres() != null ? solicitud.getAsociado().getNombres() : "";
            String apellidos = solicitud.getAsociado().getApellidos() != null ? solicitud.getAsociado().getApellidos() : "";
            nombreAsociado = (nombres + " " + apellidos).trim();
        }

        return SolicitudesCreditoResponseDTO.builder()
                .idSolicitudLinea(solicitud.getId())
                .numeroSolicitud(solicitud.getNumeroSolicitud())
                .nombreCompletoAsociado(nombreAsociado)
                .usuarioAsesor(solicitud.getUsuarioAsesor() != null ? solicitud.getUsuarioAsesor().getUsuario() : "Sin Asignar")
                .montoSolicitado(solicitud.getMontoSolicitado())
                .plazoMeses(solicitud.getPlazoMeses())
                .frecuenciaPago(solicitud.getTasaReferencia() != null && solicitud.getTasaReferencia().getFrecuenciasPago() != null
                        ? String.join(", ", solicitud.getTasaReferencia().getFrecuenciasPago())
                        : null)
                .tasaReferencia(solicitud.getTasaReferencia() != null ? solicitud.getTasaReferencia().getTasaInteresAnual() : null)
                .destinoCredito(solicitud.getDestinoCredito())
                .estadoPrestamo(solicitud.getEstadoPrestamo())
                .estado(solicitud.getEstado())
                .fechaSolicitud(solicitud.getFechaSolicitud())
                .analisisAsesor(detallesDto)
                .garantias(garantiasDto)
                .referencias(referenciasDto)
                .documentosAdjuntos(documentosDto)
                .build();
    }
}