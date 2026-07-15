package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.PermisosRequestDTO;
import com.acoasmi.roble.dto.response.PermisosResponseDTO;
import com.acoasmi.roble.entity.Permisos;
import com.acoasmi.roble.repository.PermisosRepository;
import com.acoasmi.roble.service.PermisosService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PermisosServiceImpl extends AcoasmiServiceImpl<Permisos, PermisosRequestDTO,
        PermisosResponseDTO, Long> implements PermisosService {

    private final PermisosRepository permisosRepository;

    public PermisosServiceImpl(PermisosRepository permisosRepository) {
        super(permisosRepository);
        this.permisosRepository = permisosRepository;
    }

    @Override
    protected PermisosResponseDTO convertToResponseDto(Permisos entity) {
        if (entity == null) return null;

        return PermisosResponseDTO.builder()
                .id(entity.getId())
                .codigoPermiso(entity.getCodigoPermiso())
                .descripcion(entity.getDescripcion())
                .estado(entity.getEstado())
                .build();
    }

    @Override
    protected Permisos convertToEntity(PermisosRequestDTO dto) {
        if (dto == null) return null;

        Permisos permiso = new Permisos();
        permiso.setCodigoPermiso(dto.getCodigoPermiso());
        permiso.setDescripcion(dto.getDescripcion());
        permiso.setEstado(true);

        return permiso;
    }

    @Override
    protected void updateEntityFromDto(PermisosRequestDTO dto, Permisos entity) {
        if (dto == null || entity == null) return;

        entity.setCodigoPermiso(dto.getCodigoPermiso());
        entity.setDescripcion(dto.getDescripcion());

    }

    @Override
    @Transactional(readOnly = true)
    public PermisosResponseDTO getByCodigoPermiso(String codigoPermiso) {
        Permisos entity = permisosRepository.findByCodigoPermisoAndEstadoTrue(codigoPermiso)
                .orElseThrow(() -> new RuntimeException("Permiso activo no encontrado con el código: " + codigoPermiso));
        return convertToResponseDto(entity);
    }
}