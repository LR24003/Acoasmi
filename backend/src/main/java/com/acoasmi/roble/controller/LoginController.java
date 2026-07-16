package com.acoasmi.roble.controller;

import com.acoasmi.roble.dto.request.LoginRequestDTO;
import com.acoasmi.roble.dto.request.RecoverPasswordRequestDTO;
import com.acoasmi.roble.dto.request.UsuariosRequestDTO;
import com.acoasmi.roble.dto.response.LoginResponseDTO;
import com.acoasmi.roble.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación y Accesos Públicos", description = "Controlador para login, registro y recuperación de cuentas")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario mediante credenciales y retorna un token JWT.")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(loginService.login(request));
    }

    @PostMapping("/recover-password")
    @Operation(
            summary = "Recuperar contraseña",
            description = "Solicita el restablecimiento de clave enviando un identificador de cuenta."
    )
    public ResponseEntity<Void> recoverPassword(@Valid @RequestBody RecoverPasswordRequestDTO request) {
        loginService.recoverPassword(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/registro")
    @Operation(
            summary = "Registro público de asociados",
            description = "Permite el auto-registro de nuevos asociados en el sistema."
    )
    public ResponseEntity<Void> register(@Valid @RequestBody UsuariosRequestDTO request) {
        loginService.register(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}