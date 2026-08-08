export interface LoginRequestDTO {
    usuario: string;
    password: string;
}

export interface LoginResponseDTO {
    token: string;
    usuario: string;
    nombres: string;
    apellidos: string;
    nombreRol: string;
}