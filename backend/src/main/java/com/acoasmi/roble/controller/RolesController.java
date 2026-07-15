package com.acoasmi.roble.controller;

import com.acoasmi.roble.dto.request.RolesRequestDTO;
import com.acoasmi.roble.dto.response.RolesResponseDTO;
import com.acoasmi.roble.entity.Roles;
import com.acoasmi.roble.service.RolesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Roles", description = "Controlador para la administración de roles y sus respectivos permisos de acceso")
public class RolesController extends AcoasmiController<Roles, RolesRequestDTO,
        RolesResponseDTO, Long> {

    private final RolesService rolesService;

    public RolesController(RolesService rolesService) {
        super(rolesService, "Roles");
        this.rolesService = rolesService;
    }

    @GetMapping("/buscar-rol")
    @Operation(
            summary = "Buscar un rol por su nombre único",
            description = "Retorna la información detallada de un rol (incluyendo todos sus permisos) buscando por su nombre exacto."
    )
    public ResponseEntity<RolesResponseDTO> getByNombreRol(@RequestParam String nombreRol) {
        RolesResponseDTO rol = rolesService.getByNombreRol(nombreRol);
        return ResponseEntity.ok(rol);
    }
}