package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.AsociadoCuentas;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsociadoCuentasRepository extends AcoasmiRepository<AsociadoCuentas, Long> {

    Optional<AsociadoCuentas> findByNumeroCuenta(String numeroCuenta);

    List<AsociadoCuentas> findByAsociadoNumeroAsociado(Integer numeroAsociado);

    List<AsociadoCuentas> findByAsociadoNumeroAsociadoAndPlazoDiasContaining(Integer numeroAsociado, String plazoDias);

}