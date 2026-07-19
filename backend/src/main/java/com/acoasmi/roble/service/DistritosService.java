package com.acoasmi.roble.service;

import com.acoasmi.roble.dto.request.DistritosRequestDTO;
import com.acoasmi.roble.dto.response.DistritosResponseDTO;
import com.acoasmi.roble.entity.Distritos;

public interface DistritosService extends AcoasmiService<Distritos, DistritosRequestDTO,
        DistritosResponseDTO, Long>{

    DistritosResponseDTO getByCodigoDistrito(Integer codigoDistrito);

    DistritosResponseDTO getByNombreDistrito(String nombreDistrito);
}
