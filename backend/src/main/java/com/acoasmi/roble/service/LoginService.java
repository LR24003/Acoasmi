package com.acoasmi.roble.service;

import com.acoasmi.roble.dto.request.LoginRequestDTO;
import com.acoasmi.roble.dto.request.RecoverPasswordRequestDTO;
import com.acoasmi.roble.dto.request.UsuariosRequestDTO;
import com.acoasmi.roble.dto.response.LoginResponseDTO;

public interface LoginService {

    LoginResponseDTO login(LoginRequestDTO request);

    void recoverPassword(RecoverPasswordRequestDTO request);

    void register(UsuariosRequestDTO request);
}