import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { loginSchema, type LoginFormValues } from '../schemas/login';
import { useLogin } from '../hooks/use-login';
import { useAuth } from '../context/auth-context';
import { styles } from './loginStyle';

import logoAcoasmi from '@/assets/Roble-RL.jpg';

export function LoginForm() {
    const navigate = useNavigate();
    const { isAuthenticated } = useAuth();
    const { mutateAsync: executeLogin, isPending } = useLogin();

    // Redirección si la sesión ya se encuentra activa
    useEffect(() => {
        if (isAuthenticated) {
            navigate('dashboard', { replace: true });
        }
    }, [isAuthenticated, navigate]);

    const form = useForm<LoginFormValues>({
        resolver: zodResolver(loginSchema),
        defaultValues: {
            usuario: '',
            password: '',
        },
    });

    const onSubmit = async (values: LoginFormValues) => {
        try {
            await executeLogin({
                usuario: values.usuario,
                password: values.password,
            });
            // Redirección inmediata al dashboard tras procesar las credenciales
            navigate('/dashboard', { replace: true });
        } catch (error) {
            console.error('Error durante el inicio de sesión:', error);
        }
    };

    return (
        <div style={styles.container}>
            <div style={styles.card}>

                {/* Panel Izquierdo: Presentación */}
                <div style={styles.leftPanel}>
                    <div style={styles.waveDecoration} />

                    {/* Logo grande arriba y Nombre centrado abajo */}
                    <div style={styles.brandHeader}>
                        <img src={logoAcoasmi} alt="Logo ACOASMI" style={styles.logo} />
                        <span style={styles.brandTitle}>ACOASMI DE R.L.</span>
                    </div>

                    <div style={styles.welcomeSection}>
                        <h2 style={styles.welcomeTitle}>¡Bienvenido!</h2>
                        <p style={styles.welcomeDescription}>
                            Accede a tu cuenta para gestionar créditos, aportaciones y servicios financieros de forma rápida y segura.
                        </p>
                    </div>

                    <div style={styles.leftFooter}>
                        Asociación Cooperativa de Ahorro y Crédito El Roble de R.L.
                    </div>
                </div>

                {/* Panel Derecho: Formulario */}
                <div style={styles.rightPanel}>
                    <h3 style={styles.formTitle}>Inicio de Sesión</h3>
                    <p style={styles.formSubtitle}>Ingresa tus credenciales para acceder a la plataforma.</p>

                    <form onSubmit={form.handleSubmit(onSubmit)} style={styles.form}>

                        {/* Campo Usuario */}
                        <div style={styles.inputGroup}>
                            <label style={styles.label}>Usuario</label>
                            <input
                                {...form.register('usuario')}
                                placeholder="Ej. usuario01"
                                autoComplete="username"
                                style={styles.input}
                            />
                            {form.formState.errors.usuario && (
                                <span style={styles.errorMessage}>
                                    {form.formState.errors.usuario.message}
                                </span>
                            )}
                        </div>

                        {/* Campo Contraseña */}
                        <div style={styles.inputGroup}>
                            <label style={styles.label}>Contraseña</label>
                            <input
                                type="password"
                                {...form.register('password')}
                                placeholder="••••••••"
                                autoComplete="current-password"
                                style={styles.input}
                            />
                            {form.formState.errors.password && (
                                <span style={styles.errorMessage}>
                                    {form.formState.errors.password.message}
                                </span>
                            )}
                        </div>

                        {/* Opciones Adicionales */}
                        <div style={styles.optionsRow}>
                            <label style={styles.checkboxLabel}>
                                <input type="checkbox" style={styles.checkbox} />
                                Recordarme
                            </label>
                            <a href="#" style={styles.forgotLink}>¿Olvidaste tu contraseña?</a>
                        </div>

                        {/* Botón Acción */}
                        <button
                            type="submit"
                            disabled={isPending}
                            style={{
                                ...styles.submitButton,
                                opacity: isPending ? 0.7 : 1,
                                cursor: isPending ? 'not-allowed' : 'pointer'
                            }}
                        >
                            {isPending ? 'CARGANDO...' : 'Ingresar'}
                        </button>
                    </form>

                    <div style={styles.signupFooter}>
                        ¿No tienes cuenta?{' '}
                        <a href="#" style={styles.supportLink}>Contacta al Soporte</a>
                    </div>
                </div>

            </div>
        </div>
    );
}