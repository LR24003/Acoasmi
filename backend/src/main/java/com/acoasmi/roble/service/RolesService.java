package com.acoasmi.roble.service;

import com.acoasmi.roble.dto.request.RolesRequestDTO;
import com.acoasmi.roble.dto.response.RolesResponseDTO;
import com.acoasmi.roble.entity.Roles;

public interface RolesService extends AcoasmiService<Roles, RolesRequestDTO,
        RolesResponseDTO, Long> {

    RolesResponseDTO getByNombreRol(String nombreRol);
}
