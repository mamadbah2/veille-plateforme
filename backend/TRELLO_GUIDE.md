# 📋 Guide de Répartition des Tâches - Projet Horus

## 🎯 Vue d'ensemble

Ce document présente une stratégie de répartition des tâches pour l'équipe backend du projet **Horus** (Plateforme de Veille Cybersécurité).

### Structure de l'équipe suggérée
- **1 Lead Backend** (vous) : Revue de code, architecture, merge requests
- **3-4 Développeurs Juniors** : Implémentation des services et controllers

---

## 📊 Organisation Trello Recommandée

### Colonnes du Board

| Colonne | Description |
|---------|-------------|
| 📥 **Backlog** | Toutes les tâches à faire |
| 📋 **Sprint Actuel** | Tâches sélectionnées pour le sprint |
| 🔄 **En Cours** | Tâches en développement (max 1-2 par dev) |
| 👀 **Code Review** | En attente de revue par le lead |
| ✅ **Terminé** | Tâches validées et mergées |
| 🚫 **Bloqué** | Tâches avec des blocages identifiés |

### Labels de Priorité

- 🔴 **Critique** : Bloquant pour d'autres tâches
- 🟠 **Haute** : Important pour le sprint
- 🟡 **Moyenne** : Standard
- 🟢 **Basse** : Nice to have

### Labels de Complexité (Story Points)

- **XS (1)** : < 2h de travail
- **S (2)** : 2-4h de travail
- **M (3)** : 4-8h de travail
- **L (5)** : 1-2 jours de travail
- **XL (8)** : > 2 jours de travail

---

## 🗂️ Répartition des Tâches par Module

### Sprint 1 : Fondations (Semaine 1-2)

#### 👤 Junior 1 : Module Authentification
| ID | Tâche | Priorité | Complexité | Dépendances |
|----|-------|----------|------------|-------------|
| AUTH-1 | Implémenter `AuthServiceImpl` | 🔴 Critique | L (5) | - |
| AUTH-2 | Implémenter `AuthControllerImpl` | 🔴 Critique | M (3) | AUTH-1 |
| AUTH-3 | Configurer `JwtService` (génération/validation tokens) | 🔴 Critique | L (5) | - |
| AUTH-4 | Implémenter `SecurityConfig` (filtres JWT) | 🔴 Critique | L (5) | AUTH-3 |
| AUTH-5 | Tests unitaires AuthService | 🟠 Haute | M (3) | AUTH-1 |

**Fichiers à créer :**
- `services/impl/AuthServiceImpl.java`
- `services/JwtService.java`
- `web/controllers/impl/AuthControllerImpl.java`
- `config/SecurityConfig.java`
- `config/JwtAuthenticationFilter.java`

---

#### 👤 Junior 2 : Module Utilisateurs
| ID | Tâche | Priorité | Complexité | Dépendances |
|----|-------|----------|------------|-------------|
| USER-1 | Implémenter `UserServiceImpl` | 🟠 Haute | M (3) | AUTH-1 |
| USER-2 | Implémenter `UserControllerImpl` | 🟠 Haute | M (3) | USER-1 |
| USER-3 | Implémenter `CustomUserDetailsService` | 🔴 Critique | S (2) | - |
| USER-4 | Tests unitaires UserService | 🟡 Moyenne | M (3) | USER-1 |

**Fichiers à créer :**
- `services/impl/UserServiceImpl.java`
- `services/CustomUserDetailsService.java`
- `web/controllers/impl/UserControllerImpl.java`

---

#### 👤 Junior 3 : Modules Sources & Catégories
| ID | Tâche | Priorité | Complexité | Dépendances |
|----|-------|----------|------------|-------------|
| SRC-1 | Implémenter `SourceServiceImpl` | 🟠 Haute | M (3) | - |
| SRC-2 | Implémenter `SourceControllerImpl` | 🟠 Haute | S (2) | SRC-1 |
| CAT-1 | Implémenter `CategorieServiceImpl` | 🟠 Haute | S (2) | - |
| CAT-2 | Implémenter `CategorieControllerImpl` | 🟠 Haute | S (2) | CAT-1 |
| SRC-3 | Tests unitaires Source/Categorie | 🟡 Moyenne | M (3) | SRC-1, CAT-1 |

