package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.Departamentos;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DepartamentosRepository extends AcoasmiRepository<Departamentos, Long> {

    Optional<Departamentos> findByCodigoDepartamento(Integer codigoDepartamento);

    Optional<Departamentos> findByNombreDepartamentoContainingIgnoreCaseAndEstadoTrue(String nombreDepartamento);

}
