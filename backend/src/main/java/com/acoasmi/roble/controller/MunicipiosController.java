package com.acoasmi.roble.controller;

import com.acoasmi.roble.dto.request.MunicipiosRequestDTO;
import com.acoasmi.roble.dto.response.MunicipiosResponseDTO;
import com.acoasmi.roble.entity.Municipios;
import com.acoasmi.roble.service.MunicipiosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/municipios")
@Tag(name = "Municipios", description = "Endpoints para la gestión, registro y consulta de municipios")
public class MunicipiosController extends AcoasmiController<Municipios,
        MunicipiosRequestDTO, MunicipiosResponseDTO, Long> {

    private final MunicipiosService municipiosService;

    public MunicipiosController(MunicipiosService municipiosService) {
        super(municipiosService, "Municipios");
        this.municipiosService = municipiosService;
    }

    @GetMapping("/buscar/codigo/{codigoMunicipio}")
    @Operation(
            summary = "Buscar por código de municipio",
            description = "Busca la información de un municipio usando su código geopolítico único."
    )
    public ResponseEntity<MunicipiosResponseDTO> getByCodigoMunicipio(@PathVariable Integer codigoMunicipio) {
        return ResponseEntity.ok(municipiosService.getByCodigoMunicipio(codigoMunicipio));
    }

    @GetMapping("/buscar/nombre")
    @Operation(
            summary = "Buscar por nombre",
            description = "Permite buscar municipios activos cuyo nombre coincida de manera parcial o total con el texto proporcionado."
    )
    public ResponseEntity<MunicipiosResponseDTO> getByNombreMunicipio(@RequestParam String nombreMunicipio) {
        return ResponseEntity.ok(municipiosService.getByNombreMunicipio(nombreMunicipio));
    }
}
