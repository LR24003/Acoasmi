package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.HistorialAprobacionesRequestDTO;
import com.acoasmi.roble.dto.response.HistorialAprobacionesResponseDTO;
import com.acoasmi.roble.entity.HistorialAprobaciones;
import com.acoasmi.roble.entity.SolicitudesCredito;
import com.acoasmi.roble.entity.Usuarios;
import com.acoasmi.roble.enums.EstadoSolicitudCredito;
import com.acoasmi.roble.repository.HistorialAprobacionesRepository;
import com.acoasmi.roble.repository.SolicitudesCreditoRepository;
import com.acoasmi.roble.repository.UsuariosRepository;
import com.acoasmi.roble.service.HistorialAprobacionesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistorialAprobacionesServiceImpl
        extends AcoasmiServiceImpl<HistorialAprobaciones, HistorialAprobacionesRequestDTO, HistorialAprobacionesResponseDTO, Long>
        implements HistorialAprobacionesService {

    private final HistorialAprobacionesRepository historialAprobacionesRepository;
    private final SolicitudesCreditoRepository solicitudesCreditoRepository;
    private final UsuariosRepository usuariosRepository;

    public HistorialAprobacionesServiceImpl(HistorialAprobacionesRepository historialAprobacionesRepository,
                                            SolicitudesCreditoRepository solicitudesCreditoRepository,
                                            UsuariosRepository usuariosRepository) {
        super(historialAprobacionesRepository, HistorialAprobaciones.class);
        this.historialAprobacionesRepository = historialAprobacionesRepository;
        this.solicitudesCreditoRepository = solicitudesCreditoRepository;
        this.usuariosRepository = usuariosRepository;
    }

    @Override
    @Transactional
    public HistorialAprobacionesResponseDTO registrarEvaluacion(HistorialAprobacionesRequestDTO requestDto) {
        SolicitudesCredito solicitud = solicitudesCreditoRepository
                .findByNumeroSolicitudWithDetailsAndGarantias(requestDto.getNumeroSolicitud())
                .orElseThrow(() -> new RuntimeException(
                        "No se encontró ninguna solicitud de crédito con el número: " + requestDto.getNumeroSolicitud()));

        if (solicitud.getAsociado() == null || !requestDto.getNumeroAsociado().equals(solicitud.getAsociado().getNumeroAsociado())) {
            throw new IllegalArgumentException(
                    "El número de asociado " + requestDto.getNumeroAsociado() +
                            " no coincide con el registrado en la solicitud " + requestDto.getNumeroSolicitud());
        }

        Usuarios usuarioResponsable = usuariosRepository
                .findByUsuarioIgnoreCaseAndEstadoTrue(requestDto.getUsuarioResponsable())
                .orElseThrow(() -> new RuntimeException(
                        "El usuario responsable '" + requestDto.getUsuarioResponsable() + "' no existe o está inactivo."));

        EstadoSolicitudCredito estadoAnterior = solicitud.getEstadoActual();
        solicitud.setEstadoActual(requestDto.getEstadoNuevo());
        solicitudesCreditoRepository.save(solicitud);

        HistorialAprobaciones historial = new HistorialAprobaciones();
        historial.setSolicitudCredito(solicitud);
        historial.setEstadoAnterior(estadoAnterior);
        historial.setEstadoNuevo(requestDto.getEstadoNuevo());
        historial.setUsuarioResponsable(usuarioResponsable);
        historial.setDescripcionSolicitudCredito(requestDto.getDescripcionSolicitudCredito());
        historial.setValoracionesNivelAprobacion(requestDto.getValoracionesNivelAprobacion());
        historial.setRecomendacionesNivelAprobacion(requestDto.getRecomendacionesNivelAprobacion());
        historial.setFechaAprobacion(LocalDateTime.now());

        HistorialAprobaciones historialGuardado = historialAprobacionesRepository.save(historial);

        return mapToResponseDTO(historialGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialAprobacionesResponseDTO> obtenerHistorialPorNumeroSolicitud(String numeroSolicitud) {
        return historialAprobacionesRepository.findByNumeroSolicitudOrderByFechaDesc(numeroSolicitud)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialAprobacionesResponseDTO> obtenerHistorialPorNumeroAsociado(Integer numeroAsociado) {
        return historialAprobacionesRepository.findByNumeroAsociadoOrderByFechaDesc(numeroAsociado)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    protected void mapearDtoAEntidad(HistorialAprobacionesRequestDTO request, HistorialAprobaciones entidad) {
        if (request.getNumeroSolicitud() != null) {
            SolicitudesCredito solicitud = solicitudesCreditoRepository
                    .findByNumeroSolicitudWithDetailsAndGarantias(request.getNumeroSolicitud())
                    .orElseThrow(() -> new RuntimeException("Solicitud no encontrada: " + request.getNumeroSolicitud()));
            entidad.setSolicitudCredito(solicitud);
        }

        if (request.getUsuarioResponsable() != null) {
            Usuarios usuario = usuariosRepository.findByUsuarioIgnoreCaseAndEstadoTrue(request.getUsuarioResponsable())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + request.getUsuarioResponsable()));
            entidad.setUsuarioResponsable(usuario);
        }

        entidad.setEstadoNuevo(request.getEstadoNuevo());
        entidad.setDescripcionSolicitudCredito(request.getDescripcionSolicitudCredito());
        entidad.setValoracionesNivelAprobacion(request.getValoracionesNivelAprobacion());
        entidad.setRecomendacionesNivelAprobacion(request.getRecomendacionesNivelAprobacion());
    }

    @Override
    protected HistorialAprobacionesResponseDTO mapToResponseDTO(HistorialAprobaciones h) {
        SolicitudesCredito solicitud = h.getSolicitudCredito();

        Integer numeroAsociado = (solicitud != null && solicitud.getAsociado() != null)
                ? solicitud.getAsociado().getNumeroAsociado()
                : null;

        String nombreCompleto = "Sin Asociado";
        if (solicitud != null && solicitud.getAsociado() != null) {
            String nombres = solicitud.getAsociado().getNombres() != null ? solicitud.getAsociado().getNombres() : "";
            String apellidos = solicitud.getAsociado().getApellidos() != null ? solicitud.getAsociado().getApellidos() : "";
            nombreCompleto = (nombres + " " + apellidos).trim();
        }

        return HistorialAprobacionesResponseDTO.builder()
                .idHistorialAprob(h.getId())
                .numeroAsociado(numeroAsociado)
                .nombreCompletoAsociado(nombreCompleto)
                .numeroSolicitud(solicitud != null ? solicitud.getNumeroSolicitud() : null)
                .estadoAnterior(h.getEstadoAnterior() != null ? h.getEstadoAnterior().name() : null)
                .estadoNuevo(h.getEstadoNuevo() != null ? h.getEstadoNuevo().name() : null)
                .usuarioResponsable(h.getUsuarioResponsable() != null ? h.getUsuarioResponsable().getUsuario() : "Sin Asignar")
                .descripcionSolicitudCredito(h.getDescripcionSolicitudCredito())
                .valoracionesNivelAprobacion(h.getValoracionesNivelAprobacion())
                .recomendacionesNivelAprobacion(h.getRecomendacionesNivelAprobacion())
                .fechaAprobacion(h.getFechaAprobacion())
                .build();
    }
}