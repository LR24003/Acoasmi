import { z } from 'zod';

export const loginSchema = z.object({
    usuario: z.string().min(3, 'El usuario debe tener al menos 3 caracteres'),
    password: z.string().min(4, 'La contraseña es requerida'),
});

export type LoginFormValues = z.infer<typeof loginSchema>;