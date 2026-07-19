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
public class RolesServiceImpl
        extends AcoasmiServiceImpl<Roles, RolesRequestDTO, RolesResponseDTO, Long>
        implements RolesService {

    private final RolesRepository rolesRepository;
    private final PermisosRepository permisosRepository;

    public RolesServiceImpl(RolesRepository rolesRepository, PermisosRepository permisosRepository) {
        super(rolesRepository, Roles.class);
        this.rolesRepository = rolesRepository;
        this.permisosRepository = permisosRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public RolesResponseDTO getByNombreRol(String rol) {
        Roles entity = rolesRepository.findByNombreRolIgnoreCaseAndEstadoTrue(rol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con el nombre: " + rol));
        return mapToResponseDTO(entity);
    }

    @Override
    protected void mapearDtoAEntidad(RolesRequestDTO dto, Roles entity) {
        if (dto == null || entity == null) return;

        entity.setNombreRol(dto.getRol());
        entity.setDescripcion(dto.getDescripcion());

        if (entity.getId() == null) {
            entity.setEstado(true);
        }

        if (dto.getPermisos() != null && !dto.getPermisos().isEmpty()) {

            Set<Permisos> nuevosPermisos = permisosRepository.findByCodigoPermisoInAndEstadoTrue(dto.getPermisos());

            if (nuevosPermisos.size() != dto.getPermisos().size()) {
                throw new RuntimeException("Uno o más permisos especificados no existen o se encuentran inactivos");
            }

            if (entity.getPermisos() == null) {
                entity.setPermisos(new HashSet<>());
            }

            entity.getPermisos().clear();
            entity.getPermisos().addAll(nuevosPermisos);
        } else {
            if (entity.getPermisos() != null) {
                entity.getPermisos().clear();
            }
        }
    }

    @Override
    protected RolesResponseDTO mapToResponseDTO(Roles entity) {
        if (entity == null) return null;

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
                .rol(entity.getNombreRol())
                .descripcion(entity.getDescripcion())
                .estado(entity.getEstado())
                .permisos(permisosDto)
                .build();
    }
}