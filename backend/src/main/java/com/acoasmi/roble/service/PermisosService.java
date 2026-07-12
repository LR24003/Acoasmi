package com.acoasmi.roble.service;

import com.acoasmi.roble.dto.request.PermisosRequestDTO;
import com.acoasmi.roble.dto.response.PermisosResponseDTO;

public interface PermisosService extends AcoasmiService<Object, PermisosRequestDTO, PermisosResponseDTO, Long> {

    // Buscar un permiso por su código identificador (ej. USUARIOS_CREAR)
    PermisosResponseDTO getByCodigoPermiso(String codigoPermiso);
}
