package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.ActividadesEconomicas;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActividadesEconomicasRepository extends AcoasmiRepository<ActividadesEconomicas, Long> {

    Optional<ActividadesEconomicas> findByCodigoMh(String codigoMh);

    Optional<ActividadesEconomicas> findByDescripcionContainingIgnoreCase(String descripcion);

}
