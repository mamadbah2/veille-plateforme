# Architecture Technique - Service IA

## Vue d'ensemble
Le module IA de la plateforme Veille SSI est conçu pour être **agnostique du fournisseur**. Il permet d'interagir avec n'importe quelle API compatible avec le standard OpenAI (ChatGPT, LM Studio, Ollama, vLLM, DeepSeek, etc.).

## Composants Clés

### 1. `OpenAICompatibleService` (Service Principal)
- **Rôle** : Gère toutes les interactions avec le LLM.
- **Responsabilités** :
  - Vérification de la disponibilité du service (`/v1/models`).
  - Enrichissement des articles (catégorisation, tags, score de gravité).
  - Nettoyage du contenu HTML/Texte.
  - Génération de résumés.
  - Calcul d'embeddings vectoriels pour la recherche sémantique.
  - Synthèse ("Clustering") d'articles similaires.

### 2. Configuration (`application.properties`)
La configuration est centralisée et permet de changer de modèle sans toucher au code.

```properties
# standard OpenAI (ex: Local avec LM Studio)
ai.provider.type=openai
ai.provider.url=http://localhost:1234
ai.provider.model=qwen2.5-coder-3b-instruct
ai.provider.embedding-model=text-embedding-nomic-embed-text-v1.5
ai.provider.timeout=60000
```

### 3. Gestion des Prompts (`Prompts.properties`)
Les "System Prompts" sont externalisés pour faciliter leur ajustement sans recompilation.
- `ai.prompt.enrichment.system` : Consignes pour l'analyse de sécurité.
- `ai.prompt.cleaning.system` : Consignes pour le nettoyage de texte.
- `ai.prompt.summary.system` : Consignes pour la synthèse.
- `ai.prompt.clustering.system` : Consignes pour le regroupement de sujets.

### 4. Flux de Données
1. **Scraping** : Récupération du contenu brut.
2. **Nettoyage** : Appel à `cleanContent` (LLM).
3. **Enrichissement** : Appel à `enrichArticle` (LLM) -> Gravité, Tags, Catégorie.
4. **Vectorisation** : Appel à `getEmbeddings` (LLM) -> Vecteur stocké en base.
5. **Clustering** : Comparaison cosinus des vecteurs + Synthèse par LLM.

## Extensibilité
Pour ajouter un fournisseur **non-standard** (ex: API Google Gemini native) :
1. Implémenter l'interface `AIService`.
2. Utiliser `@ConditionalOnProperty` sur le type de provider.
3. Le reste de l'application (Controllers, ScrapingService) n'aura pas besoin d'être modifié.
