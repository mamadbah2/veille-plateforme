# Dev A — TASKS

> Coche au fur et à mesure. Une tâche = une PR.

---

## 🟦 Sprint 1 — Fondations & Articles

### A1. Service `articles.ts`
- [ ] Créer `frontend/services/articles.ts`
- [ ] Fonctions exportées :
  - `listArticles(page, size): Promise<PageResponse<Article>>`
  - `getArticle(id): Promise<Article>`
  - `getTrending(): Promise<Article[]>`
  - `getWeeklySummary(): Promise<...>`
  - `getByGravite(level, page, size)`, `getByCategorie(id, page, size)`, `getBySource(id, page, size)`
- [ ] Toutes les fonctions utilisent l'instance `api` de `services/api.ts`
- [ ] Ajouter dans `types.ts` (sous `// --- Content ---`) : `PageResponse<T>`, `Article` (synchroniser avec le DTO backend)

**Critère done** : `await listArticles(0, 10)` renvoie une vraie liste depuis MongoDB.

---

### A2. Hooks React Query
- [ ] `hooks/useArticles.ts` → `useArticles(page, size)`
- [ ] `hooks/useArticle.ts` → `useArticle(id)`
- [ ] `hooks/useTrending.ts` → `useTrending()`
- [ ] Convention `queryKey` : `['articles', { page, size }]`, `['article', id]`, `['articles', 'trending']`

---

### A3. Page Home (`/`)
- [ ] Créer `pages/Home.tsx` qui rend l'ancien `ExploreView` mais avec données réelles
- [ ] Brancher `useArticles` pour la liste principale
- [ ] Brancher `useTrending` pour la section trending
- [ ] Loading state (skeleton ou spinner)
- [ ] Error state (message retry)
- [ ] Ajouter route `<Route path="/" element={<Home />} />` dans `App.tsx`
- [ ] **Supprimer `MOCK_ARTICLES`** de `App.tsx` une fois branché

**Critère done** : `http://localhost:3000/` affiche les vrais articles du backend.

---

### A4. Page détail article (`/article/:id`)
- [ ] Créer `pages/Article.tsx`
- [ ] Récupérer `id` via `useParams`
- [ ] Brancher `useArticle(id)`
- [ ] Réutiliser/adapter `ArticleDetailView`
- [ ] Bouton retour qui fait `navigate(-1)`
- [ ] Click sur ArticleCard dans Home → `navigate('/article/' + id)`

---

## 🟦 Sprint 2 — Recherche & Filtres

### A5. Service & hook recherche
- [ ] `services/search.ts` : `searchSpotlight(query, filters)`
- [ ] `hooks/useSearch.ts` avec debounce 300ms (`useDeferredValue` ou `lodash.debounce`)

### A6. Page Search (`/search`)
- [ ] Créer `pages/Search.tsx` avec barre + résultats live
- [ ] Suggestions via `/search/spotlight`
- [ ] Mettre à jour `SearchOverlay` ou créer page dédiée (préférence : page)
- [ ] URL synchronisée : `?q=foo&category=bar` (utiliser `useSearchParams`)

### A7. Page Archive (`/archive`)
- [ ] Créer `pages/Archive.tsx`
- [ ] Filtres : catégorie, gravité, source, période
- [ ] Pagination (bouton "voir plus" → `setPage(p+1)`)
- [ ] Brancher `getByCategorie`, `getByGravite`, `getBySource`

### A8. Routes filtre
- [ ] `/categorie/:id` → `pages/Archive.tsx` avec filtre pré-rempli
- [ ] `/gravite/:level` → idem

---

## 🟦 Sprint 3 — Stories & IA

### A9. Service & page Stories
- [ ] `services/stories.ts` : `listStories(page, size)`, `getStory(id)`
- [ ] `pages/Stories.tsx` : liste des clusters
- [ ] Click sur une story → afficher les articles qui la composent

### A10. Backend — Endpoint proxy IA
- [ ] Créer `web/controllers/AiProxyController.java` (interface)
- [ ] Créer `web/controllers/implementation/AiProxyControllerImpl.java`
- [ ] Endpoint `POST /api/v1/ai/summary` body `{ text: string }` → renvoie résumé en stream
- [ ] Utiliser le `OpenAICompatibleService` existant (déjà configuré pour LM Studio)
- [ ] DTO request/response sous `web/dto/`

**Critère done** : `curl -X POST http://localhost:4040/api/v1/ai/summary -d '{"text":"..."}'` renvoie un résumé.

### A11. Migrer AiSummaryOverlay côté frontend
- [ ] **Supprimer** la dépendance `@google/genai` côté client (retirer du `package.json` et de l'`index.html`)
- [ ] `services/ai.ts` : `streamSummary(text): AsyncIterable<string>` qui appelle l'endpoint backend
- [ ] Adapter `AiSummaryOverlay.tsx` pour consommer le service backend

---

## 🟦 Sprint 4 — Polish

### A12. Loading states & skeletons
- [ ] Composant `<ArticleCardSkeleton />`
- [ ] Skeleton sur Home, Archive, Search

### A13. Error boundary + 404
- [ ] `components/ErrorBoundary.tsx`
- [ ] `pages/NotFound.tsx`
- [ ] Route `<Route path="*" element={<NotFound />} />`

### A14. Tests
- [ ] Vitest sur 1-2 hooks (`useArticles`)
- [ ] Test de rendu sur `ArticleCard` + `Home` (mock du fetch)

---

## ✅ Definition of Done globale

Avant de marquer le périmètre Dev A "fini" :
- [ ] Aucun `MOCK_ARTICLES` dans le code
- [ ] Aucun `useState` qui stocke des articles (tout passe par React Query)
- [ ] Aucune ref directe à `@google/genai` côté frontend
- [ ] Toutes les pages ont loading + error states
- [ ] Au moins une route fonctionne avec un refresh navigateur (preuve que React Router est bien câblé)
