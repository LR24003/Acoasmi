package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.PermisosRequestDTO;
import com.acoasmi.roble.dto.response.PermisosResponseDTO;
import com.acoasmi.roble.entity.Permisos;
import com.acoasmi.roble.repository.PermisosRepository;
import com.acoasmi.roble.service.PermisosService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PermisosServiceImpl
        extends AcoasmiServiceImpl<Permisos, PermisosRequestDTO, PermisosResponseDTO, Long>
        implements PermisosService {

    private final PermisosRepository permisosRepository;

    public PermisosServiceImpl(PermisosRepository permisosRepository) {
        super(permisosRepository, Permisos.class);
        this.permisosRepository = permisosRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PermisosResponseDTO getByCodigoPermiso(String codigoPermiso) {
        Permisos entity = permisosRepository.findByCodigoPermisoAndEstadoTrue(codigoPermiso)
                .orElseThrow(() -> new RuntimeException("Permiso activo no encontrado con el código: " + codigoPermiso));
        return mapToResponseDTO(entity);
    }


    @Override
    protected void mapearDtoAEntidad(PermisosRequestDTO dto, Permisos entity) {
        if (dto == null || entity == null) return;

        entity.setCodigoPermiso(dto.getCodigoPermiso());
        entity.setDescripcion(dto.getDescripcion());

        if (entity.getId() == null) {
            entity.setEstado(true);
        }
    }

    @Override
    protected PermisosResponseDTO mapToResponseDTO(Permisos entity) {
        if (entity == null) return null;

        return PermisosResponseDTO.builder()
                .id(entity.getId())
                .codigoPermiso(entity.getCodigoPermiso())
                .descripcion(entity.getDescripcion())
                .estado(entity.getEstado())
                .build();
    }
}