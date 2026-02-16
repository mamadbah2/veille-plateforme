# Sources de Veille - Documentation API

## Sources avec API gratuite

### 1. NIST NVD (CVE Database)
| Champ | Valeur |
|-------|--------|
| **URL** | https://nvd.nist.gov |
| **Méthode** | API REST |
| **Auth** | API Key (gratuit) |
| **Endpoint** | `https://services.nvd.nist.gov/rest/json/cves/2.0` |
| **Obtenir clé** | https://nvd.nist.gov/developers/request-an-api-key |
| **Rate limit** | 10 req/min sans clé, 50 req/min avec clé |

```bash
# Exemple de requête
curl "https://services.nvd.nist.gov/rest/json/cves/2.0?resultsPerPage=10" \
  -H "apiKey: VOTRE_CLE"
```

---

### 2. Hacker News (YCombinator)
| Champ | Valeur |
|-------|--------|
| **URL** | https://news.ycombinator.com |
| **Méthode** | API Firebase |
| **Auth** | ❌ Aucune |
| **Rate limit** | ❌ Aucune |

**Endpoints :**
```
https://hacker-news.firebaseio.com/v0/topstories.json     # Top 500 IDs
https://hacker-news.firebaseio.com/v0/newstories.json     # Nouveaux posts
https://hacker-news.firebaseio.com/v0/beststories.json    # Meilleurs posts
https://hacker-news.firebaseio.com/v0/item/<id>.json      # Détail article
```

---

### 3. CERT-FR (Gouvernemental FR)
| Champ | Valeur |
|-------|--------|
| **URL** | https://www.cert.ssi.gouv.fr |
| **Méthode** | RSS |
| **Auth** | ❌ Aucune |

**Flux RSS :**
```
https://www.cert.ssi.gouv.fr/feed/              # Toutes publications
https://www.cert.ssi.gouv.fr/alerte/feed/       # Alertes sécurité
https://www.cert.ssi.gouv.fr/avis/feed/         # Avis
https://www.cert.ssi.gouv.fr/cti/feed/          # Indicateurs de compromission
```

---

### 4. The Hacker News (Cybersecurity)
| Champ | Valeur |
|-------|--------|
| **URL** | https://thehackernews.com |
| **Méthode** | RSS |
| **Auth** | ❌ Aucune |

**Flux RSS :**
```
https://thehackernews.com/feeds/posts/default
```

---

### 5. BleepingComputer
| Champ | Valeur |
|-------|--------|
| **URL** | https://www.bleepingcomputer.com |
| **Méthode** | RSS |
| **Auth** | ❌ Aucune |

**Flux RSS :**
```
https://www.bleepingcomputer.com/feed/                    # Général
https://www.bleepingcomputer.com/news/security/feed/      # Sécurité
https://www.bleepingcomputer.com/news/technology/feed/    # Tech
```

---

## Sources nécessitant approbation (OAuth)

### 6. Reddit (/r/netsec)
**Option A : API Officielle** (Compliqué)
1. Aller sur https://www.reddit.com/prefs/apps
2. Créer une app (script)
3. Obtenir `client_id` et `client_secret`
4. Authentification OAuth2 obligatoire

**Option B : RSS (Limité mais simple)**
- URL : `https://www.reddit.com/r/netsec/.rss`
- Inconvénient : Rate limit agressif, contenu tronqué

---

## Comment obtenir les clés ?

### 🔑 NIST API Key
1. Remplir le formulaire : https://nvd.nist.gov/developers/request-an-api-key
2. Tu reçois la clé par email instantanément.
3. On l'ajoute dans la config Source : `headers: {"apiKey": "TA_CLE"}`

### 🔑 Reddit OAuth
1. Créer compte Reddit
2. Créer app sur https://www.reddit.com/prefs/apps
3. Configurer `clientId`/`clientSecret` dans le backend.

---

## Récapitulatif par méthode

| Source | API | RSS | Scraping | Auth | Statut |
|--------|-----|-----|----------|------|--------|
| NIST NVD | ✅ | ❌ | ❌ | API Key | ❌ |
| Hacker News | ✅ | ❌ | ❌ | Aucune | ✅ |
| CERT-FR | ❌ | ✅ | ❌ | Aucune | ✅ |
| The Hacker News | ❌ | ✅ | ❌ | Aucune | ✅ |
| BleepingComputer | ❌ | ✅ | ❌ | Aucune | ✅ |
| Reddit | ⚠️ | ✅ | ⚠️ | OAuth2 | ❌ |