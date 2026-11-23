const CACHE = 'avf-installed-app-v3';
const APP_ASSETS = ['./', './index.html', './manifest.webmanifest', './icons/icon-192.png', './icons/icon-512.png'];
self.addEventListener('install', event => {
  event.waitUntil(caches.open(CACHE).then(cache => cache.addAll(APP_ASSETS)).then(() => self.skipWaiting()));
});
self.addEventListener('activate', event => {
  event.waitUntil(caches.keys().then(keys => Promise.all(keys.filter(key => key.startsWith('avf-installed-app-') && key !== CACHE).map(key => caches.delete(key)))).then(() => self.clients.claim()));
});
self.addEventListener('fetch', event => {
  if (event.request.method !== 'GET') return;
  const url = new URL(event.request.url);
  if (url.origin !== self.location.origin) return;
  // Always prefer current published game files, bypassing a stale HTTP cache; keep offline fallback.
  event.respondWith(fetch(event.request, {cache: 'reload'}).then(response => {
    if (response.ok) {
      const copy = response.clone();
      caches.open(CACHE).then(cache => cache.put(event.request.mode === 'navigate' ? './index.html' : event.request, copy));
    }
    return response;
  }).catch(async () => {
    return await caches.match(event.request) || (event.request.mode === 'navigate' ? await caches.match('./index.html') : Response.error());
  }));
});
