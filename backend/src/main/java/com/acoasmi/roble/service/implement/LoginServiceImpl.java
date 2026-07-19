package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.LoginRequestDTO;
import com.acoasmi.roble.dto.request.RecoverPasswordRequestDTO;
import com.acoasmi.roble.dto.request.UsuariosRequestDTO;
import com.acoasmi.roble.dto.response.LoginResponseDTO;
import com.acoasmi.roble.entity.Usuarios;
import com.acoasmi.roble.repository.UsuariosRepository;
import com.acoasmi.roble.security.JwtUtil;
import com.acoasmi.roble.service.LoginService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UsuariosRepository usuariosRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public LoginServiceImpl(AuthenticationManager authenticationManager,
                            UserDetailsService userDetailsService,
                            UsuariosRepository usuariosRepository,
                            JwtUtil jwtUtil,
                            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.usuariosRepository = usuariosRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public LoginResponseDTO login(LoginRequestDTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsuario(),
                        request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsuario());
        String jwt = jwtUtil.generateToken(userDetails);

        Usuarios usuarioEntity = usuariosRepository.findByUsuarioIgnoreCaseAndEstadoTrue(request.getUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario o contraseña incorrecta, verifique los datos ingresados."));

        this.actualizarUltimoAcceso(request.getUsuario());

        return new LoginResponseDTO(
                jwt,
                usuarioEntity.getUsuario(),
                usuarioEntity.getNombres(),
                usuarioEntity.getApellidos(),
                usuarioEntity.getRol() != null ? usuarioEntity.getRol().getNombreRol() : "Sin rol asignado"
        );
    }

    private void actualizarUltimoAcceso(String usuario) {
        Usuarios entity = usuariosRepository.findByUsuarioIgnoreCaseAndEstadoTrue(usuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado al registrar último acceso: " + usuario));
        entity.setUltimoAcceso(java.time.LocalDateTime.now());
        usuariosRepository.save(entity);
    }

    @Override
    @Transactional
    public void recoverPassword(RecoverPasswordRequestDTO request) {

        Usuarios usuario = usuariosRepository.findByUsuarioIgnoreCaseAndEstadoTrue(request.getEmail())
                .orElseThrow(() -> new RuntimeException("El correo electronico ingresado no existe, revise los datos."));

        // Aquí agregar la lógica de envío de correo electrónico...
    }

    @Override
    @Transactional
    public void register(UsuariosRequestDTO request) {
        if (usuariosRepository.findByUsuarioIgnoreCaseAndEstadoTrue(request.getUsuario()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya se encuentra registrado en el sistema.");
        }

        Usuarios nuevoUsuario = new Usuarios();
        nuevoUsuario.setUsuario(request.getUsuario());
        nuevoUsuario.setNombres(request.getNombres());
        nuevoUsuario.setApellidos(request.getApellidos());
        nuevoUsuario.setPassword(passwordEncoder.encode(request.getPassword()));

        nuevoUsuario.setEstado(true);

        //nuevoUsuario.setRoles(rolesRepository.findByNombreRol("Asociado").orElse(null));

        usuariosRepository.save(nuevoUsuario);
    }
}