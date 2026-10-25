/**
 * Telemetry Ingestion & Real-Time Event Simulator
 */
import { initialVehicles } from '../data/initialVehicles.js';

class TelemetryService {
  constructor() {
    this.vehicles = [...initialVehicles];
    this.subscribers = new Set();
    this.timer = null;
  }

  /**
   * Subscribe to live telematics heartbeats
   * @param {Function} callback
   * @returns {Function} unsubscribe function
   */
  subscribe(callback) {
    this.subscribers.add(callback);
    return () => this.subscribers.delete(callback);
  }

  startSimulation(intervalMs = 3000) {
    if (this.timer) return;
    this.timer = setInterval(() => {
      this.vehicles = this.vehicles.map((veh) => {
        if (veh.status === 'In Transit') {
          // Micro-fluctuation in speed and fuel
          const speedDelta = (Math.random() - 0.5) * 4;
          const newSpeed = Math.max(40, Math.min(85, veh.speed + speedDelta));
          return {
            ...veh,
            speed: Math.round(newSpeed),
            lastPing: new Date().toISOString()
          };
        }
        return veh;
      });

      this.subscribers.forEach((cb) => cb(this.vehicles));
    }, intervalMs);
  }

  stopSimulation() {
    if (this.timer) {
      clearInterval(this.timer);
      this.timer = null;
    }
  }

  getLiveVehicles() {
    return this.vehicles;
  }
}

export const telemetryService = new TelemetryService();
export default telemetryService;
