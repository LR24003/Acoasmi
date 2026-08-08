import axios from 'axios';

export const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080', // Cambia este puerto según la URL de tu API en Spring Boot
    headers: {
        'Content-Type': 'application/json',
    },
});

// Interceptor para adjuntar automáticamente el token JWT en cada petición
axiosInstance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// Interceptor para manejar globalmente cuando la sesión expira (401 Unauthorized)
axiosInstance.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            localStorage.removeItem('token');
            // Redirige al login si la sesión vence
            if (window.location.pathname !== '/login') {
                window.location.href = '/login';
            }
        }
        return Promise.reject(error);
    }
);