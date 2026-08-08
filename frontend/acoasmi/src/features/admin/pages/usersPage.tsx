import { useState } from 'react';
import { useUsers } from '../hooks/useUsers';
import { UserModal } from '../components/UserModal';
import type { UsuariosResponseDTO } from '../types';
import type { UserFormValues } from '../schemas/userSchema';

import { Button } from '@/components/ui/button';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { Badge } from '@/components/ui/badge';
import {
    Dialog,
    DialogContent,
    DialogHeader,
    DialogTitle,
    DialogDescription,
    DialogFooter,
} from '@/components/ui/dialog';
import { Plus, Edit2, Trash2 } from 'lucide-react';

const formatTableDate = (dateStr?: string | null): string => {
    if (!dateStr) return 'N/A';

    try {
        if (typeof dateStr === 'string' && dateStr.includes('-')) {
            const parts = dateStr.trim().split(' ');
            const dateParts = parts[0].split('-');

            if (dateParts.length === 3) {
                const [day, month, year] = dateParts.map(Number);
                let hours = 0;
                let minutes = 0;

                if (parts[1]) {
                    const [h, m] = parts[1].split(':').map(Number);
                    hours = h || 0;
                    minutes = m || 0;
                }

                const date = new Date(year, month - 1, day, hours, minutes);

                if (!isNaN(date.getTime())) {
                    return new Intl.DateTimeFormat('es-ES', {
                        day: '2-digit',
                        month: '2-digit',
                        year: 'numeric',
                        hour: '2-digit',
                        minute: '2-digit',
                        hour12: true,
                    }).format(date);
                }
            }
        }
        return String(dateStr);
    } catch {
        return String(dateStr);
    }
};

