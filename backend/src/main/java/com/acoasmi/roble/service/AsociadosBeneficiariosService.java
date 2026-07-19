package com.acoasmi.roble.service;

import com.acoasmi.roble.dto.request.AsociadosBeneficiariosRequestDTO;
import com.acoasmi.roble.dto.response.AsociadosBeneficiariosResponseDTO;
import com.acoasmi.roble.entity.AsociadosBeneficiarios;

import java.util.List;

public interface AsociadosBeneficiariosService
        extends AcoasmiService<AsociadosBeneficiarios, AsociadosBeneficiariosRequestDTO,
        AsociadosBeneficiariosResponseDTO, Long> {

    List<AsociadosBeneficiariosResponseDTO> getByNumeroCuenta(String numeroCuenta);

    List<AsociadosBeneficiariosResponseDTO> getByNumeroAsociado(Integer numeroAsociado);
}