package com.acoasmi.roble.controller;

import com.acoasmi.roble.dto.request.PermisosRequestDTO;
import com.acoasmi.roble.dto.response.PermisosResponseDTO;
import com.acoasmi.roble.entity.Permisos;
import com.acoasmi.roble.service.PermisosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permisos")
@Tag(name = "Permisos", description = "Controlador para la gestión de permisos individuales del sistema")
public class PermisosController extends AcoasmiController<Permisos, PermisosRequestDTO, PermisosResponseDTO, Long> {

    private final PermisosService permisosService;

    public PermisosController(PermisosService permisosService) {
        super(permisosService, "Permisos");
        this.permisosService = permisosService;
    }

    @GetMapping("/buscar-codigo")
    @Operation(
            summary = "Buscar permiso por su código único",
            description = "Retorna la información de un permiso buscando por su código exacto (ej. 'CREAR_USUARIO')."
    )
    public ResponseEntity<PermisosResponseDTO> getByCodigoPermiso(@RequestParam String codigoPermiso) {
        PermisosResponseDTO permiso = permisosService.getByCodigoPermiso(codigoPermiso);
        return ResponseEntity.ok(permiso);
    }
}
