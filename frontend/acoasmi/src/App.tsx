import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { AuthProvider, useAuth } from '@/features/auth/context/auth-context';
import { LoginForm } from '@/features/auth/components/login-form';
import { DashboardView } from '@/features/dashboard/components/dashboard-view';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { Toaster } from '@/components/ui/sonner';

const queryClient = new QueryClient();

function MainContent() {
    const { isAuthenticated, user, logout } = useAuth();

    if (!isAuthenticated) {
        return (
            <div className="min-h-screen bg-slate-100 flex items-center justify-center p-4">
                <LoginForm />
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-slate-50 p-6 space-y-6">
            {/* Header Global */}
            <header className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 bg-white p-4 rounded-xl shadow-sm border border-slate-200">
                <div>
                    <h1 className="text-xl font-bold text-slate-800">El Roble de R.L</h1>
                    <p className="text-sm text-slate-500">
                        Usuario: <span className="font-semibold text-slate-700">{user?.nombres} {user?.apellidos}</span>
                    </p>
                </div>
                <div className="flex items-center gap-3">
                    <Badge variant="outline" className="bg-slate-100 uppercase text-xs font-semibold">
                        {user?.nombreRol || 'Usuario'}
                    </Badge>
                    <Button variant="destructive" size="sm" onClick={logout} className="cursor-pointer">
                        Cerrar Sesión
                    </Button>
                </div>
            </header>

            {/* Dashboard Principal de Pruebas */}
            <main>
                <DashboardView />
            </main>
        </div>
    );
}

export function App() {
    return (
        <QueryClientProvider client={queryClient}>
            <AuthProvider>
                <MainContent />
                <Toaster position="top-right" richColors />
            </AuthProvider>
        </QueryClientProvider>
    );
}

export default App;