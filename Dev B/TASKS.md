# Dev B — TASKS

> Coche au fur et à mesure. Une tâche = une PR.

---

## 🟥 Sprint 0 — Sécurité critique (URGENT, à faire EN PREMIER)

### B0. Régénérer les secrets GitHub
- [ ] Régénérer le `GITHUB_CLIENT_SECRET` sur github.com (ancien value compromis dans l'historique git)
- [ ] Mettre à jour `backend/.env` avec les nouvelles valeurs (pas dans `application.properties`)
- [ ] Vérifier que `backend/.env` est bien dans `.gitignore`

### B1. Configurer Spring Security correctement
Fichier : `backend/src/main/java/sn/ssi/veille/config/SecurityConfig.java`

- [ ] Remplacer `.anyRequest().permitAll()` par des règles explicites :
  ```java
  .authorizeHttpRequests(auth -> auth
      .requestMatchers("/api/login", "/api/register", "/api/refresh").permitAll()
      .requestMatchers("/login/oauth2/**", "/oauth2/**").permitAll()
      .requestMatchers(HttpMethod.GET, "/api/v1/articles/**").permitAll()
      .requestMatchers(HttpMethod.GET, "/api/v1/categories", "/api/v1/sources").permitAll()
      .requestMatchers(HttpMethod.POST, "/api/v1/search/**").permitAll()
      .requestMatchers(HttpMethod.GET, "/api/v1/stories/**").permitAll()
      .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
      .requestMatchers("/api/v1/scraping/**").hasAuthority("ADMIN")
      .requestMatchers("/api/v1/users/*/enable", "/api/v1/users/*/disable").hasAuthority("ADMIN")
      .anyRequest().authenticated()
  )
  ```
- [ ] Activer CORS proprement (whitelist `http://localhost:3000`) — créer `WebConfig.java` si absent

**Critère done** :
- `curl http://localhost:4040/api/v1/articles` → 200 (public)
- `curl http://localhost:4040/api/v1/users/me` → 401 sans token
- `curl http://localhost:4040/api/v1/users/me -H "Authorization: Bearer <token>"` → 200

---

## 🟥 Sprint 1 — Auth frontend

### B2. Service auth + interceptor
- [ ] Compléter `frontend/services/api.ts` :
  - Request interceptor : ajouter `Authorization: Bearer ${token}` depuis localStorage
  - Response interceptor : sur 401, tenter `POST /api/refresh`, retry l'appel ; sur échec, redirect `/login`
- [ ] Créer `frontend/services/auth.ts` :
  - `login(email, password)` → `POST /api/login`, stocke token
  - `register(email, password, name)` → `POST /api/register`
  - `refresh()` → `POST /api/refresh`
  - `logout()` → `POST /api/logout`, clear localStorage
- [ ] Créer `frontend/services/user.ts` : `getMe()` → `GET /api/v1/users/me`

### B3. AuthContext
- [ ] Créer `contexts/AuthContext.tsx` qui expose `{ user, token, login, logout, isAuthenticated, loading }`
- [ ] Au mount : si token existe → fetch `/users/me`, sinon `loading=false`
- [ ] Wrapper `<AuthProvider>` à ajouter dans `index.tsx` (autour de `<App />`, à l'intérieur de `QueryClientProvider`)
- [ ] Hook `useAuth()` qui throw si hors provider

### B4. Pages Login & Register
- [ ] `pages/Login.tsx` : formulaire email/password + boutons OAuth (Google, GitHub)
- [ ] `pages/Register.tsx` : formulaire email/password/nom
- [ ] Validation côté client (email format, password 8+ chars)
- [ ] Affichage erreurs serveur
- [ ] Redirect vers `/` après login réussi

Boutons OAuth : redirige vers `http://localhost:4040/oauth2/authorization/google` (et `/github`), Spring fait le reste.

### B5. AuthCallback
- [ ] `pages/AuthCallback.tsx` à la route `/auth/callback`
- [ ] Récupère `token` depuis l'URL (query param)
- [ ] Stocke dans localStorage, fetch `/users/me`, redirect `/`

### B6. ProtectedRoute
- [ ] `components/ProtectedRoute.tsx` qui :
  - Si `loading` → spinner
  - Si `!isAuthenticated` → `<Navigate to="/login" />`
  - Sinon → `{children}` ou `<Outlet />`
- [ ] Wrapper sur les routes protégées dans `App.tsx`

---

## 🟧 Sprint 2 — User-centric features

### B7. Profile (`/profile`)
- [ ] `pages/Profile.tsx` (route protégée)
- [ ] Affiche `user` du `AuthContext`
- [ ] Bouton logout
- [ ] (option) édition prénom/nom → `PUT /api/v1/users/me` (vérifier dispo backend)

### B8. Bookmarks (`/bookmarks`)
- [ ] `services/favoris.ts` : `list()`, `add(articleId)`, `remove(articleId)`
- [ ] `hooks/useFavoris.ts` (React Query + mutations)
- [ ] `pages/Bookmarks.tsx` (route protégée) — liste articles favoris
- [ ] **Coordination Dev A** : ajouter un bouton bookmark sur `ArticleCard` qui appelle `useFavoris().add/remove` (PR séparée, après que Dev A ait branché ArticleCard)

### B9. Notifications
- [ ] `services/notifications.ts` : `list()`, `markRead(id)`
- [ ] `hooks/useNotifications.ts`
- [ ] Brancher `NotificationsOverlay` et `NotificationsFullView` sur les vraies données
- [ ] Indicateur de non-lus dans `Header`

---

## 🟨 Sprint 3 — Admin (optionnel selon temps)

### B10. AdminSources (`/admin/sources`)
- [ ] Route protégée par `ProtectedRoute` + check role admin
- [ ] CRUD sur `/api/v1/sources`
- [ ] Bouton "trigger scraping" → `POST /api/v1/scraping/run`
- [ ] Affichage `/api/v1/scraping/health`

### B11. Rate limiting backend
- [ ] Ajouter Bucket4j ou équivalent
- [ ] Limites : `/api/login` 5/min/IP, `/api/register` 3/min/IP

---

## 🟦 Sprint 4 — Polish

### B12. Logout dans le UI
- [ ] Bouton dans `Header` (avatar dropdown) ou `Sidebar`
- [ ] Confirmation + appel `logout()` du context

### B13. Tests
- [ ] Vitest sur `AuthContext` (mock fetch)
- [ ] Test du flow login (form submit → token stocké)

---

## ✅ Definition of Done globale

- [ ] `.anyRequest().permitAll()` retiré de `SecurityConfig`
- [ ] Aucun secret en clair dans `application.properties`
- [ ] CORS whitelist explicite (pas `*`)
- [ ] Toute route nécessitant un user a un `<ProtectedRoute>`
- [ ] Refresh token fonctionne (401 → refresh → retry sans déconnecter l'utilisateur)
- [ ] Tester le flow OAuth Google bout en bout (login → callback → user chargé)
