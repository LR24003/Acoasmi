import { axiosInstance } from '@/lib/axios';

import type {
    UsuariosRequestDTO,
    UsuariosResponseDTO,
    RolesRequestDTO,
    PermisosRequestDTO
} from '../types';

export const userService = {
    // --- Usuarios ---
    getUsers: async (): Promise<UsuariosResponseDTO[]> => {
        const { data } = await axiosInstance.get('/api/usuarios');
        return data;
    },

    createUser: async (userDTO: UsuariosRequestDTO): Promise<UsuariosResponseDTO> => {
        const { data } = await axiosInstance.post('/api/usuarios', userDTO);
        return data;
    },

    updateUser: async (id: number, userDTO: UsuariosRequestDTO): Promise<UsuariosResponseDTO> => {
        const { data } = await axiosInstance.put(`/api/usuarios/${id}`, userDTO);
        return data;
    },
    
    deleteUser: async (id: number): Promise<void> => {
        await axiosInstance.delete(`/api/usuarios/${id}`);
    },

    toggleUserStatus: async (id: number, estado: boolean): Promise<void> => {
        await axiosInstance.patch(`/api/usuarios/${id}/estado`, null, {
            params: { estado }
        });
    },

    // --- Roles ---
    getRoles: async (): Promise<Array<{ id: number; rol: string; descripcion: string }>> => {
        const { data } = await axiosInstance.get('/api/roles');
        return data;
    },

    createRole: async (roleDTO: RolesRequestDTO) => {
        const { data } = await axiosInstance.post('/api/roles', roleDTO);
        return data;
    },

    // --- Permisos ---
    getPermissions: async (): Promise<PermisosRequestDTO[]> => {
        const { data } = await axiosInstance.get('/api/permisos');
        return data;
    }
};