package com.acoasmi.roble.service;

import com.acoasmi.roble.dto.request.CreditoDocumentosAdjuntosRequestDTO;
import com.acoasmi.roble.dto.request.SolicitudesCreditoRequestDTO;
import com.acoasmi.roble.dto.response.CreditoDocumentosAdjuntosResponseDTO;
import com.acoasmi.roble.dto.response.SolicitudesCreditoResponseDTO;
import com.acoasmi.roble.entity.SolicitudesCredito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SolicitudesCreditoService extends AcoasmiService<SolicitudesCredito,
        SolicitudesCreditoRequestDTO, SolicitudesCreditoResponseDTO, Long> {

    SolicitudesCreditoResponseDTO obtenerPorNumeroSolicitud(String numeroSolicitud);

    Page<SolicitudesCreditoResponseDTO> listarPorEstadoPrestamo(String estadoPrestamo, Pageable pageable);

    List<SolicitudesCreditoResponseDTO> listarPorAsesor(String usuarioAsesor);
    
}