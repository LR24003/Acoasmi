import React from 'react';

const isMobile = typeof window !== 'undefined' && (window.innerWidth <= 768 || window.innerHeight < 600);

export const styles: Record<string, React.CSSProperties> = {
    container: {
        position: 'fixed',
        top: 0,
        left: 0,
        right: 0,
        bottom: 0,
        width: '100vw',
        height: '100vh',
        background: 'linear-gradient(135deg, #059669 0%, #10B981 50%, #34D399 100%)',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        padding: isMobile ? '12px' : '20px',
        boxSizing: 'border-box',
        overflow: 'hidden',
        zIndex: 9999,
        fontFamily: 'system-ui, -apple-system, sans-serif'
    },
    card: {
        width: '100%',
        maxWidth: '900px',
        backgroundColor: '#ffffff',
        borderRadius: '16px',
        boxShadow: '0 20px 25px -5px rgba(0, 0, 0, 0.1)',
        display: 'flex',
        flexDirection: isMobile ? 'column' : 'row',
        overflow: 'hidden',
        maxHeight: isMobile ? '95vh' : '90vh',
        overflowY: isMobile ? 'auto' : 'hidden',
        border: '1px solid #E2E8F0'
    },
    leftPanel: {
        flex: isMobile ? 'none' : '1 1 380px',
        width: isMobile ? '100%' : 'auto',
        background: 'linear-gradient(135deg, #06B6D4 0%, #3B82F6 100%)',
        padding: isMobile ? '24px 20px' : '36px',
        color: '#000000',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'space-between',
        alignItems: 'center',
        position: 'relative',
        boxSizing: 'border-box',
        overflow: 'hidden',
        gap: isMobile ? '16px' : '0'
    },
    waveDecoration: {
        position: 'absolute',
        bottom: '-50px',
        left: '-50px',
        width: '250px',
        height: '250px',
        borderRadius: '50%',
        backgroundColor: 'rgba(255, 255, 255, 0.1)',
        pointerEvents: 'none'
    },
    brandHeader: {
        display: 'flex',
        flexDirection: 'column', // Apila la imagen y el texto verticalmente
        alignItems: 'center',    // Centra el logo y el texto
        justifyContent: 'center',
        gap: '12px',
        zIndex: 1,
        width: '100%',
        textAlign: 'center'
    },
    logo: {
        width: isMobile ? '70px' : '90px',   // Logo más grande
        height: isMobile ? '70px' : '90px',  // Logo más grande
        borderRadius: '50%',
        objectFit: 'contain',
        backgroundColor: '#ffffff',
        padding: '6px',
        boxShadow: '0 4px 14px rgba(0, 0, 0, 0.15)'
    },
    brandTitle: {
        fontSize: isMobile ? '18px' : '20px',
        fontWeight: '800',
        letterSpacing: '0.8px',
        color: '#ffffff'
    },
    welcomeSection: {
        zIndex: 1,
        margin: isMobile ? '12px 0' : '20px 0',
        textAlign: 'center' // Centra los textos de bienvenida
    },
    welcomeTitle: {
        fontSize: isMobile ? '24px' : '32px',
        fontWeight: '800',
        margin: '0 0 8px 0',
        lineHeight: 1.1
    },
    welcomeDescription: {
        fontSize: isMobile ? '13px' : '14px',
        lineHeight: 1.4,
        opacity: 0.9,
        margin: 0
    },
    leftFooter: {
        fontSize: '11px',
        opacity: 0.8,
        zIndex: 1,
        textAlign: 'center'
    },
    rightPanel: {
        flex: isMobile ? 'none' : '1 1 420px',
        width: isMobile ? '100%' : 'auto',
        padding: isMobile ? '24px 20px' : '40px',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        boxSizing: 'border-box',
        backgroundColor: '#ffffff'
    },
    formTitle: {
        fontSize: isMobile ? '24px' : '30px',
        fontWeight: '700',
        color: '#2563EB',
        margin: '0 0 4px 0'
    },
    formSubtitle: {
        fontSize: '12px',
        color: '#94A3B8',
        margin: '0 0 20px 0'
    },
    form: {
        display: 'flex',
        flexDirection: 'column',
        gap: '14px'
    },
    inputGroup: {
        display: 'flex',
        flexDirection: 'column',
        gap: '6px'
    },
    label: {
        fontSize: '12px',
        fontWeight: '600',
        color: '#64748B'
    },
    input: {
        height: '42px',
        padding: '0 14px',
        borderRadius: '8px',
        border: '1.5px solid #CBD5E1',
        fontSize: '14px',
        outline: 'none',
        color: '#1E293B',
        width: '100%',
        boxSizing: 'border-box'
    },
    errorMessage: {
        fontSize: '11px',
        color: '#EF4444'
    },
    optionsRow: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        fontSize: '12px',
        flexWrap: 'wrap',
        gap: '8px'
    },
    checkboxLabel: {
        display: 'flex',
        alignItems: 'center',
        gap: '6px',
        color: '#64748B',
        cursor: 'pointer'
    },
    checkbox: {
        accentColor: '#3B82F6',
        width: '16px',
        height: '16px'
    },
    forgotLink: {
        color: '#3B82F6',
        textDecoration: 'none',
        fontWeight: '500'
    },
    submitButton: {
        height: '42px',
        borderRadius: '8px',
        backgroundColor: '#3B82F6',
        color: '#ffffff',
        border: 'none',
        fontWeight: '700',
        fontSize: '14px',
        cursor: 'pointer',
        letterSpacing: '0.5px',
        marginTop: '6px',
        boxShadow: '0 4px 12px rgba(59, 130, 246, 0.3)',
        width: '100%'
    },
    signupFooter: {
        marginTop: '16px',
        textAlign: 'center',
        fontSize: '12px',
        color: '#94A3B8'
    },
    supportLink: {
        color: '#3B82F6',
        textDecoration: 'none',
        fontWeight: '700'
    }
};