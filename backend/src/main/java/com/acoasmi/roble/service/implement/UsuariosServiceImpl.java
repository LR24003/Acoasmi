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
public class UsuariosServiceImpl extends AcoasmiServiceImpl<Usuarios, UsuariosRequestDTO, UsuariosResponseDTO, Long> implements UsuariosService {

    private final UsuariosRepository usuariosRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuariosServiceImpl(UsuariosRepository usuariosRepository,
                               RolesRepository rolesRepository,
                               PasswordEncoder passwordEncoder) {
        super(usuariosRepository);
        this.usuariosRepository = usuariosRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected UsuariosResponseDTO convertToResponseDto(Usuarios entity) {
        if (entity == null) return null;

        return UsuariosResponseDTO.builder()
                .id(entity.getId())
                .usuario(entity.getUsuario())
                .nombres(entity.getNombres())
                .apellidos(entity.getApellidos())
                .email(entity.getEmail())
                .fechaCreacion(entity.getFechaCreacion())
                .ultimoAcceso(entity.getUltimoAcceso())
                .activo(entity.getActivo())
                .nombreRol(entity.getRoles() != null ? entity.getRoles().getNombreRol() : null)
                .build();
    }

    @Override
    protected Usuarios convertToEntity(UsuariosRequestDTO dto) {
        if (dto == null) return null;

        Usuarios usuario = new Usuarios();
        usuario.setUsuario(dto.getUsuario());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setNombres(dto.getNombres());
        usuario.setApellidos(dto.getApellidos());
        usuario.setEmail(dto.getEmail());
        usuario.setActivo(true);

        if (dto.getIdRol() != null) {
            Roles rol = rolesRepository.findById(dto.getIdRol())
                    .orElseThrow(() -> new RuntimeException("El Rol especificado no existe con ID: " + dto.getIdRol()));
            usuario.setRoles(rol);
        }

        return usuario;
    }

    @Override
    protected void updateEntityFromDto(UsuariosRequestDTO dto, Usuarios entity) {
        if (dto == null || entity == null) return;

        entity.setUsuario(dto.getUsuario());
        entity.setNombres(dto.getNombres());
        entity.setApellidos(dto.getApellidos());
        entity.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getIdRol() != null && (entity.getRoles() == null || !entity.getRoles().getId().equals(dto.getIdRol()))) {
            Roles rol = rolesRepository.findById(dto.getIdRol())
                    .orElseThrow(() -> new RuntimeException("El Rol especificado no existe con ID: " + dto.getIdRol()));
            entity.setRoles(rol);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UsuariosResponseDTO getByUsuario(String usuario) {
        Usuarios entity = usuariosRepository.findByUsuarioContainingIgnoreCase(usuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el usuario: " + usuario));
        return convertToResponseDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuariosResponseDTO> getByActivo(Boolean activo) {
        return usuariosRepository.findByActivo(activo).stream()
                .map(this::convertToResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public void actualizarUltimoAcceso(String username) {
        Usuarios usuario = usuariosRepository.findByUsuarioContainingIgnoreCase(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el username: " + username));
        usuario.setUltimoAcceso(LocalDateTime.now());
        usuariosRepository.save(usuario);
    }
}