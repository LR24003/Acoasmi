package com.acoasmi.roble.service;

import com.acoasmi.roble.dto.request.RolesRequestDTO;
import com.acoasmi.roble.dto.response.RolesResponseDTO;

public interface RolesService extends AcoasmiService<Object, RolesRequestDTO, RolesResponseDTO, Long> {

    // Buscar un rol por su nombre único
    RolesResponseDTO getByNombreRol(String nombreRol);
}
