package com.acoasmi.roble.service;


import com.acoasmi.roble.dto.request.MunicipiosRequestDTO;
import com.acoasmi.roble.dto.response.MunicipiosResponseDTO;
import com.acoasmi.roble.entity.Municipios;

public interface MunicipiosService extends AcoasmiService <Municipios, MunicipiosRequestDTO,
        MunicipiosResponseDTO, Long>{

    MunicipiosResponseDTO getByCodigoMunicipio(String codigoMunicipio);

    MunicipiosResponseDTO getByNombre(String nombre);
}
