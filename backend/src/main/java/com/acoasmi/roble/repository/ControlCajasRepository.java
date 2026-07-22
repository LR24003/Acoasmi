package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.ControlCajas;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ControlCajasRepository extends AcoasmiRepository<ControlCajas, Long> {

    Optional<ControlCajas> findByUsuarioCajeroIdAndFechaCierreIsNullAndEstadoTrue(Long Usuario);

    Optional<ControlCajas> findByUsuarioCajero_Usuario(String usuario);

    Optional<ControlCajas> findByIdAndEstadoTrue(Long idSesionCaja);

}