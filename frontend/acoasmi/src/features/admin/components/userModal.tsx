import { useEffect } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { userFormSchema, type UserFormValues } from '../schemas/userSchema';
import type { UsuariosResponseDTO } from '../types';

import {
    Dialog,
    DialogContent,
    DialogHeader,
    DialogTitle,
    DialogFooter,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';

interface UserModalProps {
    isOpen: boolean;
    onClose: () => void;
    onSubmit: (values: UserFormValues) => Promise<void>;
    userToEdit?: UsuariosResponseDTO | null;
    rolesList: Array<{ id: number; rol: string }>;
    isSubmitting: boolean;
}

export function UserModal({
                              isOpen,
                              onClose,
                              onSubmit,
                              userToEdit,
                              rolesList,
                              isSubmitting,
                          }: UserModalProps) {
    const isEditing = Boolean(userToEdit);

    const {
        register,
        handleSubmit,
        reset,
        control,
        formState: { errors },
    } = useForm<UserFormValues>({
        resolver: zodResolver(userFormSchema),
        defaultValues: {
            nombres: '',
            apellidos: '',
            usuario: '',
            email: '',
            password: '',
            rol: '',
            estado: true, // Por defecto TRUE
        },
    });

    useEffect(() => {
        if (!isOpen) return;

        if (userToEdit) {
            // Lectura directa del campo 'estado' proveniente de tu DTO
            const estadoActual = (userToEdit as any).estado ?? (userToEdit as any).activo ?? true;

            reset({
                nombres: userToEdit.nombres || '',
                apellidos: userToEdit.apellidos || '',
                usuario: userToEdit.usuario || '',
                email: userToEdit.email || '',
                password: '',
                rol: userToEdit.rol || '',
                estado: Boolean(estadoActual),
            });
        } else {
            reset({
                nombres: '',
                apellidos: '',
                usuario: '',
                email: '',
                password: '',
                rol: rolesList[0]?.rol || '',
                estado: true, // Siempre true por defecto para creaciones
            });
        }
    }, [userToEdit, reset, rolesList, isOpen]);

    const handleFormSubmit = async (data: UserFormValues) => {
        await onSubmit(data);
        onClose();
    };

    return (
        <Dialog open={isOpen} onOpenChange={onClose}>
            <DialogContent className="sm:max-w-[500px] bg-white">
                <DialogHeader>
                    <DialogTitle className="text-xl font-bold text-gray-900">
                        {isEditing ? 'Editar Usuario' : 'Crear Nuevo Usuario'}
                    </DialogTitle>
                </DialogHeader>

                <form onSubmit={handleSubmit(handleFormSubmit)} className="space-y-4 py-2">
                    {/* Nombres y Apellidos */}
                    <div className="grid grid-cols-2 gap-4">
                        <div className="space-y-1">
                            <label className="text-xs font-semibold text-slate-600">Nombres</label>
                            <Input {...register('nombres')} placeholder="Ej. Juan Carlos" />
                            {errors.nombres && <p className="text-xs text-red-500">{errors.nombres.message}</p>}
                        </div>
                        <div className="space-y-1">
                            <label className="text-xs font-semibold text-slate-600">Apellidos</label>
                            <Input {...register('apellidos')} placeholder="Ej. Pérez Gómez" />
                            {errors.apellidos && <p className="text-xs text-red-500">{errors.apellidos.message}</p>}
                        </div>
                    </div>

                    {/* Usuario y Email */}
                    <div className="grid grid-cols-2 gap-4">
                        <div className="space-y-1">
                            <label className="text-xs font-semibold text-slate-600">Usuario</label>
                            <Input {...register('usuario')} placeholder="jperez" />
                            {errors.usuario && <p className="text-xs text-red-500">{errors.usuario.message}</p>}
                        </div>
                        <div className="space-y-1">
                            <label className="text-xs font-semibold text-slate-600">Correo Electrónico</label>
                            <Input type="email" {...register('email')} placeholder="juan@cooperativa.com" />
                            {errors.email && <p className="text-xs text-red-500">{errors.email.message}</p>}
                        </div>
                    </div>

                    {/* Contraseña y Rol */}
                    <div className="grid grid-cols-2 gap-4">
                        <div className="space-y-1">
                            <label className="text-xs font-semibold text-slate-600">
                                Contraseña {isEditing && '(Opcional)'}
                            </label>
                            <Input
                                type="password"
                                {...register('password')}
                                placeholder={isEditing ? '••••••••' : 'Ingrese contraseña'}
                            />
                            {errors.password && <p className="text-xs text-red-500">{errors.password.message}</p>}
                        </div>

                        <div className="space-y-1">
                            <label className="text-xs font-semibold text-slate-600">Rol Asignado</label>
                            <select
                                {...register('rol')}
                                className="w-full h-10 px-3 rounded-md border border-slate-200 text-sm focus:outline-none focus:ring-2 focus:ring-emerald-500 bg-white"
                            >
                                <option value="">Seleccione un rol...</option>
                                {rolesList.map((r) => (
                                    <option key={r.id} value={r.rol}>
                                        {r.rol}
                                    </option>
                                ))}
                            </select>
                            {errors.rol && <p className="text-xs text-red-500">{errors.rol.message}</p>}
                        </div>
                    </div>

                    {/* Campo Estado */}
                    <div className="space-y-1">
                        <label className="text-xs font-semibold text-slate-600">Estado de la cuenta</label>
                        <Controller
                            name="estado"
                            control={control}
                            render={({ field }) => (
                                <select
                                    value={field.value ? 'true' : 'false'}
                                    onChange={(e) => field.onChange(e.target.value === 'true')}
                                    className="w-full h-10 px-3 rounded-md border border-slate-200 text-sm focus:outline-none focus:ring-2 focus:ring-emerald-500 bg-white"
                                >
                                    <option value="true">Activo</option>
                                    <option value="false">Inactivo</option>
                                </select>
                            )}
                        />
                    </div>

                    <DialogFooter className="pt-4 border-t">
                        <Button type="button" variant="outline" onClick={onClose} disabled={isSubmitting}>
                            Cancelar
                        </Button>
                        <Button type="submit" disabled={isSubmitting} className="bg-emerald-600 hover:bg-emerald-700 text-white">
                            {isSubmitting ? 'Guardando...' : isEditing ? 'Actualizar' : 'Guardar Usuario'}
                        </Button>
                    </DialogFooter>
                </form>
            </DialogContent>
        </Dialog>
    );
}