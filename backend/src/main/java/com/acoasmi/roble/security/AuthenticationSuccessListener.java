package com.acoasmi.roble.security;

import com.acoasmi.roble.service.UsuariosService;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener {

    private final UsuariosService usuariosService;

    public AuthenticationSuccessListener(UsuariosService usuariosService) {
        this.usuariosService = usuariosService;
    }

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            String usuario = userDetails.getUsername();
            usuariosService.obtenerUltimoAcceso(usuario);
        }
    }
}
