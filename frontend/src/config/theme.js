/**
 * FleetTrack Logistics OS - Design System Tokens
 * Reusable theme definition shared across pages and tailwind configuration.
 */

export const themeTokens = {
  colors: {
    brand: {
      50: '#eff6ff',
      100: '#dbeafe',
      200: '#bfdbfe',
      300: '#93c5fd',
      400: '#60a5fa',
      500: '#3b82f6',
      600: '#2563eb',
      700: '#1d4ed8',
      800: '#1e40af',
      900: '#1e3a8a',
    },
    navy: {
      800: '#1e293b',
      900: '#0f172a',
      950: '#090d16',
    },
    primary: {
      DEFAULT: '#004ac6',
      container: '#2563eb',
      surface: '#f8f9ff',
    },
    status: {
      active: '#007d55',
      warning: '#eab308',
      danger: '#ba1a1a',
      info: '#2563eb',
    }
  },
  typography: {
    fontFamily: ['Plus Jakarta Sans', 'Inter', 'system-ui', 'sans-serif'],
  },
  borderRadius: {
    sm: '0.25rem',
    md: '0.5rem',
    lg: '0.75rem',
    xl: '1rem',
    full: '9999px',
  }
};

export default themeTokens;
