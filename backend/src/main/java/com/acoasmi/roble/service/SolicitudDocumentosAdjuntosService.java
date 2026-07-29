package com.acoasmi.roble.service;

import com.acoasmi.roble.dto.request.CreditoDocumentosAdjuntosRequestDTO;
import com.acoasmi.roble.dto.response.CreditoDocumentosAdjuntosResponseDTO;
import com.acoasmi.roble.entity.CreditoDocumentosAdjuntos;
import org.springframework.web.multipart.MultipartFile;

public interface SolicitudDocumentosAdjuntosService extends AcoasmiService<CreditoDocumentosAdjuntos,
        CreditoDocumentosAdjuntosRequestDTO, CreditoDocumentosAdjuntosResponseDTO, Long >{

    CreditoDocumentosAdjuntosResponseDTO adjuntarDocumento(Long idSolicitud,
                                                           CreditoDocumentosAdjuntosRequestDTO dto,
                                                           MultipartFile archivo);
}
