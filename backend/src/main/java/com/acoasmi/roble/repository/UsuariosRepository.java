package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.Usuarios;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuariosRepository extends AcoasmiRepository<Usuarios, Long> {

    // Buscar usuario por su username para el proceso de autenticación
    Optional<Usuarios> findByUsuarioContainingIgnoreCase(String usuario);

    Optional<Usuarios> findByActivo(Boolean activo);

}