**Fichiers à créer :**
- `services/impl/SourceServiceImpl.java`
- `services/impl/CategorieServiceImpl.java`
- `web/controllers/impl/SourceControllerImpl.java`
- `web/controllers/impl/CategorieControllerImpl.java`

---

### Sprint 2 : Core Features (Semaine 3-4)

#### 👤 Junior 1 : Module Articles
| ID | Tâche | Priorité | Complexité | Dépendances |
|----|-------|----------|------------|-------------|
| ART-1 | Implémenter `ArticleServiceImpl` (CRUD) | 🔴 Critique | L (5) | SRC-1, CAT-1 |
| ART-2 | Implémenter recherche avec critères | 🔴 Critique | L (5) | ART-1 |
| ART-3 | Implémenter `ArticleControllerImpl` | 🟠 Haute | M (3) | ART-1, ART-2 |
| ART-4 | Implémenter génération résumé hebdomadaire | 🟡 Moyenne | M (3) | ART-1 |
| ART-5 | Tests unitaires ArticleService | 🟡 Moyenne | M (3) | ART-1 |

**Fichiers à créer :**
- `services/impl/ArticleServiceImpl.java`
- `web/controllers/impl/ArticleControllerImpl.java`

---

#### 👤 Junior 2 : Module Favoris
| ID | Tâche | Priorité | Complexité | Dépendances |
|----|-------|----------|------------|-------------|
| FAV-1 | Implémenter `FavorisServiceImpl` | 🟠 Haute | M (3) | ART-1 |
| FAV-2 | Implémenter `FavorisControllerImpl` | 🟠 Haute | S (2) | FAV-1 |
| FAV-3 | Tests unitaires FavorisService | 🟡 Moyenne | S (2) | FAV-1 |

**Fichiers à créer :**
- `services/impl/FavorisServiceImpl.java`
- `web/controllers/impl/FavorisControllerImpl.java`

---

#### 👤 Junior 3 : Module Notifications
| ID | Tâche | Priorité | Complexité | Dépendances |
|----|-------|----------|------------|-------------|
| NOTIF-1 | Implémenter `NotificationServiceImpl` | 🟠 Haute | M (3) | USER-1 |
| NOTIF-2 | Implémenter `NotificationControllerImpl` | 🟠 Haute | S (2) | NOTIF-1 |
| NOTIF-3 | Implémenter job de nettoyage notifications | 🟡 Moyenne | S (2) | NOTIF-1 |
| NOTIF-4 | Tests unitaires NotificationService | 🟡 Moyenne | S (2) | NOTIF-1 |

**Fichiers à créer :**
- `services/impl/NotificationServiceImpl.java`
- `web/controllers/impl/NotificationControllerImpl.java`
- `config/SchedulingConfig.java`

---

### Sprint 3 : Scraping & Bonus (Semaine 5-6)

#### 👤 Junior 1 ou 2 : Module Scraping (Complexe)
| ID | Tâche | Priorité | Complexité | Dépendances |
|----|-------|----------|------------|-------------|
| SCRAP-1 | Implémenter `ScrapingServiceImpl` (base) | 🔴 Critique | XL (8) | SRC-1, ART-1 |
| SCRAP-2 | Implémenter collecte API (Reddit) | 🟠 Haute | L (5) | SCRAP-1 |
| SCRAP-3 | Implémenter collecte RSS | 🟠 Haute | M (3) | SCRAP-1 |
| SCRAP-4 | Implémenter collecte HTML (Jsoup) | 🟠 Haute | L (5) | SCRAP-1 |
| SCRAP-5 | Implémenter catégorisation automatique | 🟡 Moyenne | M (3) | SCRAP-1, CAT-1 |
| SCRAP-6 | Implémenter détection gravité | 🟡 Moyenne | M (3) | SCRAP-1 |
| SCRAP-7 | Job planifié de scraping | 🟠 Haute | M (3) | SCRAP-1 |

