package com.acoasmi.roble.service;

import com.acoasmi.roble.dto.request.UsuariosRequestDTO;
import com.acoasmi.roble.dto.response.UsuariosResponseDTO;
import com.acoasmi.roble.entity.Usuarios;

import java.util.List;

public interface UsuariosService extends AcoasmiService<Usuarios, UsuariosRequestDTO, UsuariosResponseDTO, Long> {

    UsuariosResponseDTO getByUsuario(String usuario);

    List<UsuariosResponseDTO> getByEstado(Boolean estado);

    void actualizarUltimoAcceso(String usuario);

}
