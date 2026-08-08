import { useState } from 'react';
import { Link, useLocation, Outlet, useNavigate } from 'react-router-dom';
import { navigationConfig } from '@/config/navigation';
import { useAuth } from '@/features/auth/context/auth-context';
import logoAcoasmi from '@/assets/Roble-RL.jpg';
import { LogOut, Menu, X, ChevronRight, UserCircle, LayoutDashboard } from 'lucide-react';
import { Button } from '@/components/ui/button';

export function DashboardLayout() {
    const [sidebarOpen, setSidebarOpen] = useState(false);
    const location = useLocation();
    const navigate = useNavigate();
    const { user, logout } = useAuth();
    
    const rawRole = user?.rol || user?.nombreRol || 'ADMINISTRADOR';
    const userRole = String(rawRole).toUpperCase();

    // Filtrar rutas de forma segura comparando roles en mayúsculas
    const menuItems = (navigationConfig || []).filter((item) => {
        if (!item || !item.rolesAllowed) return false;
        const allowedUpper = item.rolesAllowed.map((r) => String(r).toUpperCase());
        return allowedUpper.includes(userRole) || allowedUpper.includes('ADMINISTRADOR');
    });

    const handleLogout = () => {
        logout();
        navigate('/login', { replace: true });
    };

    return (
        <div className="min-h-screen bg-slate-100 flex flex-col md:flex-row">
            {/* Header Móvil */}
            <header className="md:hidden bg-emerald-800 text-white p-4 flex justify-between items-center sticky top-0 z-50">
                <div className="flex items-center gap-3">
                    <img src={logoAcoasmi} alt="Logo ACOASMI" className="w-8 h-8 rounded-full bg-white p-0.5" />
                    <span className="font-bold text-sm tracking-wide">ACOASMI DE R.L.</span>
                </div>
                <button onClick={() => setSidebarOpen(!sidebarOpen)} className="p-1">
                    {sidebarOpen ? <X size={24} /> : <Menu size={24} />}
                </button>
            </header>

            {/* Sidebar Lateral */}
            <aside
                className={`
                    fixed inset-y-0 left-0 z-40 w-64 bg-slate-900 text-slate-300 transform transition-transform duration-200 ease-in-out flex flex-col justify-between
                    ${sidebarOpen ? 'translate-x-0' : '-translate-x-full'}
                    md:relative md:translate-x-0
                `}
            >
                <div>
                    {/* Header Sidebar */}
                    <div className="p-5 bg-slate-950 flex items-center gap-3 border-b border-slate-800">
                        <img src={logoAcoasmi} alt="Logo ACOASMI" className="w-10 h-10 rounded-full bg-white p-1" />
                        <div>
                            <h2 className="text-white font-bold text-sm leading-tight">ACOASMI DE R.L.</h2>
                            <p className="text-[11px] text-emerald-400 font-medium">Sistema Financiero</p>
                        </div>
                    </div>

                    {/* Menú Navegación */}
                    <nav className="p-3 space-y-1">
                        {menuItems.map((item) => {
                            const IconComponent = item.icon || LayoutDashboard;
                            const isActive = location.pathname === item.href;

                            if (item.disabled) {
                                return (
                                    <div
                                        key={item.href}
                                        className="flex items-center justify-between px-3 py-2.5 rounded-lg text-slate-500 text-sm opacity-60 cursor-not-allowed"
                                    >
                                        <div className="flex items-center gap-3">
                                            <IconComponent size={18} />
                                            <span>{item.title}</span>
                                        </div>
                                        <span className="text-[10px] bg-slate-800 px-1.5 py-0.5 rounded text-slate-400">Pronto</span>
                                    </div>
                                );
                            }

                            return (
                                <Link
                                    key={item.href}
                                    to={item.href}
                                    onClick={() => setSidebarOpen(false)}
                                    className={`
                                        flex items-center justify-between px-3 py-2.5 rounded-lg text-sm font-medium transition-colors
                                        ${isActive
                                        ? 'bg-emerald-600 text-white shadow-sm'
                                        : 'hover:bg-slate-800 text-slate-300 hover:text-white'
                                    }
                                    `}
                                >
                                    <div className="flex items-center gap-3">
                                        <IconComponent size={18} />
                                        <span>{item.title}</span>
                                    </div>
                                    {isActive && <ChevronRight size={16} />}
                                </Link>
                            );
                        })}
                    </nav>
                </div>

                {/* Footer Sidebar - Datos Usuario */}
                <div className="p-4 border-t border-slate-800 bg-slate-950/50">
                    <div className="flex items-center gap-3 mb-3">
                        <UserCircle className="w-9 h-9 text-emerald-500 flex-shrink-0" />
                        <div className="overflow-hidden">
                            <p className="text-sm font-semibold text-white truncate">
                                {user?.nombres || user?.usuario || 'Usuario'} {user?.apellidos || ''}
                            </p>
                            <span className="text-[11px] px-2 py-0.5 rounded bg-emerald-900/50 text-emerald-300 border border-emerald-700/50 inline-block mt-0.5">
                                {userRole}
                            </span>
                        </div>
                    </div>
                    <Button
                        onClick={handleLogout}
                        variant="outline"
                        className="w-full justify-start text-rose-400 border-slate-800 hover:bg-rose-950/30 hover:text-rose-300 hover:border-rose-900 text-xs"
                    >
                        <LogOut size={15} className="mr-2" /> Cerrar Sesión
                    </Button>
                </div>
            </aside>

            {/* Contenido Principal donde se renderiza DashboardOverview o sub-rutas */}
            <main className="flex-1 p-4 md:p-8 overflow-y-auto">
                <Outlet />
            </main>
        </div>
    );
}