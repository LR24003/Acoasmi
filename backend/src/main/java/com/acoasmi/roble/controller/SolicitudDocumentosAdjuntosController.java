package com.acoasmi.roble.controller;

import com.acoasmi.roble.dto.request.CreditoDocumentosAdjuntosRequestDTO;
import com.acoasmi.roble.dto.response.CreditoDocumentosAdjuntosResponseDTO;
import com.acoasmi.roble.entity.CreditoDocumentosAdjuntos;
import com.acoasmi.roble.service.SolicitudDocumentosAdjuntosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/creditos-documentos")
@Tag(name = "Solicitudes Crédito Documentos", description = "Endpoints para la gestión y subida de documentos adjuntos en solicitudes de crédito")
public class SolicitudDocumentosAdjuntosController extends AcoasmiController<
        CreditoDocumentosAdjuntos,
        CreditoDocumentosAdjuntosRequestDTO,
        CreditoDocumentosAdjuntosResponseDTO,
        Long> {

    private final SolicitudDocumentosAdjuntosService solicitudDocumentosAdjuntosService;

    public SolicitudDocumentosAdjuntosController(SolicitudDocumentosAdjuntosService documentosAdjuntosService) {
        super(documentosAdjuntosService, "Documentos Adjuntos");
        this.solicitudDocumentosAdjuntosService = documentosAdjuntosService;
    }

    @PostMapping(value = "/adjuntar/{idSolicitud}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Adjuntar archivo a una solicitud de crédito",
            description = "Recibe el ID de la solicitud, el DTO con el tipo de documento y el archivo físico a almacenar."
    )
    public ResponseEntity<CreditoDocumentosAdjuntosResponseDTO> adjuntarDocumento(
            @PathVariable Long idSolicitud,
            @RequestPart("dto") @Valid CreditoDocumentosAdjuntosRequestDTO dto,
            @RequestPart("archivo") MultipartFile archivo) {

        CreditoDocumentosAdjuntosResponseDTO response = solicitudDocumentosAdjuntosService.adjuntarDocumento(idSolicitud, dto, archivo);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
