package com.acoasmi.roble.service;

import com.acoasmi.roble.dto.request.DepartamentosRequestDTO;
import com.acoasmi.roble.dto.response.DepartamentosResponseDTO;
import com.acoasmi.roble.entity.Departamentos;

public interface DepartamentosService extends AcoasmiService <Departamentos, DepartamentosRequestDTO,
        DepartamentosResponseDTO, Long>{

    DepartamentosResponseDTO getByCodigoDepartamento(String codigoDepartamento);

    DepartamentosResponseDTO getByNombre(String nombre);
}
