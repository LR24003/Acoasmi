package com.acoasmi.roble.service;

import com.acoasmi.roble.dto.request.CajaAperturaRequestDTO;
import com.acoasmi.roble.dto.request.CajaCierreRequestDTO;
import com.acoasmi.roble.dto.response.ControlCajasResponseDTO;
import com.acoasmi.roble.entity.ControlCajas;

public interface ControlCajasService extends AcoasmiService<ControlCajas, CajaAperturaRequestDTO,
        ControlCajasResponseDTO, Long> {


    ControlCajasResponseDTO realizarArqueoIntermedio(Long idSesionCaja, CajaCierreRequestDTO arqueoDto);

    ControlCajasResponseDTO cerrarCaja(Long idSesionCaja, CajaCierreRequestDTO cierreDto);

}