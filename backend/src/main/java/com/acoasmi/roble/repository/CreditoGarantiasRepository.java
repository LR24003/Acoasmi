package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.CreditoGarantias;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CreditoGarantiasRepository extends AcoasmiRepository<CreditoGarantias, Long>{

    List<CreditoGarantias> findByTipoGarantiaContainingIgnoreCase(String tipoGarantia);
}
