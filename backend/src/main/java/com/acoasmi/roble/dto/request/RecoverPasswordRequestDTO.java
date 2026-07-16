package com.acoasmi.roble.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecoverPasswordRequestDTO {

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Formato de email invalido")
    private String email;
}
