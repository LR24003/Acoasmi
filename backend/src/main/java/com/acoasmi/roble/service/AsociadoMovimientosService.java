package com.acoasmi.roble.service;

import com.acoasmi.roble.dto.request.AsociadoMovimientosRequestDTO;
import com.acoasmi.roble.dto.response.AsociadoMovimientosResponseDTO;
import com.acoasmi.roble.dto.response.ComprobanteMovimientoDTO;
import com.acoasmi.roble.dto.response.EstadoCuentaResponseDTO;
import com.acoasmi.roble.entity.AsociadoMovimientos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AsociadoMovimientosService extends AcoasmiService<AsociadoMovimientos,
        AsociadoMovimientosRequestDTO, AsociadoMovimientosResponseDTO, Long> {

    ComprobanteMovimientoDTO obtenerComprobantePorId(Long idMovimiento);

    List<ComprobanteMovimientoDTO> obtenerComprobantesAperturaPorAsociado(Integer numeroAsociado);
    
    List<AsociadoMovimientosResponseDTO> obtenerHistorialPorNumeroCuenta(String numeroCuenta);

    Page<AsociadoMovimientosResponseDTO> obtenerHistorialPorNumeroCuentaPaginado(String numeroCuenta, Pageable pageable);

    EstadoCuentaResponseDTO obtenerEstadoCuenta(
            String numeroAsociado,
            String fechaInicio,
            String fechaFin
    );
}