**Fichiers à créer :**
- `services/impl/ScrapingServiceImpl.java`
- `services/scraping/ApiScraper.java`
- `services/scraping/RssScraper.java`
- `services/scraping/HtmlScraper.java`

---

#### 👤 Junior 3 : Bonus Features
| ID | Tâche | Priorité | Complexité | Dépendances |
|----|-------|----------|------------|-------------|
| BONUS-1 | Intégration IA pour résumés (LLM) | 🟢 Basse | L (5) | ART-1 |
| BONUS-2 | Implémenter collecte Playwright | 🟢 Basse | XL (8) | SCRAP-1 |
| BONUS-3 | Dashboard monitoring scrapers | 🟢 Basse | L (5) | SCRAP-1 |

---

## 📝 Template de Carte Trello

```
## 📌 [ID] Titre de la Tâche

### 📋 Description
Brève description de ce qui doit être fait.

### 🎯 Critères d'Acceptation
- [ ] Critère 1
- [ ] Critère 2
- [ ] Tests unitaires passent
- [ ] Code review approuvé

### 📁 Fichiers à Modifier/Créer
- `path/to/File1.java`
- `path/to/File2.java`

### 🔗 Dépendances
- Dépend de : [TASK-ID]
- Bloque : [TASK-ID]

### 📚 Ressources
- Lien vers documentation
- Interface à implémenter : `NomInterface.java`

### ⏱️ Estimation
Story Points : X
```

---

## ✅ Checklist avant Merge

Chaque développeur doit vérifier avant de soumettre :

```markdown
- [ ] Code compilé sans erreurs
- [ ] Tests unitaires écrits et passent
- [ ] Javadoc sur les méthodes publiques
- [ ] Pas de TODO laissés dans le code
- [ ] Utilisation des mappers pour les conversions
- [ ] Exceptions appropriées levées
- [ ] Logs ajoutés pour debugging
- [ ] Code formaté (Ctrl+Shift+F)
```

---

## 🔄 Workflow Git

### Branches
```
main (production)
└── develop (intégration)
    ├── feature/AUTH-1-auth-service
    ├── feature/USER-1-user-service
    └── feature/ART-1-article-service
```

### Conventions de Commit
```
feat(auth): implémenter AuthServiceImpl
fix(article): corriger recherche par date
test(user): ajouter tests UserService
docs(api): mettre à jour swagger
refactor(favoris): simplifier logique ajout
```

---

## 📅 Planning Suggéré

| Semaine | Objectif | Livrables |
|---------|----------|-----------|
| 1 | Setup & Auth | Auth fonctionnel avec JWT |
| 2 | Users & Bases | CRUD Users, Sources, Catégories |
| 3 | Articles Core | CRUD Articles, Recherche |
| 4 | Favoris & Notifs | Modules utilisateur complets |
| 5 | Scraping | Collecte automatique fonctionnelle |
| 6 | Tests & Polish | Couverture tests, bugs fixes |

---

## 💡 Conseils pour les Juniors

1. **Lisez les interfaces** : Tout est documenté avec Javadoc
2. **Utilisez les mappers** : Ne faites jamais de conversion manuelle
3. **Gérez les exceptions** : Utilisez les exceptions du package `exceptions`
4. **Testez localement** : Utilisez Swagger UI pour vos tests manuels
5. **Demandez de l'aide** : N'hésitez pas à poser des questions sur Slack/Discord

---

## 📞 Contacts

- **Lead Backend** : [Votre nom] - Pour les questions d'architecture
- **DevOps** : Pour les problèmes de déploiement
- **Frontend** : Pour la coordination des APIs

---

*Document généré automatiquement - Projet Horus v1.0*