export function UsersPage() {
    const { users, roles, isLoading, createUser, updateUser, deleteUser, isSubmitting } = useUsers();

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedUser, setSelectedUser] = useState<UsuariosResponseDTO | null>(null);

    // Estado para el modal de confirmación de borrado
    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
    const [userToDelete, setUserToDelete] = useState<UsuariosResponseDTO | null>(null);

    // Extraer array real de usuarios
    const rawUsersList: any = users;
    const allUsers: any[] = Array.isArray(rawUsersList)
        ? rawUsersList
        : rawUsersList?.data?.content || rawUsersList?.content || rawUsersList?.data || [];

    // FILTRADO DE BORRADO LÓGICO: Solo mostramos en la tabla a los usuarios ACTIVOS (estado === true)
    const activeUsers = allUsers.filter((u) => Boolean(u.estado) === true);

    const handleOpenCreate = () => {
        setSelectedUser(null);
        setIsModalOpen(true);
    };

    const handleOpenEdit = (user: UsuariosResponseDTO) => {
        setSelectedUser(user);
        setIsModalOpen(true);
    };

    // Abre modal de confirmación de borrado
    const handleOpenDelete = (user: UsuariosResponseDTO) => {
        setUserToDelete(user);
        setIsDeleteModalOpen(true);
    };

    // Ejecuta la desactivación en backend
    const handleConfirmDelete = async () => {
        if (!userToDelete) return;

        try {
            if (deleteUser) {
                await deleteUser(userToDelete.id);
            }
        } finally {
            setIsDeleteModalOpen(false);
            setUserToDelete(null);
        }
    };

    const handleSubmit = async (values: UserFormValues) => {
        if (selectedUser) {
            await updateUser({ id: selectedUser.id, dto: values });
        } else {
            await createUser(values);
        }
    };

    return (
        <div className="p-6 space-y-6 max-w-7xl mx-auto">
            <div className="flex justify-between items-center">
                <div>
                    <h1 className="text-2xl font-bold text-gray-900">Gestión de Usuarios</h1>
                    <p className="text-sm text-gray-500">
                        Módulo exclusivo de Administración y Gerencia de ACOASMI DE R.L.
                    </p>
                </div>
                <Button onClick={handleOpenCreate} className="bg-emerald-600 hover:bg-emerald-700 text-white">
                    <Plus className="mr-2 h-4 w-4" /> Nuevo Usuario
                </Button>
            </div>

            <div className="border rounded-lg bg-white shadow-sm overflow-hidden">
                <Table>
                    <TableHeader className="bg-slate-50">
                        <TableRow>
                            <TableHead>Nombre Completo</TableHead>
                            <TableHead>Usuario</TableHead>
                            <TableHead>Correo</TableHead>
                            <TableHead>Rol</TableHead>
                            <TableHead>Estado</TableHead>
                            <TableHead>Fecha Creación</TableHead>
                            <TableHead>Último Acceso</TableHead>
                            <TableHead className="text-right">Acciones</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {isLoading ? (
                            <TableRow>
                                <TableCell colSpan={8} className="text-center py-6 text-slate-500">
                                    Cargando usuarios...
                                </TableCell>
                            </TableRow>
                        ) : activeUsers.length === 0 ? (
                            <TableRow>
                                <TableCell colSpan={8} className="text-center py-6 text-slate-500">
                                    No hay usuarios activos para mostrar.
                                </TableCell>
                            </TableRow>
                        ) : (
                            activeUsers.map((u: any) => {
                                const fechaCreacionRaw = u.fechaCreacion || u.fecha_creacion;
                                const ultimoAccesoRaw = u.ultimoAcceso || u.ultimo_acceso;

                                return (
                                    <TableRow key={u.id}>
                                        <TableCell className="font-semibold text-slate-800">
                                            {u.nombres} {u.apellidos}
                                        </TableCell>
                                        <TableCell className="text-slate-600">{u.usuario}</TableCell>
                                        <TableCell className="text-slate-600">{u.email}</TableCell>
                                        <TableCell>
                                            <Badge variant="outline" className="border-emerald-500 text-emerald-700 bg-emerald-50 font-semibold">
                                                {u.rol}
                                            </Badge>
                                        </TableCell>
                                        <TableCell>
                                            <Badge className="bg-emerald-600 hover:bg-emerald-700">
                                                Activo
                                            </Badge>
                                        </TableCell>

                                        <TableCell className="text-xs text-slate-600 font-medium">
                                            {fechaCreacionRaw ? formatTableDate(fechaCreacionRaw) : 'Sin fecha'}
                                        </TableCell>

                                        <TableCell className="text-xs text-slate-500">
                                            {ultimoAccesoRaw ? formatTableDate(ultimoAccesoRaw) : 'Sin accesos'}
                                        </TableCell>

                                        <TableCell className="text-right space-x-1">
                                            <Button size="sm" variant="ghost" onClick={() => handleOpenEdit(u)} title="Editar Usuario">
                                                <Edit2 className="h-4 w-4 text-slate-600" />
                                            </Button>

                                            <Button
                                                size="sm"
                                                variant="ghost"
                                                onClick={() => handleOpenDelete(u)}
                                                className="hover:bg-rose-50"
                                                title="Eliminar Usuario"
                                            >
                                                <Trash2 className="h-4 w-4 text-rose-600" />
                                            </Button>
                                        </TableCell>
                                    </TableRow>
                                );
                            })
                        )}
                    </TableBody>
                </Table>
            </div>

            {/* Modal de Formulario Crear/Editar */}
            <UserModal
                isOpen={isModalOpen}
                onClose={() => setIsModalOpen(false)}
                onSubmit={handleSubmit}
                userToEdit={selectedUser}
                rolesList={roles}
                isSubmitting={isSubmitting}
            />

            {/* Modal de Confirmación de Borrado Lógico */}
            <Dialog open={isDeleteModalOpen} onOpenChange={setIsDeleteModalOpen}>
                <DialogContent className="sm:max-w-[425px] bg-white">
                    <DialogHeader>
                        <DialogTitle className="text-lg font-bold text-gray-900">
                            Confirmar Eliminación
                        </DialogTitle>
                        <DialogDescription className="text-sm text-slate-500 pt-2">
                            ¿Estás seguro de que deseas eliminar al usuario{' '}
                            <span className="font-semibold text-slate-800">
                                {userToDelete?.nombres} {userToDelete?.apellidos}
                            </span>
                            ? El registro pasará a estar inactivo.
                        </DialogDescription>
                    </DialogHeader>
                    <DialogFooter className="pt-4 border-t space-x-2">
                        <Button
                            variant="outline"
                            onClick={() => setIsDeleteModalOpen(false)}
                            disabled={isSubmitting}
                        >
                            Cancelar
                        </Button>
                        <Button
                            onClick={handleConfirmDelete}
                            disabled={isSubmitting}
                            className="bg-rose-600 hover:bg-rose-700 text-white"
                        >
                            {isSubmitting ? 'Eliminando...' : 'Sí, eliminar'}
                        </Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>
        </div>
    );
}