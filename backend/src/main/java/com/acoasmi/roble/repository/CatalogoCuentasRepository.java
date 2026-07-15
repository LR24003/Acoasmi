package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.CatalogoCuentas;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CatalogoCuentasRepository extends AcoasmiRepository<CatalogoCuentas, Long> {

    Optional<CatalogoCuentas> findByCodigoCuentaAndEstadoTrue(String codigoCuenta);

    // Obtener todas las cuentas principales (Nivel 1 / Sin Padre) para armar el árbol desde la raíz
    List<CatalogoCuentas> findByCuentaPadreIsNullAndEstadoTrue();

    // Obtener las subcuentas que dependen de un padre específico
    List<CatalogoCuentas> findByCuentaPadreIdAndEstadoTrue(Long idPadre);
}
