/**
 * Geospatial Utilities & Geofencing Math
 */

/**
 * Calculate Great-Circle distance between two coordinates using Haversine formula
 * @param {number} lat1
 * @param {number} lon1
 * @param {number} lat2
 * @param {number} lon2
 * @returns {number} distance in kilometers
 */
export function calculateDistanceKm(lat1, lon1, lat2, lon2) {
  const R = 6371; // Earth's radius in km
  const dLat = ((lat2 - lat1) * Math.PI) / 180;
  const dLon = ((lon2 - lon1) * Math.PI) / 180;
  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos((lat1 * Math.PI) / 180) *
      Math.cos((lat2 * Math.PI) / 180) *
      Math.sin(dLon / 2) *
      Math.sin(dLon / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  return Math.round(R * c * 10) / 10;
}

/**
 * Check if a vehicle coordinate is within a geofence radius
 * @param {[number, number]} point [lat, lon]
 * @param {[number, number]} center [lat, lon]
 * @param {number} radiusKm
 * @returns {boolean}
 */
export function isWithinGeofence(point, center, radiusKm = 5) {
  return calculateDistanceKm(point[0], point[1], center[0], center[1]) <= radiusKm;
}
