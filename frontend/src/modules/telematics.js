/**
 * Telematics Corridor & Fleet Monitoring Logic
 */
export const corridorWaypoints = [
  [18.5204, 73.8567], // Pune Origin
  [18.6298, 73.7997], // Pimpri Hub
  [18.7537, 73.4063], // Lonavala Expressway Pass
  [18.8924, 73.1782], // Khalapur Tollway
  [18.9894, 73.1175], // Panvel Inbound Corridor
  [18.9496, 72.9510]  // Mumbai JNPT Terminal
];

export const telematics = {
  getCorridorPoints() {
    return corridorWaypoints;
  },

  getVehicleStatusColor(status) {
    switch (status) {
      case 'On Route':
      case 'In Transit':
        return '#2563eb';
      case 'Stopped':
      case 'Refueling':
        return '#eab308';
      case 'Delayed':
      case 'Alert':
        return '#ba1a1a';
      default:
        return '#64748b';
    }
  }
};

export default telematics;
