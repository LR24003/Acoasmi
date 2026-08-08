import { Link } from 'react-router-dom';
import { navigationConfig } from '@/config/navigation';
import { useAuth } from '@/features/auth/context/auth-context';

export function DashboardOverview() {
    const { user } = useAuth();

    // Normaliza el rol a Mayúsculas para evitar fallas por minúsculas (ej. "admin" vs "ADMIN")
    const rawRole = user?.rol || user?.nombreRol || 'ADMINISTRADOR';
    const userRole = String(rawRole).toUpperCase();

    // Filtra los módulos de forma segura verificando que navigationConfig no sea undefined
    const accessibleModules = (navigationConfig || []).filter((m) => {
        if (!m || !m.rolesAllowed) return false;
        // Compara pasando los roles permitidos también a mayúsculas
        const allowedUpper = m.rolesAllowed.map((r) => String(r).toUpperCase());
        return (allowedUpper.includes(userRole) || allowedUpper.includes('ADMINISTRADOR')) && m.href !== '/dashboard';
    });

    return (
        <div className="space-y-6 max-w-7xl mx-auto p-4 sm:p-6">
            <div>
                <h1 className="text-2xl font-bold text-slate-900">
                    ¡Bienvenido, {user?.nombres || user?.usuario || 'Usuario'}!
                </h1>
                <p className="text-sm text-slate-500">
                    Selecciona un módulo de trabajo para iniciar la gestión operativa de ACOASMI DE R.L.
                </p>
            </div>

            {/* Grid de Módulos */}
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
                {accessibleModules.map((module) => {
                    const IconComponent = module.icon;

                    return (
                        <div
                            key={module.href}
                            className={`
                                border rounded-xl p-5 bg-white shadow-sm flex flex-col justify-between transition-all
                                ${module.disabled
                                ? 'opacity-60 bg-slate-50 border-slate-200'
                                : 'hover:shadow-md hover:border-emerald-500'
                            }
                            `}
                        >
                            <div className="space-y-3">
                                <div className="flex justify-between items-start">
                                    <div className={`p-3 rounded-lg ${module.disabled ? 'bg-slate-200 text-slate-500' : 'bg-emerald-100 text-emerald-700'}`}>
                                        {/* Renderizado seguro del ícono */}
                                        {IconComponent ? <IconComponent size={24} /> : <div className="w-6 h-6 bg-slate-300 rounded" />}
                                    </div>
                                    {module.disabled && (
                                        <span className="text-[10px] font-bold bg-slate-200 text-slate-600 px-2 py-0.5 rounded">
                                            Próximamente
                                        </span>
                                    )}
                                </div>
                                <h3 className="font-bold text-slate-800 text-base">{module.title}</h3>
                            </div>

                            <div className="pt-4 mt-4 border-t border-slate-100">
                                {!module.disabled ? (
                                    <Link
                                        to={module.href}
                                        className="text-xs font-bold text-emerald-600 hover:text-emerald-700 flex items-center gap-1"
                                    >
                                        Ingresar al módulo &rarr;
                                    </Link>
                                ) : (
                                    <span className="text-xs text-slate-400 font-medium">Módulo en desarrollo</span>
                                )}
                            </div>
                        </div>
                    );
                })}
            </div>
        </div>
    );
}