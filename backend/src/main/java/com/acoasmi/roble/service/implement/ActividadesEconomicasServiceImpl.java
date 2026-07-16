package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.ActividadesEconomicasRequestDTO;
import com.acoasmi.roble.dto.response.ActividadesEconomicasResponseDTO;
import com.acoasmi.roble.entity.ActividadesEconomicas;
import com.acoasmi.roble.repository.ActividadesEconomicasRepository;
import com.acoasmi.roble.service.ActividadesEconomicasService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActividadesEconomicasServiceImpl extends AcoasmiServiceImpl<ActividadesEconomicas,
        ActividadesEconomicasRequestDTO, ActividadesEconomicasResponseDTO, Long>
        implements ActividadesEconomicasService {

    private final ActividadesEconomicasRepository actividadesEconomicasRepository;

    public ActividadesEconomicasServiceImpl(ActividadesEconomicasRepository actividadesEconomicasRepository) {
        super(actividadesEconomicasRepository, ActividadesEconomicas.class);
        this.actividadesEconomicasRepository = actividadesEconomicasRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public ActividadesEconomicasResponseDTO getByCodigoMh(String codigoMh) {
        return actividadesEconomicasRepository.findByCodigoMh(codigoMh)
                .map(this::mapToResponseDTO)
                .orElseThrow(() -> new RuntimeException("No se encontró actividad económica con ese código: " + codigoMh));
    }

    @Override
    @Transactional(readOnly = true)
    public ActividadesEconomicasResponseDTO getByDescripcion(String descripcion) {
        return actividadesEconomicasRepository.findByDescripcionContainingIgnoreCase(descripcion)
                .map(this::mapToResponseDTO)
                .orElseThrow(() -> new RuntimeException("No se encontró actividad económica con esa descripción: " + descripcion));
    }


    @Override
    protected void mapearDtoAEntidad(ActividadesEconomicasRequestDTO dto, ActividadesEconomicas entity) {
        if (dto == null || entity == null) return;

        entity.setCodigoMh(dto.getCodigoMh());
        entity.setDescripcion(dto.getDescripcion());

        if (entity.getId() == null) {
            entity.setEstado(true);
        }
    }

    @Override
    protected ActividadesEconomicasResponseDTO mapToResponseDTO(ActividadesEconomicas entity) {
        if (entity == null) return null;

        return ActividadesEconomicasResponseDTO.builder()
                .id(entity.getId())
                .codigoMh(entity.getCodigoMh())
                .descripcion(entity.getDescripcion())
                .estado(entity.getEstado())
                .build();
    }
}