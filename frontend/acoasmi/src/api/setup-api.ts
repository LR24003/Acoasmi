import { client } from './generated/client.gen';
import { getAccessToken, getRefreshToken, setTokens, clearTokens } from '@/lib/auth-tokens';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

// 1. Configurar la URL base
client.setConfig({
    baseUrl: API_URL,
});

// 2. Interceptor de peticiones
client.interceptors.request.use(async (request) => {
    const token = getAccessToken();
    if (token) {
        request.headers.set('Authorization', `Bearer ${token}`);
    }
    return request;
});

// 3. Manejo de cola
let isRefreshing = false;
let failedQueue: Array<{
    resolve: (token: string) => void;
    reject: (error: unknown) => void;
}> = [];

const processQueue = (error: unknown, token: string | null = null) => {
    failedQueue.forEach((prom) => {
        if (error) {
            prom.reject(error);
        } else if (token) {
            prom.resolve(token);
        }
    });
    failedQueue = [];
};

// 4. Interceptor de respuestas (Refresh token)
client.interceptors.response.use(async (response, request, options) => {
    if (response.status !== 401) {
        return response;
    }

    const requestUrl = request.url || '';
    if (requestUrl.includes('/auth/login') || requestUrl.includes('/auth/refresh')) {
        return response;
    }

    if (isRefreshing) {
        return new Promise((resolve, reject) => {
            failedQueue.push({
                resolve: async (newToken: string) => {
                    const retryRequest = new Request(request, {
                        headers: new Headers(request.headers),
                    });
                    retryRequest.headers.set('Authorization', `Bearer ${newToken}`);

                    const retryFetch = options?.fetch ?? globalThis.fetch;
                    resolve(await retryFetch(retryRequest));
                },
                reject: (err) => reject(err),
            });
        });
    }

    isRefreshing = true;
    const refreshToken = getRefreshToken();

    if (!refreshToken) {
        isRefreshing = false;
        clearTokens();
        window.location.href = '/login';
        return response;
    }

    try {
        const refreshResponse = await fetch(`${API_URL}/api/v1/auth/refresh`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ refreshToken }),
        });

        if (!refreshResponse.ok) {
            throw new Error('Refresh token expirado');
        }

        const data = await refreshResponse.json();
        const { accessToken: newAccessToken, refreshToken: newRefreshToken } = data;

        setTokens(newAccessToken, newRefreshToken || refreshToken);
        processQueue(null, newAccessToken);

        const newRequest = new Request(request, {
            headers: new Headers(request.headers),
        });
        newRequest.headers.set('Authorization', `Bearer ${newAccessToken}`);

        const _fetch = options?.fetch ?? globalThis.fetch;
        return await _fetch(newRequest);

    } catch (refreshError) {
        processQueue(refreshError, null);
        clearTokens();
        window.location.href = '/login';
        return response;
    } finally {
        isRefreshing = false;
    }
});