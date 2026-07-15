package com.acoasmi.roble.service;

import com.acoasmi.roble.dto.request.PermisosRequestDTO;
import com.acoasmi.roble.dto.response.PermisosResponseDTO;
import com.acoasmi.roble.entity.Permisos;

public interface PermisosService extends AcoasmiService<Permisos, PermisosRequestDTO,
        PermisosResponseDTO, Long> {

    PermisosResponseDTO getByCodigoPermiso(String codigoPermiso);
}
