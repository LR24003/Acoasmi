package com.acoasmi.roble.service;

import com.acoasmi.roble.dto.request.DesembolsoCreditoRequestDTO;
import com.acoasmi.roble.dto.response.DesembolsoCreditoResponseDTO;
import com.acoasmi.roble.entity.DesembolsoCredito;

import java.time.LocalDateTime;
import java.util.List;

public interface DesembolsoCreditoService extends AcoasmiService<DesembolsoCredito,
        DesembolsoCreditoRequestDTO, DesembolsoCreditoResponseDTO, Long>{

    DesembolsoCreditoResponseDTO obtenerPorNumeroDesembolso(String numeroDesembolso);

    List<DesembolsoCreditoResponseDTO> obtenerPorNumeroPrestamo(String numeroPrestamo);

    List<DesembolsoCreditoResponseDTO> obtenerPorNumeroAsociado(Integer numeroAsociado);

    List<DesembolsoCreditoResponseDTO> obtenerPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
