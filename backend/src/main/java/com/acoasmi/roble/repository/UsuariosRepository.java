package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.Usuarios;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuariosRepository extends AcoasmiRepository<Usuarios, Long> {

    Optional<Usuarios> findByUsuarioIgnoreCaseAndEstadoTrue(String usuario);

    List<Usuarios> findByEstado(Boolean estado);

}