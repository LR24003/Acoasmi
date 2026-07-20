package com.acoasmi.roble.service;

import com.acoasmi.roble.dto.request.CumplimientoPerfilRiesgoRequestDTO;
import com.acoasmi.roble.dto.response.CumplimientoPerfilRiesgoResponseDTO;
import com.acoasmi.roble.entity.Asociados;
import com.acoasmi.roble.entity.CumplimientoPerfilRiesgo;

import java.util.List;

public interface CumplimientoPerfilRiesgoService extends AcoasmiService<CumplimientoPerfilRiesgo,
        CumplimientoPerfilRiesgoRequestDTO, CumplimientoPerfilRiesgoResponseDTO, Long> {

    List<CumplimientoPerfilRiesgoResponseDTO> getByNivelRiesgo(String nivelRiesgo);


    void generarPerfilRiesgoInicial(Asociados asociado, com.acoasmi.roble.dto.request.AsociadosRequestDTO dto);
}
