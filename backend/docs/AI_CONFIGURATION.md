# 🤖 Guide de Configuration IA - Veille SSI

Ce guide explique comment configurer et personnaliser le moteur d'IA de la plateforme.

## 1. Vue d'ensemble
Le backend utilise un service **`OpenAICompatibleService`** qui peut se connecter à n'importe quelle API respectant le standard OpenAI.
Cela inclut :
-   **Local** : LM Studio, Ollama, LocalAI.
-   **Cloud** : OpenAI (ChatGPT), DeepSeek API, Mistral API.

## 2. Changer de Modèle (Local avec LM Studio)

### Pré-requis
-   Avoir **LM Studio** installé et lancé.
-   Avoir démarré le serveur local (Start Server) sur le port `1234` (par défaut).
-   Avoir chargé **deux modèles** (si ta machine le permet) ou un seul polyvalent.

### Configuration (`backend/src/main/resources/application.properties`)

```properties
# Type de provider (toujours 'openai' pour LM Studio/Ollama)
ai.provider.type=openai

# URL du serveur local
ai.provider.url=http://localhost:1234

# Clé API (généralement inutile en local, mettre n'importe quoi)
ai.provider.api-key=not-needed

# Modèle pour le CHAT (Analyse, Résumé, Nettoyage)
# Copier l'ID exact depuis LM Studio (ex: qwen2.5-coder-3b-instruct)
ai.provider.model=qwen2.5-coder-3b-instruct

# Modèle pour les EMBEDDINGS (Recherche vectorielle)
# Copier l'ID exact (ex: text-embedding-nomic-embed-text-v1.5)
ai.provider.embedding-model=text-embedding-nomic-embed-text-v1.5

# Timeout (en ms) - Augmenter si le modèle est lent
ai.provider.timeout=60000
```

---

## 3. Utiliser OpenAI (ChatGPT)

Si tu veux passer sur la puissance du Cloud OpenAI :

```properties
ai.provider.type=openai
ai.provider.url=https://api.openai.com
ai.provider.api-key=sk-proj-TON-API-KEY-ICI
ai.provider.model=gpt-4o
ai.provider.embedding-model=text-embedding-3-small
```

---

## 4. Personnaliser le Comportement (Prompts)

Les instructions données à l'IA sont modifiables dans **`backend/src/main/resources/prompts.properties`**.
Tu peux ajuster ces prompts sans recompiler le code Java.

### Exemple : Rendre l'IA plus paranoïaque sur la sécurité
Fichier : `prompts.properties`

```properties
# Prompt d'enrichissement
ai.prompt.enrichment.system=Agis comme un expert paranoïaque en cybersécurité (SOC Analyst). \
Tu dois analyser cet article et identifier TOUTE menace potentielle, même faible. \
Si tu as un doute, classe en 'WARNING'.
```

### Les Prompts Disponibles
1.  **Enrichment** : Pour classifier et taguer l'article.
2.  **Cleaning** : Pour nettoyer le HTML et reformuler le texte.
3.  **Summary** : Pour générer le résumé.
4.  **Clustering** : Pour synthétiser un groupe d'articles similaires.
