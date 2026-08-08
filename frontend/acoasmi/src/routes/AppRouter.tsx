import { Routes, Route, Navigate } from 'react-router-dom';
import { LoginForm } from '@/features/auth/components/login-form';
import { DashboardLayout } from '@/components/layout/DashboardLayout';
import { DashboardOverview } from '@/features/dashboard/pages/DashboardOverview';
import { UsersPage } from '@/features/admin/pages/UsersPage';
import { ProtectedRoute } from './ProtectedRoute';

export function AppRouter() {
    return (
        <Routes>
            <Route path="/" element={<Navigate to="/dashboard" replace />} />
            <Route path="/login" element={<LoginForm />} />

            {/* Única capa de protección para todo el Dashboard */}
            <Route element={<ProtectedRoute />}>
                <Route path="/dashboard" element={<DashboardLayout />}>
                    <Route index element={<DashboardOverview />} />

                    {/* Todas tus sub-rutas van aquí directamente usando path absoluto o relativo */}
                    <Route path="/dashboard/usuarios" element={<UsersPage />} />
                </Route>
            </Route>

            <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
    );
}