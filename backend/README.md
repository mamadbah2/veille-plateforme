# Cahier des Charges Global : Horus (Veille SSI)

## Objectives

L'objectif de ce projet est de concevoir une plateforme de veille automatisée dédiée à la cybersécurité. Le système doit agréger, filtrer et notifier les utilisateurs (étudiants en SSI) des dernières actualités pertinentes provenant de sources critiques (Reddit, YCombinator, NIST, etc.).

Le backend repose sur une architecture robuste en **Java 21 / Spring Boot 4**, utilisant le scraping de données et une infrastructure automatisée pour garantir une veille en temps réel.

---

## Instructions

### 1. Configuration de l'Environnement & Infrastructure

* **Architecture Monolithique SOLID :** DDéveloppement d'une API unique structurée en couches (Web, Service, Data, Config).Application stricte du principe de responsabilité unique (SRP) et d'inversion de dépendance (DIP) via les interfaces.

* **Infrastructure as Code (IaC) :**
* **Docker :** Conteneurisation des services et déploiement automatisé sur un VPS.


* **Pipeline CI/CD :**
* **Jenkins :** Automatisation des builds Maven et des tests.
* **SonarQube :** Analyse statique obligatoire du code pour valider la qualité avant déploiement.


* **Logging (Optionnel) :** Centralisation des logs pour surveiller le bon fonctionnement des agents de scraping.

### 2. Stratégie de Données

* **MongoDB :** Base principale pour les entités `User`, `Article`, `Source`, `Categorie`, `Notification` et `Favoris`.
* **Cohérence :** Gestion de l'idempotence pour éviter les doublons lors de la collecte d'articles sur une même source.

### 3. Développement et Design par Rôles

#### **A. Administrateur (Le Superviseur)**

* **Monitoring (Optionnel) :** Dashboard sur l'état de santé des scrapers et le volume d'articles collectés.

#### **B. Étudiant / Utilisateur (Le Veilleur)**

* **Consultation :** Lecture des actualités filtrées par date, catégorie ou gravité.
* **Recherche & Filtrage :** Utilisation des filtres avancés pour isoler les news pertinentes.
* **Favoris :** Possibilité de mettre de côté des articles pour une lecture ultérieure.
* **Notifications :** Réception d'alertes en fonction de l'état (non lu/lu) et de la pertinence.
* **Résumé Hebdomadaire :** Accès à une synthèse des news les plus importantes de la semaine - Implementer a travers une ia dont le user fournira la cle api (ou en local).

### 4. Sécurité et Conformité (Contexte M1 SSI)

* **Authentification :** Mise en place de `SecurityConfig.java` utilisant JWT pour sécuriser les échanges entre le front et le back.
* **Secrets Management :** Utilisation de variables d'environnement ou Vault pour les clés d'API de sources tierces.

### 5. Architecture dossier (exple)
```
.
├── exceptions
├── config
│   └── SecurityConfig.java
├── data
│   ├── entities
│   └── repositories
|   └── records
├── services
│   ├── implementation
│   ├── SocialService.java
│   ├── UserService.java
│   └── WatchlistService.java (interface)
├── UserServiceApplication.java
└── web
    ├── controllers
    │   ├── implementation
    │   ├── SocialController.java
    ├── dto
    │   ├── requests
    │   └── responses
    └── mappers
```

---

## Bonus

* **Scraping Avancé :** Support du rendu JavaScript via Playwright pour les sites protégés.
* **IA Summarizer :** Utilisation d'un LLM pour générer automatiquement les résumés des articles longs.
* **Analyse de Gravité :**

---

