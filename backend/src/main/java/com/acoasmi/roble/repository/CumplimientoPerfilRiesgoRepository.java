package com.acoasmi.roble.repository;


import com.acoasmi.roble.entity.CumplimientoPerfilRiesgo;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CumplimientoPerfilRiesgoRepository extends AcoasmiRepository<CumplimientoPerfilRiesgo, Long> {

    Optional<CumplimientoPerfilRiesgo> findByNivelRiesgoContainingIgnoreCase(String nivelRiesgo);
}