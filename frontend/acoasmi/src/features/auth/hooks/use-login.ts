import { useMutation } from '@tanstack/react-query';
import { useAuth } from '../context/auth-context';
import type { LoginRequestDTO, LoginResponseDTO } from '../types';
import { toast } from 'sonner';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

export const useLogin = () => {
    const { login } = useAuth();

    return useMutation({
        mutationFn: async (credentials: LoginRequestDTO) => {
            const response = await fetch(`${API_BASE_URL}/api/auth/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(credentials),
            });

            if (!response.ok) {
                const errorText = await response.text();
                let errorMessage = 'Credenciales inválidas';
                try {
                    const jsonError = JSON.parse(errorText);
                    errorMessage = jsonError.message || errorMessage;
                } catch {
                    if (errorText) errorMessage = errorText;
                }
                throw new Error(errorMessage);
            }

            const data: LoginResponseDTO = await response.json();
            return data;
        },
        onSuccess: (data) => {
            login(data);
            toast.success(`¡Bienvenido/a ${data.nombres} ${data.apellidos}!`);
        },
        onError: (error: Error) => {
            toast.error(error.message || 'Error al iniciar sesión');
        },
    });
};