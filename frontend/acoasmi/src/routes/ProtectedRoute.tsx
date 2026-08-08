import { ReactNode } from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '@/features/auth/context/auth-context';

interface ProtectedRouteProps {
    allowedRoles?: string[];
    children?: ReactNode;
}

export function ProtectedRoute({ allowedRoles, children }: ProtectedRouteProps) {
    const { isAuthenticated, isLoading, user } = useAuth();

    // 1. Si está cargando el contexto, no tomar decisiones de redirección
    if (isLoading) {
        return null;
    }

    const storedUserRaw = localStorage.getItem('auth_user') || localStorage.getItem('user');
    let currentUser = user;

    if (!currentUser && storedUserRaw) {
        try {
            currentUser = JSON.parse(storedUserRaw);
        } catch {
            currentUser = null;
        }
    }

    const isUserAuth = isAuthenticated || Boolean(currentUser);

    // 3. Si definitivamente no hay sesión ni en memoria ni en localStorage -> Login
    if (!isUserAuth || !currentUser) {
        return <Navigate to="/login" replace />;
    }

    // 4. Verificación de Permisos / Roles
    if (allowedRoles && allowedRoles.length > 0) {
        // Extraer rol soportando cualquier estructura de DTO de tu backend
        const userRoleRaw =
            currentUser.rol ||
            currentUser.nombreRol ||
            currentUser.role ||
            (currentUser as any).roles?.[0] ||
            '';

        const userRole = String(userRoleRaw).toUpperCase().trim();
        const allowedUpper = allowedRoles.map((r) => String(r).toUpperCase().trim());

        // Administrador siempre tiene acceso total
        const isAdmin = userRole === 'ADMIN' || userRole === 'ADMINISTRADOR';
        const hasPermission = isAdmin || allowedUpper.includes(userRole);

        if (!hasPermission) {
            console.warn(`[ProtectedRoute] Acceso denegado. Rol actual: "${userRole}", Permitidos:`, allowedUpper);
            return <Navigate to="/dashboard" replace />;
        }
    }

    // Retornar las rutas/componentes
    return children ? <>{children}</> : <Outlet />;
}