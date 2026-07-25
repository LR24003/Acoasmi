package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.TasasPrestamos;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TasasPrestamosRepository extends AcoasmiRepository<TasasPrestamos, Long> {

    Optional<TasasPrestamos> findFirstByTasaInteresAnualAndEstadoTrue(BigDecimal tasaInteresAnual);
}
