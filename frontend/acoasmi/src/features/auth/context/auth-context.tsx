import { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { getAccessToken, setTokens, clearTokens } from '@/lib/auth-tokens';
import type { LoginResponseDTO } from '../types';

export interface AuthUser extends Omit<LoginResponseDTO, 'token'> {
    rol: string;
}

interface AuthContextType {
    isAuthenticated: boolean;
    isLoading: boolean;
    user: AuthUser | null;
    login: (data: LoginResponseDTO) => void;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
    const [user, setUser] = useState<AuthUser | null>(null);

    // Cargar sesión inicial al montar el componente
    useEffect(() => {
        try {
            const token = getAccessToken();
            const savedUser = localStorage.getItem('auth_user');

            if (token && savedUser) {
                const parsedUser = JSON.parse(savedUser);
                setUser(parsedUser);
                setIsAuthenticated(true);
            }
        } catch (error) {
            console.error('Error parseando usuario almacenado:', error);
            clearTokens();
            localStorage.removeItem('auth_user');
            setUser(null);
            setIsAuthenticated(false);
        } finally {
            setIsLoading(false);
        }
    }, []);

    // Sincronización entre pestañas
    useEffect(() => {
        const checkAuth = () => {
            const token = getAccessToken();
            const savedUser = localStorage.getItem('auth_user');

            if (token && savedUser) {
                try {
                    setUser(JSON.parse(savedUser));
                    setIsAuthenticated(true);
                } catch {
                    setUser(null);
                    setIsAuthenticated(false);
                }
            } else {
                setUser(null);
                setIsAuthenticated(false);
            }
        };

        window.addEventListener('storage', checkAuth);
        return () => window.removeEventListener('storage', checkAuth);
    }, []);

    const login = (data: LoginResponseDTO) => {
        try {
            // Guardar tokens de forma segura con fallback
            const accessToken = data.token || '';
            setTokens(accessToken, accessToken);

            // Mapear datos de usuario asegurando 'rol' en mayúsculas
            const rawRol = data.nombreRol || (data as any).rol || 'ADMINISTRADOR';

            const userData: AuthUser = {
                usuario: data.usuario || '',
                nombres: data.nombres || '',
                apellidos: data.apellidos || '',
                nombreRol: rawRol,
                rol: String(rawRol).toUpperCase(),
            };

            localStorage.setItem('auth_user', JSON.stringify(userData));
            setUser(userData);
            setIsAuthenticated(true);
        } catch (error) {
            console.error('Error durante el proceso de login en el contexto:', error);
        }
    };

    const logout = () => {
        try {
            clearTokens();
            localStorage.removeItem('auth_user');
        } catch (error) {
            console.error('Error al cerrar sesión:', error);
        } finally {
            setUser(null);
            setIsAuthenticated(false);
        }
    };

    // Previene la pantalla en blanco bloqueante durante la hidratación inicial
    if (isLoading) {
        return (
            <div className="flex h-screen w-screen items-center justify-center bg-slate-900 text-white font-sans">
                <div className="flex flex-col items-center gap-3">
                    <div className="w-8 h-8 border-4 border-emerald-500 border-t-transparent rounded-full animate-spin"></div>
                    <span className="text-sm font-semibold tracking-wide">Iniciando ACOASMI...</span>
                </div>
            </div>
        );
    }

    return (
        <AuthContext.Provider value={{ isAuthenticated, isLoading, user, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth debe usarse dentro de un AuthProvider');
    }
    return context;
};