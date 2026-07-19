package com.acoasmi.roble.controller;
import com.acoasmi.roble.dto.request.DistritosRequestDTO;
import com.acoasmi.roble.dto.response.DistritosResponseDTO;
import com.acoasmi.roble.entity.Distritos;
import com.acoasmi.roble.service.DistritosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/distritos")
@Tag(name = "Distritos", description = "Endpoints para la gestión y consulta de los distritos")
public class DistritosController extends AcoasmiController<Distritos,
        DistritosRequestDTO, DistritosResponseDTO, Long> {

    private final DistritosService distritosService;

    public DistritosController(DistritosService distritosService) {
        super(distritosService, "Distritos");
        this.distritosService = distritosService;
    }

    @GetMapping("/buscar/codigo/{codigoDistrito}")
    @Operation(
            summary = "Buscar por código de distrito",
            description = "Busca los datos de un distrito utilizando su código de identificación."
    )
    public ResponseEntity<DistritosResponseDTO> getByCodigoDistrito(@PathVariable Integer codigoDistrito) {
        return ResponseEntity.ok(distritosService.getByCodigoDistrito(codigoDistrito));
    }

    @GetMapping("/buscar/nombre")
    @Operation(
            summary = "Buscar por nombre",
            description = "Busca distritos activos cuyo nombre contenga la cadena proporcionada."
    )
    public ResponseEntity<DistritosResponseDTO> getByNombreDistrito(@RequestParam String nombreDistrito) {
        return ResponseEntity.ok(distritosService.getByNombreDistrito(nombreDistrito));
    }
}