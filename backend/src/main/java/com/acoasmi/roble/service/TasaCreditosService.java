package com.acoasmi.roble.service;

import com.acoasmi.roble.dto.request.TasaCreditosRequestDTO;
import com.acoasmi.roble.dto.response.TasaCreditosResponseDTO;
import com.acoasmi.roble.entity.TasasCreditos;

import java.util.Collection;
import java.util.List;

public interface TasaCreditosService extends AcoasmiService<TasasCreditos,
        TasaCreditosRequestDTO, TasaCreditosResponseDTO, Long> {

    List<TasaCreditosResponseDTO> buscarPorNombreProducto(String nombreProducto);

    List<TasaCreditosResponseDTO> buscarPorNombreYFrecuencia(String nombreProducto, Collection<String> frecuenciasPago);
}
