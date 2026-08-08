import React, { Component, ReactNode } from 'react';
import { BrowserRouter } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { AuthProvider } from '@/features/auth/context/auth-context';
import { AppRouter } from '@/routes/AppRouter';
import { Toaster } from '@/components/ui/sonner';

const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            refetchOnWindowFocus: false,
            retry: 1,
        },
    },
});

// Capturador de errores fatal de React
class ErrorBoundary extends Component<{ children: ReactNode }, { hasError: boolean; error: any }> {
    constructor(props: any) {
        super(props);
        this.state = { hasError: false, error: null };
    }

    static getDerivedStateFromError(error: any) {
        return { hasError: true, error };
    }

    componentDidCatch(error: any, errorInfo: any) {
        console.error("Error crítico capturado:", error, errorInfo);
    }

    render() {
        if (this.state.hasError) {
            return (
                <div style={{ padding: '20px', backgroundColor: '#fee2e2', color: '#991b1b', fontFamily: 'monospace', height: '100vh', boxSizing: 'border-box' }}>
                    <h2 style={{ fontSize: '18px', fontWeight: 'bold' }}>⚠️ Ocurrió un error fatal al renderizar la pantalla:</h2>
                    <pre style={{ marginTop: '10px', backgroundColor: '#ffffff', padding: '15px', borderRadius: '8px', overflowX: 'auto', border: '1px solid #fca5a5' }}>
                        {this.state.error?.toString()}
                        {'\n\n'}
                        {this.state.error?.stack}
                    </pre>
                </div>
            );
        }
        return this.props.children;
    }
}

export function App() {
    return (
        <ErrorBoundary>
            <QueryClientProvider client={queryClient}>
                <BrowserRouter>
                    <AuthProvider>
                        <AppRouter />
                        <Toaster position="top-right" richColors />
                    </AuthProvider>
                </BrowserRouter>
            </QueryClientProvider>
        </ErrorBoundary>
    );
}

export default App;