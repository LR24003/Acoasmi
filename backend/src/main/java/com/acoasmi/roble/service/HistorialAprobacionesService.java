package com.acoasmi.roble.service;

import com.acoasmi.roble.dto.request.HistorialAprobacionesRequestDTO;
import com.acoasmi.roble.dto.response.HistorialAprobacionesResponseDTO;
import com.acoasmi.roble.entity.HistorialAprobaciones;

import java.util.List;

public interface HistorialAprobacionesService extends AcoasmiService<HistorialAprobaciones,
        HistorialAprobacionesRequestDTO, HistorialAprobacionesResponseDTO, Long> {

    HistorialAprobacionesResponseDTO registrarEvaluacion(HistorialAprobacionesRequestDTO requestDto);

    List<HistorialAprobacionesResponseDTO> obtenerHistorialPorNumeroSolicitud(String numeroSolicitud);
    
    List<HistorialAprobacionesResponseDTO> obtenerHistorialPorNumeroAsociado(Integer numeroAsociado);
}