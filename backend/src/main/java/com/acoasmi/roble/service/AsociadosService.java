package com.acoasmi.roble.service;

import com.acoasmi.roble.dto.request.AsociadosRequestDTO;
import com.acoasmi.roble.dto.response.AsociadosResponseDTO;
import com.acoasmi.roble.entity.Asociados;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AsociadosService extends AcoasmiService<Asociados, AsociadosRequestDTO, AsociadosResponseDTO, Long> {

    AsociadosResponseDTO getByNumeroAsociado(Integer numeroAsociado);

    AsociadosResponseDTO getByNumeroDocumento(String numeroDocumento);

    AsociadosResponseDTO getByNit (String nit);

    AsociadosResponseDTO getByEmail(String email);

    Page<AsociadosResponseDTO> getByEstado(Boolean estado, Pageable pageable);

    Page<AsociadosResponseDTO> buscarGlobal(String filtro, Pageable pageable);

}
