package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.Roles;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RolesRepository extends AcoasmiRepository<Roles, Long> {

    Optional<Roles> findByNombreRol(String nombreRol);

}