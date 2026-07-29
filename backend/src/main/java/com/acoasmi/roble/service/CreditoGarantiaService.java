package com.acoasmi.roble.service;

import com.acoasmi.roble.dto.request.CreditoGarantiasRequestDTO;
import com.acoasmi.roble.dto.response.CreditoGarantiasResponseDTO;
import com.acoasmi.roble.dto.response.CreditoReferenciasResponseDTO;
import com.acoasmi.roble.entity.CreditoGarantias;

import java.util.List;

public interface CreditoGarantiaService extends AcoasmiService<CreditoGarantias,
        CreditoGarantiasRequestDTO, CreditoGarantiasResponseDTO, Long>{

    List<CreditoGarantiasResponseDTO> obtenerPorTipoGarantia(String  tipoGarantia);
}
