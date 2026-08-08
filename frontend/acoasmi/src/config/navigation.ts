import {
    Users,
    UserCheck,
    PiggyBank,
    HandCoins,
    Landmark,
    BookOpenCheck,
    ShieldCheck,
    LayoutDashboard,
    type LucideIcon
} from 'lucide-react';

export interface NavItem {
    title: string;
    href: string;
    icon: LucideIcon;
    badge?: string;
    rolesAllowed: string[];
    disabled?: boolean;
}

export const navigationConfig: NavItem[] = [
    {
        title: 'Dashboard Principal',
        href: '/dashboard',
        icon: LayoutDashboard,
        rolesAllowed: ['ADMIN', 'ADMINISTRADOR', 'GERENTE', 'CAJERO', 'OFICIAL'],
    },
    {
        title: 'Gestión de Usuarios',
        href: '/dashboard/usuarios',
        icon: Users,
        rolesAllowed: ['ADMIN', 'ADMINISTRADOR', 'GERENTE'],
    },
    {
        title: 'Asociados',
        href: '/dashboard/asociados',
        icon: UserCheck,
        rolesAllowed: ['ADMIN', 'ADMINISTRADOR', 'GERENTE', 'OFICIAL'],
        disabled: true, // Se activará en la siguiente fase
    },
    {
        title: 'Cuentas de Ahorro',
        href: '/dashboard/cuentas',
        icon: PiggyBank,
        rolesAllowed: ['ADMIN', 'ADMINISTRADOR', 'GERENTE', 'CAJERO'],
        disabled: true,
    },
    {
        title: 'Préstamos y Créditos',
        href: '/dashboard/prestamos',
        icon: HandCoins,
        rolesAllowed: ['ADMIN', 'ADMINISTRADOR', 'GERENTE', 'OFICIAL'],
        disabled: true,
    },
    {
        title: 'Caja / Operaciones',
        href: '/dashboard/caja',
        icon: Landmark,
        rolesAllowed: ['ADMIN', 'ADMINISTRADOR', 'GERENTE', 'CAJERO'],
        disabled: true,
    },
    {
        title: 'Contabilidad',
        href: '/dashboard/contabilidad',
        icon: BookOpenCheck,
        rolesAllowed: ['ADMIN', 'ADMINISTRADOR', 'GERENTE'],
        disabled: true,
    },
    {
        title: 'Oficialía y Cumplimiento',
        href: '/dashboard/oficialia',
        icon: ShieldCheck,
        rolesAllowed: ['ADMIN', 'ADMINISTRADOR', 'GERENTE', 'OFICIAL'],
        disabled: true,
    },
];