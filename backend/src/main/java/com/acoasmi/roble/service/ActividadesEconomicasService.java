package com.acoasmi.roble.service;

import com.acoasmi.roble.dto.request.ActividadesEconomicasRequestDTO;
import com.acoasmi.roble.dto.response.ActividadesEconomicasResponseDTO;
import com.acoasmi.roble.entity.ActividadesEconomicas;

public interface ActividadesEconomicasService extends AcoasmiService <ActividadesEconomicas,
        ActividadesEconomicasRequestDTO, ActividadesEconomicasResponseDTO, Long>{

    ActividadesEconomicasResponseDTO getByCodigoMh(String codigoMh);

    ActividadesEconomicasResponseDTO getByDescripcion(String descripcion);

}
