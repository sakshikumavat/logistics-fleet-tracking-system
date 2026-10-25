/**
 * Safe LocalStorage abstraction with in-memory fallback
 */
class StorageService {
  constructor() {
    this.memoryStore = new Map();
  }

  isAvailable() {
    try {
      const test = '__storage_test__';
      localStorage.setItem(test, test);
      localStorage.removeItem(test);
      return true;
    } catch (e) {
      return false;
    }
  }

  getItem(key) {
    if (this.isAvailable()) {
      return localStorage.getItem(key);
    }
    return this.memoryStore.get(key) || null;
  }

  setItem(key, value) {
    if (this.isAvailable()) {
      localStorage.setItem(key, value);
    } else {
      this.memoryStore.set(key, String(value));
    }
  }

  removeItem(key) {
    if (this.isAvailable()) {
      localStorage.removeItem(key);
    } else {
      this.memoryStore.delete(key);
    }
  }

  clear() {
    if (this.isAvailable()) {
      localStorage.clear();
    } else {
      this.memoryStore.clear();
    }
  }
}

export const storage = new StorageService();
export default storage;
