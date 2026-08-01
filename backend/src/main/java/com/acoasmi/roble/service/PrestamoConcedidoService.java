package com.acoasmi.roble.service;

import com.acoasmi.roble.dto.request.GenerarPlanPagosRequestDTO;
import com.acoasmi.roble.dto.request.PrestamosConcedidosRequestDTO;
import com.acoasmi.roble.dto.request.SimularPlanPagosRequestDTO;
import com.acoasmi.roble.dto.response.PlanPagosResponseDTO;
import com.acoasmi.roble.dto.response.PrestamosConcedidosResponseDTO;
import com.acoasmi.roble.entity.PrestamosConcedidos;

import java.util.List;

public interface PrestamoConcedidoService extends AcoasmiService<PrestamosConcedidos,
        PrestamosConcedidosRequestDTO, PrestamosConcedidosResponseDTO, Long> {

    PrestamosConcedidosResponseDTO obtenerPorNumeroPrestamo(String numeroPrestamo);

    List<PrestamosConcedidosResponseDTO> obtenerPorNumeroAsociado(String numeroAsociado);

    PlanPagosResponseDTO simularPlanPagos(SimularPlanPagosRequestDTO request);

    PlanPagosResponseDTO generarYGuardarPlanPagos(GenerarPlanPagosRequestDTO request);

    PlanPagosResponseDTO obtenerPlanPagosPorNumeroPrestamo(String numeroPrestamo);
}
