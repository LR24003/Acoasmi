package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.RolesRequestDTO;
import com.acoasmi.roble.dto.response.PermisosResponseDTO;
import com.acoasmi.roble.dto.response.RolesResponseDTO;
import com.acoasmi.roble.entity.Permisos;
import com.acoasmi.roble.entity.Roles;
import com.acoasmi.roble.repository.PermisosRepository;
import com.acoasmi.roble.repository.RolesRepository;
import com.acoasmi.roble.service.RolesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RolesServiceImpl extends AcoasmiServiceImpl<Roles, RolesRequestDTO,
        RolesResponseDTO, Long> implements RolesService {

    private final RolesRepository rolesRepository;
    private final PermisosRepository permisosRepository;

    public RolesServiceImpl(RolesRepository rolesRepository, PermisosRepository permisosRepository) {
        super(rolesRepository);
        this.rolesRepository = rolesRepository;
        this.permisosRepository = permisosRepository;
    }

    @Override
    protected RolesResponseDTO convertToResponseDto(Roles entity) {
        if (entity == null) return null;

        // Mapeo de Entidad a DTO
        Set<PermisosResponseDTO> permisosDto = new HashSet<>();
        if (entity.getPermisos() != null) {
            permisosDto = entity.getPermisos().stream()
                    .filter(permiso -> Boolean.TRUE.equals(permiso.getEstado()))
                    .map(permiso -> PermisosResponseDTO.builder()
                            .id(permiso.getId())
                            .codigoPermiso(permiso.getCodigoPermiso())
                            .descripcion(permiso.getDescripcion())
                            .estado(permiso.getEstado())
                            .build())
                    .collect(Collectors.toSet());
        }

        return RolesResponseDTO.builder()
                .id(entity.getId())
                .nombreRol(entity.getNombreRol())
                .descripcion(entity.getDescripcion())
                .estado(entity.getEstado())
                .permisos(permisosDto)
                .build();
    }

    // Mapeo de DTO a Entidad
    @Override
    protected Roles convertToEntity(RolesRequestDTO dto) {
        if (dto == null) return null;

        Roles rol = new Roles();
        rol.setNombreRol(dto.getNombreRol());
        rol.setDescripcion(dto.getDescripcion());
        rol.setEstado(true);

        if (dto.getPermisosIds() != null && !dto.getPermisosIds().isEmpty()) {
            Set<Permisos> permisos = new HashSet<>(permisosRepository.findAllById(dto.getPermisosIds()));
            rol.setPermisos(permisos);
        }

        return rol;
    }

    @Override
    protected void updateEntityFromDto(RolesRequestDTO dto, Roles entity) {
        if (dto == null || entity == null) return;

        entity.setNombreRol(dto.getNombreRol());
        entity.setDescripcion(dto.getDescripcion());

        if (dto.getPermisosIds() != null) {
            Set<Permisos> nuevosPermisos = new HashSet<>(permisosRepository.findAllById(dto.getPermisosIds()));
            entity.getPermisos().clear();
            entity.getPermisos().addAll(nuevosPermisos);
        } else {
            entity.getPermisos().clear();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public RolesResponseDTO getByNombreRol(String nombreRol) {
        Roles entity = rolesRepository.findByNombreRolAndEstadoTrue(nombreRol)
                .orElseThrow(() -> new RuntimeException("Rol activo no encontrado con el nombre: " + nombreRol));
        return convertToResponseDto(entity);
    }
}