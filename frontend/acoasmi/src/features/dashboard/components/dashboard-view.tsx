import { clearTokens } from '@/lib/auth-tokens';

export function DashboardView() {
    const handleLogout = () => {
        clearTokens();
        window.location.href = '/login';
    };

    return (
        <div className="min-h-screen bg-slate-50 p-6 space-y-6">
            {/* Header con bienvenida y Logout */}
            <header className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 bg-white p-6 rounded-xl shadow-sm border border-slate-100">
                <div>
                    <h1 className="text-2xl font-bold text-slate-800">¡Bienvenido al Panel Principal! 👋</h1>
                    <p className="text-sm text-slate-500 mt-1">
                        El inicio de sesión se ha realizado correctamente. Entorno de pruebas.
                    </p>
                </div>
                <button
                    onClick={handleLogout}
                    className="inline-flex items-center justify-center px-4 py-2 text-sm font-medium text-red-600 bg-red-50 hover:bg-red-100 rounded-lg transition-colors cursor-pointer w-full sm:w-auto"
                >
                    Cerrar Sesión
                </button>
            </header>

            {/* Grid de Métricas de prueba */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                <div className="bg-white p-6 rounded-xl shadow-sm border border-slate-100">
                    <p className="text-sm font-medium text-slate-500">Estado de Conexión</p>
                    <div className="flex items-center gap-2 mt-2">
                        <span className="h-3 w-3 rounded-full bg-emerald-500 animate-pulse"></span>
                        <span className="text-xl font-bold text-slate-800">Autenticado</span>
                    </div>
                    <p className="text-xs text-slate-400 mt-2">Token JWT activo en almacenamiento</p>
                </div>

                <div className="bg-white p-6 rounded-xl shadow-sm border border-slate-100">
                    <p className="text-sm font-medium text-slate-500">Asociados / Clientes</p>
                    <p className="text-3xl font-bold text-slate-800 mt-2">1,248</p>
                    <p className="text-xs text-emerald-600 mt-2 font-medium">↑ +12% este mes</p>
                </div>

                <div className="bg-white p-6 rounded-xl shadow-sm border border-slate-100">
                    <p className="text-sm font-medium text-slate-500">Solicitudes Pendientes</p>
                    <p className="text-3xl font-bold text-slate-800 mt-2">18</p>
                    <p className="text-xs text-amber-600 mt-2 font-medium">Requieren revisión</p>
                </div>
            </div>

            {/* Sección de Pruebas Rápida */}
            <div className="bg-white p-6 rounded-xl shadow-sm border border-slate-100">
                <h2 className="text-lg font-semibold text-slate-800 mb-4">Acciones de Prueba</h2>
                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4">
                    <button
                        onClick={() => alert('Prueba de API realizada')}
                        className="p-4 border border-slate-200 rounded-lg text-left hover:border-blue-500 hover:bg-blue-50/50 transition-all cursor-pointer"
                    >
                        <p className="font-medium text-slate-700 text-sm">Probador API</p>
                        <p className="text-xs text-slate-400 mt-1">Llamar endpoint protegido</p>
                    </button>

                    <button
                        onClick={() => console.log('Simulación de carga')}
                        className="p-4 border border-slate-200 rounded-lg text-left hover:border-blue-500 hover:bg-blue-50/50 transition-all cursor-pointer"
                    >
                        <p className="font-medium text-slate-700 text-sm">Ver Tokens</p>
                        <p className="text-xs text-slate-400 mt-1">Imprimir en consola</p>
                    </button>
                </div>
            </div>
        </div>
    );
}