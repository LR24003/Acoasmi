package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.Permisos;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PermisosRepository extends AcoasmiRepository<Permisos, Long> {

    Optional<Permisos> findByCodigoPermisoAndEstadoTrue(String codigoPermiso);

    Set<Permisos> findByCodigoPermisoInAndEstadoTrue(Collection<String> codigoPermiso);

}
