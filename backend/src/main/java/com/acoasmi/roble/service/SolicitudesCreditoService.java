package com.acoasmi.roble.service;

import com.acoasmi.roble.dto.request.SolicitudesCreditoRequestDTO;
import com.acoasmi.roble.dto.response.SolicitudesCreditoResponseDTO;
import com.acoasmi.roble.entity.SolicitudesCredito;
import com.acoasmi.roble.enums.EstadoSolicitudCredito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SolicitudesCreditoService extends AcoasmiService<SolicitudesCredito,
        SolicitudesCreditoRequestDTO, SolicitudesCreditoResponseDTO, Long> {

    SolicitudesCreditoResponseDTO obtenerPorNumeroSolicitud(String numeroSolicitud);

    Page<SolicitudesCreditoResponseDTO> listarPorEstadoActualSolicitud(EstadoSolicitudCredito estadoActual, Pageable pageable);

    List<SolicitudesCreditoResponseDTO> listarPorAsesor(String usuarioAsesor);
    
}