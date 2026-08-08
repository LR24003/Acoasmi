import { useMutation } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/auth-context';
import type { LoginRequestDTO, LoginResponseDTO } from '../types';
import { toast } from 'sonner';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

export const useLogin = () => {
    const { login } = useAuth();
    const navigate = useNavigate();

    return useMutation({
        mutationFn: async (credentials: LoginRequestDTO) => {
            const response = await fetch(`${API_BASE_URL}/api/auth/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(credentials),
            });

            const textResponse = await response.text();

            if (!response.ok) {
                let errorMessage = 'Credenciales inválidas';
                try {
                    const jsonError = JSON.parse(textResponse);
                    errorMessage = jsonError.message || errorMessage;
                } catch {
                    if (textResponse) errorMessage = textResponse;
                }
                throw new Error(errorMessage);
            }

            // Parseo seguro de la respuesta JSON
            try {
                const data: LoginResponseDTO = JSON.parse(textResponse);
                return data;
            } catch (error) {
                console.error('Error al parsear respuesta JSON del servidor:', error);
                throw new Error('La respuesta del servidor tiene un formato inválido.');
            }
        },
        onSuccess: (data) => {
            // 1. Guardar token y estado en el AuthContext
            login(data);

            // 2. Notificación visual de éxito
            toast.success(`¡Bienvenido/a ${data.nombres || data.usuario || ''}!`);

            // 3. REDIRECCIÓN FORZADA AL DASHBOARD (Evita la pantalla colgada o en blanco)
            navigate('/dashboard', { replace: true });
        },
        onError: (error: Error) => {
            toast.error(error.message || 'Error al iniciar sesión');
        },
    });
};