package com.acoasmi.roble.controller;

import com.acoasmi.roble.dto.request.UsuariosRequestDTO;
import com.acoasmi.roble.dto.response.UsuariosResponseDTO;
import com.acoasmi.roble.entity.Usuarios;
import com.acoasmi.roble.service.UsuariosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Controlador para la gestión de usuarios del sistema y seguridad")
public class UsuariosController extends AcoasmiController<Usuarios, UsuariosRequestDTO, UsuariosResponseDTO, Long> {

    private final UsuariosService usuariosService;

    public UsuariosController(UsuariosService usuariosService) {
        super(usuariosService, "Usuarios");
        this.usuariosService = usuariosService;
    }

    @GetMapping("/buscar")
    @Operation(
            summary = "Buscar usuario por username",
            description = "Retorna los datos de un usuario mediante una coincidencia de su nombre de usuario."
    )
    public ResponseEntity<UsuariosResponseDTO> getByUsuario(@RequestParam String usuario) {
        UsuariosResponseDTO response = usuariosService.getByUsuario(usuario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/estado")
    @Operation(
            summary = "Filtrar usuarios por estado",
            description = "Retorna un listado de usuarios filtrados según estén activos (true) o inactivos (false)."
    )
    public ResponseEntity<List<UsuariosResponseDTO>> getByEstado(@RequestParam Boolean estado) {
        List<UsuariosResponseDTO> usuarios = usuariosService.getByEstado(estado);
        return ResponseEntity.ok(usuarios);
    }

    @PatchMapping("/ultimo-acceso")
    @Operation(
            summary = "Actualizar último acceso del usuario",
            description = "Registra la fecha y hora de la última sesión exitosa del usuario en el sistema."
    )
    public ResponseEntity<Void> actualizarUltimoAcceso(@RequestParam String username) {
        usuariosService.actualizarUltimoAcceso(username);
        return ResponseEntity.ok().build();
    }
}