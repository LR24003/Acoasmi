package com.acoasmi.roble.controller;

import com.acoasmi.roble.dto.request.CumplimientoPerfilRiesgoRequestDTO;
import com.acoasmi.roble.dto.response.CumplimientoPerfilRiesgoResponseDTO;
import com.acoasmi.roble.entity.CumplimientoPerfilRiesgo;
import com.acoasmi.roble.service.CumplimientoPerfilRiesgoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cumplimiento-riesgos")
@Tag(name = "Cumplimiento LDA - Perfiles de Riesgo", description = "Endpoints para la auditoría, análisis y actualización del perfil de riesgo de los asociados")
public class CumplimientoPerfilRiesgoController extends AcoasmiController<CumplimientoPerfilRiesgo,
        CumplimientoPerfilRiesgoRequestDTO, CumplimientoPerfilRiesgoResponseDTO, Long> {

    private final CumplimientoPerfilRiesgoService cumplimientoService;

    public CumplimientoPerfilRiesgoController(CumplimientoPerfilRiesgoService cumplimientoService) {
        super(cumplimientoService, "Perfiles de Riesgo");
        this.cumplimientoService = cumplimientoService;
    }

    @GetMapping("/asociado/{nivelRiesgo}")
    @Operation(
            summary = "Obtener perfil por ID nivel de riesgo del asociado",
            description = "Recupera la declaración de origen de fondos y nivel de riesgo de un asociado según normativas de prevención de lavado.")
    public ResponseEntity<List<CumplimientoPerfilRiesgoResponseDTO>> getByNivelRiesgo(@PathVariable String nivelRiesgo) {
        return ResponseEntity.ok(cumplimientoService.getByNivelRiesgo(nivelRiesgo));
    }
}
