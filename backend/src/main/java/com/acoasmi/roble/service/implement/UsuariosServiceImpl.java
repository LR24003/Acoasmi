package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.UsuariosRequestDTO;
import com.acoasmi.roble.dto.response.UsuariosResponseDTO;
import com.acoasmi.roble.entity.Roles;
import com.acoasmi.roble.entity.Usuarios;
import com.acoasmi.roble.repository.RolesRepository;
import com.acoasmi.roble.repository.UsuariosRepository;
import com.acoasmi.roble.service.UsuariosService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuariosServiceImpl
        extends AcoasmiServiceImpl<Usuarios, UsuariosRequestDTO, UsuariosResponseDTO, Long>
        implements UsuariosService {

    private final UsuariosRepository usuariosRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuariosServiceImpl(UsuariosRepository usuariosRepository,
                               RolesRepository rolesRepository,
                               PasswordEncoder passwordEncoder) {
        super(usuariosRepository, Usuarios.class);
        this.usuariosRepository = usuariosRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional(readOnly = true)
    public UsuariosResponseDTO getByUsuario(String usuario) {
        Usuarios entity = usuariosRepository.findByUsuarioIgnoreCaseAndEstadoTrue(usuario)
                .orElseThrow(() -> new RuntimeException("Usuario activo no encontrado con el usuario: " + usuario));
        return mapToResponseDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuariosResponseDTO> getByEstado(Boolean estado) {
        return usuariosRepository.findByEstado(estado).stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LocalDateTime obtenerUltimoAcceso(String usuario) {
        Usuarios entity = usuariosRepository.findByUsuarioIgnoreCaseAndEstadoTrue(usuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o inactivo: " + usuario));

        return entity.getUltimoAcceso();
    }


    @Override
    protected void mapearDtoAEntidad(UsuariosRequestDTO dto, Usuarios entity) {
        if (dto == null || entity == null) return;

        entity.setUsuario(dto.getUsuario());
        entity.setNombres(dto.getNombres());
        entity.setApellidos(dto.getApellidos());
        entity.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (entity.getId() == null) {
            entity.setEstado(true);
        }

        if (dto.getIdRol() != null) {
            if (entity.getRoles() == null || !entity.getRoles().getId().equals(dto.getIdRol())) {
                Roles rol = rolesRepository.findById(dto.getIdRol())
                        .orElseThrow(() -> new RuntimeException("El Rol especificado no existe con ID: " + dto.getIdRol()));
                entity.setRoles(rol);
            }
        }
    }

    @Override
    protected UsuariosResponseDTO mapToResponseDTO(Usuarios entity) {
        if (entity == null) return null;

        return UsuariosResponseDTO.builder()
                .id(entity.getId())
                .usuario(entity.getUsuario())
                .nombres(entity.getNombres())
                .apellidos(entity.getApellidos())
                .email(entity.getEmail())
                .fechaCreacion(entity.getFechaCreacion())
                .ultimoAcceso(entity.getUltimoAcceso())
                .estado(entity.getEstado())
                .nombreRol(entity.getRoles() != null ? entity.getRoles().getNombreRol() : null)
                .build();
    }
}