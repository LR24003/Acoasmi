package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.entity.Usuarios;
import com.acoasmi.roble.repository.UsuariosRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuariosRepository usuariosRepository;

    public UserDetailsServiceImpl(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuarios usuario = usuariosRepository.findByUsuarioIgnoreCaseAndEstadoTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado o inactivo: " + username));

        String nombreRol = usuario.getRol() != null ? usuario.getRol().getNombreRol() : "USUARIO";

        if (!nombreRol.startsWith("ROLE_")) {
            nombreRol = "ROLE_" + nombreRol;
        }

        return User.builder()
                .username(usuario.getUsuario())
                .password(usuario.getPassword())
                .disabled(!usuario.getEstado())
                .roles(nombreRol.replace("ROLE_", ""))
                .build();
    }
}
