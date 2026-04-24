# Dev B — Users, Auth & Admin

**Périmètre** : sécurité backend + tout ce qui nécessite un user connecté (login, register, OAuth, profil, favoris, notifications, admin).
**Pas ton job** : afficher les articles, recherche, stories, IA, archive.

## Branche Git
Travaille sur des branches préfixées `feat/auth/...`, `feat/user/...`, `feat/admin/...` (ex: `feat/auth/login`).

## Variables d'environnement
Tu hérites du `.env` créé en Phase 0 :
- `VITE_API_URL=http://localhost:4040/api/v1`
- `VITE_AUTH_URL=http://localhost:4040/api`

⚠️ Côté backend, vérifier que `MONGO_PASS`, `GOOGLE_CLIENT_*`, `GITHUB_CLIENT_*` sont bien dans `backend/.env` (voir `backend/.env.example`).

## Endpoints backend que tu vas consommer
| Méthode | URL | Usage |
|---|---|---|
| POST | `/api/login` | Connexion email/password |
| POST | `/api/register` | Inscription |
| POST | `/api/refresh` | Refresh JWT |
| POST | `/api/logout` | Déconnexion |
| GET | `/api/v1/users/me` | Profil courant |
| POST | `/api/v1/users/{id}/enable` | Admin |
| POST | `/api/v1/users/{id}/disable` | Admin |
| GET/POST/DELETE | `/api/v1/favoris` | Favoris user |
| GET | `/api/v1/notifications` | Notifs user |
| ALL | `/api/v1/sources` | Admin sources |
| POST | `/api/v1/scraping/run` | Admin trigger scraping |

OAuth2 callbacks Google/GitHub : `http://localhost:4040/login/oauth2/code/{google|github}` (géré par Spring), redirige ensuite vers `http://localhost:3000/auth/callback?token=...`.

## Fichiers que TU possèdes (tu peux modifier librement)
```
frontend/pages/Login.tsx
frontend/pages/Register.tsx
frontend/pages/AuthCallback.tsx
frontend/pages/Profile.tsx
frontend/pages/Bookmarks.tsx
frontend/pages/Notifications.tsx
frontend/pages/AdminSources.tsx (optionnel)
frontend/services/auth.ts
frontend/services/user.ts
frontend/services/favoris.ts
frontend/services/notifications.ts
frontend/services/sources.admin.ts
frontend/contexts/AuthContext.tsx
frontend/hooks/useAuth.ts
frontend/hooks/useFavoris.ts
frontend/hooks/useNotifications.ts
frontend/components/ProtectedRoute.tsx
frontend/components/ProfileView.tsx
frontend/components/BookmarksView.tsx
frontend/components/NotificationsOverlay.tsx
frontend/components/NotificationsFullView.tsx
frontend/components/SettingsOverlay.tsx
frontend/services/api.ts (interceptor JWT — c'est toi qui possèdes ce fichier)
backend/src/main/java/sn/ssi/veille/config/SecurityConfig.java
backend/src/main/java/sn/ssi/veille/config/WebConfig.java (CORS — créer si absent)
```

## Fichiers PARTAGÉS (coordonner avec Dev A avant push)
- `frontend/App.tsx` — ajouter tes `<Route>` (login, register, profile, etc.) **ordre alphabétique**, une route par ligne
- `frontend/types.ts` — ajouter tes types sous `// --- Auth ---`
- `frontend/components/Header.tsx`, `Sidebar.tsx` — bouton Login/Logout, avatar utilisateur
- `frontend/components/BottomNav.tsx` — onglet Profile, Bookmarks

## TODO list ordonnée

Voir `TASKS.md`.

## ⚠️ Sécurité — TODO immédiat

Les **GitHub OAuth credentials** ont été commit en clair dans l'historique git (`backend/src/main/resources/application.properties` ligne 36 avant Phase 0). **Action requise** :
1. Aller sur GitHub → Settings → Developer settings → OAuth Apps → trouver l'app Horus
2. Régénérer le client secret
3. Mettre la nouvelle valeur dans `backend/.env` (ne pas committer)
