package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.Permisos;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PermisosRepository extends AcoasmiRepository<Permisos, Long> {

    // Buscar permiso por su código (ej. "USUARIOS_CREAR")
    Optional<Permisos> findByCodigoPermiso(String codigoPermiso);

    // Verificar si ya existe el código del permiso
    Boolean existsByCodigoPermiso(String codigoPermiso);
}
