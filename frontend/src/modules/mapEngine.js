/**
 * Leaflet Map Telematics Engine Helper
 */
import { appConfig } from '../config/appConfig.js';

export class MapEngine {
  /**
   * Initialize a Leaflet map instance with default settings
   * @param {string} containerId
   * @param {[number, number]} center
   * @param {number} zoom
   */
  static initMap(containerId, center = appConfig.mapDefaults.center, zoom = appConfig.mapDefaults.zoom) {
    if (typeof L === 'undefined') {
      console.warn('Leaflet (L) is not loaded on the window object.');
      return null;
    }

    const container = document.getElementById(containerId);
    if (!container) return null;

    const map = L.map(containerId, {
      zoomControl: false,
      attributionControl: false
    }).setView(center, zoom);

    L.tileLayer(appConfig.mapDefaults.tileLayer, {
      maxZoom: 19,
      subdomains: 'abcd'
    }).addTo(map);

    return map;
  }

  /**
   * Add a logistics depot hub marker to map
   */
  static addHubMarker(map, lat, lng, code, name, color = '#2563eb') {
    if (!map || typeof L === 'undefined') return;

    const icon = L.divIcon({
      className: '',
      html: `
        <div style="display:flex;align-items:center;gap:6px;">
          <div style="width:26px;height:26px;border-radius:9999px;background:${color};color:white;display:flex;align-items:center;justify-content:center;font-weight:bold;font-size:11px;box-shadow:0 3px 8px rgba(0,0,0,0.3);border:2px solid white;">${code}</div>
          <div style="background:rgba(255,255,255,0.95);backdrop-filter:blur(4px);color:#0f172a;font-size:11px;font-weight:700;padding:2px 8px;border-radius:6px;box-shadow:0 2px 6px rgba(0,0,0,0.15);white-space:nowrap;border:1px solid #e2e8f0;">${name}</div>
        </div>
      `,
      iconSize: [140, 30],
      iconAnchor: [13, 15]
    });

    return L.marker([lat, lng], { icon }).addTo(map);
  }
}

export default MapEngine;
