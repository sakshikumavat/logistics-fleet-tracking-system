/**
 * FleetTrack Data Formatters
 */

/**
 * Format Indian Rupee currency (₹)
 * @param {number} amount
 * @returns {string}
 */
export function formatINR(amount) {
  return new Intl.NumberFormat('en-IN', {
    style: 'currency',
    currency: 'INR',
    maximumFractionDigits: 0
  }).format(amount);
}

/**
 * Format Speed in km/h
 * @param {number} speed
 * @returns {string}
 */
export function formatSpeed(speed) {
  return `${Math.round(speed)} km/h`;
}

/**
 * Format Distance in km
 * @param {number} km
 * @returns {string}
 */
export function formatDistance(km) {
  return `${km.toLocaleString('en-IN')} km`;
}

/**
 * Format Fuel percentage
 * @param {number} pct
 * @returns {string}
 */
export function formatFuel(pct) {
  return `${Math.round(pct)}%`;
}

/**
 * Format Date to readable string
 * @param {Date|string} date
 * @returns {string}
 */
export function formatTimestamp(date = new Date()) {
  const d = new Date(date);
  return d.toLocaleTimeString('en-IN', {
    hour: '2-digit',
    minute: '2-digit',
    hour12: true
  });
}
