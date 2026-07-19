package com.acoasmi.roble.controller;

import com.acoasmi.roble.dto.request.AsociadoDocumentosRequestDTO;
import com.acoasmi.roble.dto.response.AsociadoDocumentosResponseDTO;
import com.acoasmi.roble.entity.AsociadoDocumentos;
import com.acoasmi.roble.service.AsociadoDocumentosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/asociados-documentos")
@Tag(name = "Expediente Digital", description = "Endpoints para la gestión y almacenamiento de documentos de asociados")
public class AsociadoDocumentosController extends AcoasmiController<AsociadoDocumentos,
        AsociadoDocumentosRequestDTO, AsociadoDocumentosResponseDTO, Long>{

    private final AsociadoDocumentosService asociadoDocumentosService;

    public AsociadoDocumentosController(AsociadoDocumentosService asociadoDocumentosService) {
        super(asociadoDocumentosService, "Expediente Digital");
        this.asociadoDocumentosService = asociadoDocumentosService;
    }

    @PostMapping(
            value = "/subir/{numeroAsociado}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Subir un documento al expediente digital del asociado",
            description = "Permite cargar un archivo físico y clasificarlo enviando el archivo y la metadata por separado.",
            requestBody = @RequestBody(
                    content = @Content(
                            encoding = @Encoding(name = "metadata", contentType = MediaType.APPLICATION_JSON_VALUE)
                    )
            )
    )
    public ResponseEntity<AsociadoDocumentosResponseDTO> subirDocumento(
            @Parameter(description = "Número correlativo del asociado", example = "1850")
            @PathVariable Integer numeroAsociado,

            @RequestPart("metadata") @Valid AsociadoDocumentosRequestDTO requestDto,

            @Parameter(description = "Archivo físico digital a almacenar")
            @RequestPart("archivo") MultipartFile archivo
    ) {
        AsociadoDocumentosResponseDTO response = asociadoDocumentosService.subirDocumento(numeroAsociado, requestDto, archivo);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/asociado/{numeroAsociado}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Listar documentos por número de asociado",
            description = "Obtiene el historial de documentos activos cargados en el expediente del asociado utilizando su número correlativo."
    )
    public ResponseEntity<List<AsociadoDocumentosResponseDTO>> getByNumeroAsociado(
            @Parameter(description = "Número correlativo del asociado", example = "1850")
            @PathVariable Integer numeroAsociado
    ) {
        List<AsociadoDocumentosResponseDTO> documentos = asociadoDocumentosService.getByNumeroAsociado(numeroAsociado);
        return ResponseEntity.ok(documentos);
    }
}