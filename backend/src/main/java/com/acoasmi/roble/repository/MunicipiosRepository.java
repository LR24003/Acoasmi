package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.Municipios;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MunicipiosRepository extends AcoasmiRepository<Municipios, Long> {

    Optional<Municipios> findByCodigoMunicipio(String codigoMunicipio);

    Optional<Municipios> findByNombreContainingIgnoreCaseAndEstadoTrue(String nombre);
}
