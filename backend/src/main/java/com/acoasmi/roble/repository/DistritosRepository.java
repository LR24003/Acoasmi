package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.Distritos;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DistritosRepository extends AcoasmiRepository<Distritos, Long> {

    Optional<Distritos> findByCodigoDistrito(String codigoDistrito);

    Optional<Distritos> findByNombreContainingIgnoreCase(String nombre);

}
