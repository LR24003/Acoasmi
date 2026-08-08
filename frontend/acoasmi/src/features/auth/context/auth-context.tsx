import { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { getAccessToken, setTokens, clearTokens } from '@/lib/auth-tokens';
import type { LoginResponseDTO } from '../types'; 

interface AuthContextType {
    isAuthenticated: boolean;
    user: Omit<LoginResponseDTO, 'token'> | null;
    login: (data: LoginResponseDTO) => void;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(() => !!getAccessToken());

    const [user, setUser] = useState<Omit<LoginResponseDTO, 'token'> | null>(() => {
        try {
            const savedUser = localStorage.getItem('auth_user');
            return savedUser ? JSON.parse(savedUser) : null;
        } catch (error) {
            console.error('Error parseando usuario almacenado:', error);
            return null;
        }
    });

    useEffect(() => {
        const checkAuth = () => {
            setIsAuthenticated(!!getAccessToken());
            const savedUser = localStorage.getItem('auth_user');
            setUser(savedUser ? JSON.parse(savedUser) : null);
        };

        window.addEventListener('storage', checkAuth);
        return () => window.removeEventListener('storage', checkAuth);
    }, []);

    const login = (data: LoginResponseDTO) => {
        setTokens(data.token, data.token);

        const userData: Omit<LoginResponseDTO, 'token'> = {
            usuario: data.usuario,
            nombres: data.nombres,
            apellidos: data.apellidos,
            nombreRol: data.nombreRol,
        };

        localStorage.setItem('auth_user', JSON.stringify(userData));
        setUser(userData);
        setIsAuthenticated(true);
    };

    const logout = () => {
        clearTokens();
        localStorage.removeItem('auth_user');
        setUser(null);
        setIsAuthenticated(false);
    };

    return (
        <AuthContext.Provider value={{ isAuthenticated, user, login, logout }}>
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