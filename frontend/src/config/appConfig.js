/**
 * FleetTrack Logistics OS - Core App Configuration
 * Centralized settings for telemetry refresh, map endpoints, and hub hubs.
 */

export const appConfig = {
  appName: 'FleetTrack Logistics OS',
  version: '2.4.0',
  telematics: {
    pollIntervalMs: 3000,
    heartbeatTimeoutMs: 15000,
    speedThresholdKmph: 80,
    lowFuelThresholdPct: 15,
  },
  depotHubs: [
    { code: 'PN', name: 'Pune Origin Depot', lat: 18.5204, lng: 73.8567, color: '#2563eb' },
    { code: 'MB', name: 'Mumbai JNPT Port', lat: 18.9496, lng: 72.9510, color: '#0f172a' },
    { code: 'NK', name: 'Nashik MIDC Terminal', lat: 19.9975, lng: 73.7898, color: '#475569' }
  ],
  mapDefaults: {
    center: [18.78, 73.40],
    zoom: 9,
    tileLayer: 'https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png',
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>'
  },
  auth: {
    storageKey: 'fleettrack_logged_in',
    userStorageKey: 'fleettrack_user_profile',
    defaultUser: {
      name: 'Sakshi Kumavat',
      role: 'System Administrator & Fleet Operations Lead',
      email: 'sakshi.kumavat@fleettrack.io'
    }
  }
};

export default appConfig;
