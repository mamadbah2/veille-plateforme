# Source Implementation - Résumé des discussions

## Vue d'ensemble

Ce document résume les décisions prises pour l'implémentation des Sources dans la plateforme de veille.

---

## Entité Source - Champs à ajouter (23 nouveaux)

### Authentification
| Champ | Type | Description |
|-------|------|-------------|
| `apiKey` | String | Clé API pour sources authentifiées |
| `headers` | Map<String,String> | En-têtes HTTP personnalisés |

### Gestion des erreurs
| Champ | Type | Description |
|-------|------|-------------|
| `lastError` | String | Dernière erreur rencontrée |
| `lastErrorAt` | LocalDateTime | Date de la dernière erreur |
| `consecutiveFailures` | int | Nombre d'échecs consécutifs |

### Configuration
| Champ | Type | Description |
|-------|------|-------------|
| `langue` | String | Langue (FR, EN) |
| `priorite` | int | Priorité de scraping (1-10) |
| `categorieParDefaut` | String | Catégorie par défaut |
| `timeout` | int | Timeout en secondes |
| `retryCount` | int | Nombre de tentatives |
| `rateLimitPerMinute` | int | Limite de requêtes/minute |
| `maxArticlesPerSync` | int | Max articles par synchro |

### Métadonnées UI
| Champ | Type | Description |
|-------|------|-------------|
| `description` | String | Description de la source |
| `logoUrl` | String | URL du logo |
| `trustScore` | int | Score de fiabilité (1-10) |
| `verified` | boolean | Source validée par admin |
| `sourceType` | SourceType | OFFICIAL, MEDIA, BLOG, COMMUNITY, UNKNOWN |

### Statistiques
| Champ | Type | Description |
|-------|------|-------------|
| `totalArticlesCollected` | long | Total articles collectés |
| `articlesLastSync` | int | Articles dernière synchro |
| `nextSyncAt` | LocalDateTime | Prochaine synchro prévue |

### Scraping HTML
| Champ | Type | Description |
|-------|------|-------------|
| `selectorTitle` | String | Sélecteur CSS pour titre |
| `selectorContent` | String | Sélecteur CSS pour contenu |
| `selectorDate` | String | Sélecteur CSS pour date |

---

## Système de vérification de crédibilité

### TrustScore
Score manuel de 1-10 attribué par l'admin.

### SourceType (enum)
```java
OFFICIAL   → NIST, CISA, CERT-FR
MEDIA      → TechCrunch, Wired
BLOG       → Krebs, Schneier
COMMUNITY  → Reddit, Hacker News
UNKNOWN    → Non vérifié
```

### Cross-Referencing (Phase 4)
- Comparer articles entre sources
- Si plusieurs sources parlent du même sujet → plus crédible
- Champs Article : `relatedArticleIds`, `credibilityScore`

---

## Classification automatique

### ScrapingService.categorizeArticle()
- Analyse le contenu de chaque article
- Utilise LM Studio (IA locale)
- Retourne : catégorie, tags, gravité

### Pourquoi ?
La `categorieParDefaut` de la source n'est qu'une suggestion.
L'IA vérifie le contenu réel de l'article.

---

## Phases d'implémentation

1. **Phase 1** : Modifier Source.java (23 champs)
2. **Phase 2** : Implémenter ScrapingService
3. **Phase 3** : Classification IA (LM Studio)
4. **Phase 4** : Cross-referencing

---

## Fichiers à modifier

- `Source.java` - Entité
- `SourceRequest.java` - DTO requête
- `SourceResponse.java` - DTO réponse
- `ScrapingServiceImpl.java` - Logique scraping
