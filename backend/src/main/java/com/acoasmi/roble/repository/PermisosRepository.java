package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.Permisos;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PermisosRepository extends AcoasmiRepository<Permisos, Long> {

    Optional<Permisos> findByCodigoPermisoAndEstadoTrue(String codigoPermiso);

}
