import { resolve } from 'path';
import { defineConfig } from 'vite';

export default defineConfig({
  publicDir: 'public',
  resolve: {
    alias: {
      '@': resolve(__dirname, './src')
    }
  },
  server: {
    port: 3000,
    open: '/login.html',
    host: 'localhost',
    proxy: {
      '/api': {
        target: 'http://localhost:8081',
        changeOrigin: true,
        secure: false
      }
    }
  },
  build: {
    outDir: 'dist',
    sourcemap: false,
    rollupOptions: {
      input: {
        main: resolve(__dirname, 'index.html'),
        fleet: resolve(__dirname, 'fleet.html'),
        addVehicle: resolve(__dirname, 'add-vehicle.html'),
        drivers: resolve(__dirname, 'drivers.html'),
        shipments: resolve(__dirname, 'shipments.html'),
        createShipment: resolve(__dirname, 'create-shipment.html'),
        liveTracking: resolve(__dirname, 'live-tracking.html'),
        reports: resolve(__dirname, 'reports.html'),
        settings: resolve(__dirname, 'settings.html'),
        login: resolve(__dirname, 'login.html'),
        signup: resolve(__dirname, 'signup.html')
      }
    }
  }
});
