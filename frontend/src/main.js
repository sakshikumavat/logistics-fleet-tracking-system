/**
 * FleetTrack Logistics OS - Core Entry Point
 */
import './styles/main.css';
export * from './config/theme.js';
export * from './config/appConfig.js';
export * from './modules/auth.js';
export * from './modules/telematics.js';
export * from './modules/mapEngine.js';
export * from './services/storageService.js';
export * from './services/apiService.js';
export * from './services/telemetryService.js';
export * from './utils/formatters.js';
export * from './utils/geo.js';
export * from './utils/dom.js';

console.info(
  '%c FleetTrack Logistics OS %c v2.4.0 (AIS-140 Compliant) ',
  'background:#004ac6;color:#ffffff;font-weight:bold;padding:3px 6px;border-radius:4px 0 0 4px;',
  'background:#0f172a;color:#60a5fa;font-weight:bold;padding:3px 6px;border-radius:0 4px 4px 0;'
);
