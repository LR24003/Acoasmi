export interface PermisosRequestDTO {
    codigoPermiso: string;
    descripcion: string;
}

export interface RolesRequestDTO {
    rol: string;
    descripcion: string;
    permisos: string[];
}

export interface UsuariosRequestDTO {
    usuario: string;
    password?: string;
    nombres: string;
    apellidos: string;
    email: string;
    rol: string;
}

export interface UsuariosResponseDTO {
    id: number;
    usuario: string;
    nombres: string;
    apellidos: string;
    email: string;
    fechaCreacion: string;
    ultimoAcceso?: string;
    rol: string;
    estado: boolean;
}