package com.acoasmi.roble.service;

import com.acoasmi.roble.dto.request.CatalogoCuentasRequestDTO;
import com.acoasmi.roble.dto.response.CatalogoCuentasResponseDTO;
import java.util.List;

public interface CatalogoCuentasService extends AcoasmiService<Object, CatalogoCuentasRequestDTO, CatalogoCuentasResponseDTO, Long> {

    // Obtener el catálogo estructurado en forma de árbol (solo cuentas raíz con sus hijos anidados)
    List<CatalogoCuentasResponseDTO> getArbolContable();

    // Obtener únicamente las subcuentas que dependen directamente de una cuenta padre
    List<CatalogoCuentasResponseDTO> getSubCuentasPorPadre(Long idPadre);

    // Buscar una cuenta por su código estructurado (ej. "1011-01")
    CatalogoCuentasResponseDTO getByCodigoCuenta(String codigoCuenta);
}
