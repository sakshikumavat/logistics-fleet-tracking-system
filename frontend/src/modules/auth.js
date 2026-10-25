/**
 * Enterprise Authentication & Access Control Gate
 */
import { storage } from '../services/storageService.js';
import { appConfig } from '../config/appConfig.js';
import { apiService } from '../services/apiService.js';

export const auth = {
  /**
   * Verify if current session is authenticated; redirect to login if unauthenticated
   * @param {string} loginUrl
   */
  enforceAuth(loginUrl = 'login.html') {
    if (!this.isAuthenticated()) {
      window.location.replace(loginUrl);
    }
  },

  /**
   * Check login flag
   * @returns {boolean}
   */
  isAuthenticated() {
    return storage.getItem(appConfig.auth.storageKey) === 'true';
  },

  /**
   * Perform backend authentication with JWT token persistence
   * @param {string} email
   * @param {string} password
   * @param {string} targetUrl
   */
  async loginWithBackend(email, password, targetUrl = 'index.html') {
    try {
      const res = await apiService.auth.login(email, password);
      storage.setItem(appConfig.auth.storageKey, 'true');
      window.location.href = targetUrl;
      return res;
    } catch (err) {
      // Re-throw so caller can display error message in UI
      throw err;
    }
  },

  /**
   * Fallback quick demo login
   * @param {string} email
   * @param {string} targetUrl
   */
  login(email = 'admin@logistics.com', targetUrl = 'index.html') {
    storage.setItem(appConfig.auth.storageKey, 'true');
    storage.setItem(
      appConfig.auth.userStorageKey,
      JSON.stringify({
        ...appConfig.auth.defaultUser,
        email: email || appConfig.auth.defaultUser.email
      })
    );
    window.location.href = targetUrl;
  },

  /**
   * Invalidate session and redirect to login
   * @param {string} redirectUrl
   */
  logout(redirectUrl = 'login.html') {
    apiService.auth.logout();
    storage.removeItem(appConfig.auth.storageKey);
    storage.removeItem(appConfig.auth.userStorageKey);
    window.location.href = redirectUrl;
  },

  /**
   * Retrieve active user profile
   */
  getUser() {
    try {
      const data = storage.getItem(appConfig.auth.userStorageKey);
      return data ? JSON.parse(data) : appConfig.auth.defaultUser;
    } catch (e) {
      return appConfig.auth.defaultUser;
    }
  }
};

export default auth;
