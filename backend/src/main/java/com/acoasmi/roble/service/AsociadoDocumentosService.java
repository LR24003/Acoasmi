package com.acoasmi.roble.service;

import com.acoasmi.roble.dto.request.AsociadoDocumentosRequestDTO;
import com.acoasmi.roble.dto.response.AsociadoDocumentosResponseDTO;
import com.acoasmi.roble.entity.AsociadoDocumentos;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface AsociadoDocumentosService extends AcoasmiService<AsociadoDocumentos,
        AsociadoDocumentosRequestDTO, AsociadoDocumentosResponseDTO, Long> {

    AsociadoDocumentosResponseDTO subirDocumento(Integer numeroAsociado,
                                                 AsociadoDocumentosRequestDTO requestDto,
                                                 MultipartFile archivo);


    List<AsociadoDocumentosResponseDTO> getByNumeroAsociado(Integer numeroAsociado);


}