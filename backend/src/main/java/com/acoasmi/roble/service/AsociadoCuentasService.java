package com.acoasmi.roble.service;

import com.acoasmi.roble.dto.request.AsociadoCuentasRequestDTO;
import com.acoasmi.roble.dto.response.AsociadoCuentasResponseDTO;
import com.acoasmi.roble.entity.AsociadoCuentas;
import java.util.List;

public interface AsociadoCuentasService extends AcoasmiService<AsociadoCuentas,
        AsociadoCuentasRequestDTO, AsociadoCuentasResponseDTO, Long> {

    AsociadoCuentasResponseDTO getByNumeroCuenta(String numeroCuenta);

    List<AsociadoCuentasResponseDTO> getByNumeroAsociado(Integer numeroAsociado);

    List<AsociadoCuentasResponseDTO> obtenerCuentasPorAsociadoYPlazo(Integer numeroAsociado, String plazoDias);
}
