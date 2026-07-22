package com.acoasmi.roble.service;

import com.acoasmi.roble.dto.request.FacturacionRequestDTO;
import com.acoasmi.roble.dto.response.FacturacionResponseDTO;
import com.acoasmi.roble.entity.Facturas;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FacturacionService extends AcoasmiService<Facturas,
        FacturacionRequestDTO, FacturacionResponseDTO, Long> {

    FacturacionResponseDTO crearFactura(FacturacionRequestDTO request);

    FacturacionResponseDTO obtenerPorUuid(UUID uuid);

    Page<FacturacionResponseDTO> getByCajaUsuario(String usuario, Pageable pageable);

    Page<FacturacionResponseDTO> getByAsociadoNumeroAsociado(Integer numeroAsociado, Pageable pageable);
}