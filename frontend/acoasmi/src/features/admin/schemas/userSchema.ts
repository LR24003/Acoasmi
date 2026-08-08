import { z } from 'zod';

export const userFormSchema = z.object({
    usuario: z.string().min(4, 'El usuario debe tener al menos 4 caracteres'),
    password: z.string().min(6, 'La contraseña debe tener al menos 6 caracteres').optional().or(z.literal('')),
    nombres: z.string().min(2, 'El nombre es obligatorio'),
    apellidos: z.string().min(2, 'El apellido es obligatorio'),
    email: z.string().email('Ingrese un correo electrónico válido'),
    rol: z.string().min(1, 'Debe seleccionar un rol'),
});

export type UserFormValues = z.infer<typeof userFormSchema>;