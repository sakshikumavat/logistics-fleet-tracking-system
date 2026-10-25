/**
 * DOM Helper Utilities
 */

/**
 * Initialize a dropdown menu attached to a button trigger
 * @param {string} triggerId
 * @param {string} menuId
 */
export function setupDropdown(triggerId, menuId) {
  const trigger = document.getElementById(triggerId);
  const menu = document.getElementById(menuId);

  if (!trigger || !menu) return;

  trigger.addEventListener('click', (e) => {
    e.stopPropagation();
    menu.classList.toggle('hidden');
  });

  document.addEventListener('click', (e) => {
    if (!menu.contains(e.target) && !trigger.contains(e.target)) {
      menu.classList.add('hidden');
    }
  });
}

/**
 * Display a temporary floating toast alert
 * @param {string} message
 * @param {'success'|'warning'|'error'} type
 */
export function showToast(message, type = 'success') {
  const toast = document.createElement('div');
  const bgClass =
    type === 'error'
      ? 'bg-red-600 text-white'
      : type === 'warning'
      ? 'bg-amber-500 text-slate-900'
      : 'bg-emerald-600 text-white';

  toast.className = `fixed bottom-5 right-5 z-50 flex items-center gap-2 px-4 py-2.5 rounded-xl shadow-lg font-medium text-xs transition-all duration-300 transform translate-y-2 opacity-0 ${bgClass}`;
  toast.textContent = message;

  document.body.appendChild(toast);

  requestAnimationFrame(() => {
    toast.classList.remove('translate-y-2', 'opacity-0');
  });

  setTimeout(() => {
    toast.classList.add('opacity-0', 'translate-y-2');
    setTimeout(() => toast.remove(), 300);
  }, 3500);
}
