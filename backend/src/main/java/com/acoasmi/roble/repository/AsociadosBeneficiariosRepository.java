package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.AsociadosBeneficiarios;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsociadosBeneficiariosRepository extends AcoasmiRepository<AsociadosBeneficiarios, Long> {

    List<AsociadosBeneficiarios> findByCuentaNumeroCuenta(String numeroCuenta);

    List<AsociadosBeneficiarios> findByAsociadoNumeroAsociado(Integer numeroAsociado);
}
