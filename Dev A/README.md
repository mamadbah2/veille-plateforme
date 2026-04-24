# Dev A — Content & Discovery

**Périmètre** : tout ce qui affiche du contenu public (articles, recherche, stories, archives, IA).
**Pas ton job** : auth, login, JWT, profil, favoris, notifications, sécurité backend.

## Branche Git
Travaille sur des branches préfixées `feat/content/...` (ex: `feat/content/home`, `feat/content/search`).

## Variables d'environnement
Tu hérites du `.env` créé en Phase 0 :
- `VITE_API_URL=http://localhost:4040/api/v1`

## Endpoints backend que tu vas consommer
| Méthode | URL | Usage |
|---|---|---|
| GET | `/articles?page=0&size=20` | Liste paginée |
| GET | `/articles/{id}` | Détail |
| GET | `/articles/trending` | Tendances |
| GET | `/articles/weekly-summary` | Résumé hebdo |
| GET | `/articles/gravite/{level}` | Filtre gravité |
| GET | `/articles/categorie/{id}` | Filtre catégorie |
| GET | `/articles/source/{id}` | Filtre source |
| POST | `/search/spotlight` | Recherche hybride |
| GET | `/stories` | Clusters |
| GET | `/categories` | Liste catégories |
| GET | `/sources` | Liste sources |
| POST | `/ai/summary` | **À créer côté backend** — proxy vers LM Studio |

## Fichiers que TU possèdes (tu peux modifier librement)
```
frontend/pages/Home.tsx
frontend/pages/Article.tsx
frontend/pages/Search.tsx
frontend/pages/Archive.tsx
frontend/pages/Stories.tsx
frontend/services/articles.ts
frontend/services/search.ts
frontend/services/stories.ts
frontend/services/ai.ts
frontend/services/categories.ts
frontend/services/sources.ts
frontend/hooks/useArticles.ts
frontend/hooks/useArticle.ts
frontend/hooks/useSearch.ts
frontend/hooks/useStories.ts
frontend/hooks/useTrending.ts
frontend/components/ArticleCard.tsx
frontend/components/ExploreView.tsx
frontend/components/ArchiveView.tsx
frontend/components/ArticleDetailView.tsx
frontend/components/SearchOverlay.tsx
frontend/components/AiSummaryOverlay.tsx
frontend/components/RightPanel.tsx
frontend/components/TopicCustomizationOverlay.tsx
backend/src/main/java/sn/ssi/veille/web/controllers/AiProxyController.java (nouveau)
backend/src/main/java/sn/ssi/veille/web/controllers/implementation/AiProxyControllerImpl.java (nouveau)
```

## Fichiers PARTAGÉS (coordonner avec Dev B avant push)
- `frontend/App.tsx` — ajouter tes `<Route>` en haut du fichier, **ordre alphabétique**, une route par ligne
- `frontend/services/api.ts` — **read-only**, c'est Dev B qui le modifie
- `frontend/types.ts` — ajouter tes types sous `// --- Content ---`
- `frontend/components/Header.tsx`, `Sidebar.tsx`, `BottomNav.tsx` — tu y mets les liens nav contenu (Home, Search, Archive, Stories)

## TODO list ordonnée

Voir `TASKS.md` pour les tâches détaillées avec critères de done.
