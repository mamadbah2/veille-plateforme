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

## Sources nécessitant approbation

### 6. Reddit
| Champ | Valeur |
|-------|--------|
| **URL** | https://www.reddit.com/r/netsec |
| **Méthode** | API OAuth2 ⚠️ |
| **Auth** | OAuth2 obligatoire |
| **Approbation** | Requise depuis 2023 |

**Alternatives :**
- RSS : `https://www.reddit.com/r/netsec/.rss` (limité)
- Scraping HTML (respecter ToS)

---

## Récapitulatif par méthode

| Source | API | RSS | Scraping | Auth |
|--------|-----|-----|----------|------|
| NIST NVD | ✅ | ❌ | ❌ | API Key |
| Hacker News | ✅ | ❌ | ❌ | Aucune |
| CERT-FR | ❌ | ✅ | ❌ | Aucune |
| The Hacker News | ❌ | ✅ | ❌ | Aucune |
| BleepingComputer | ❌ | ✅ | ❌ | Aucune |
| Reddit | ⚠️ | ✅ | ⚠️ | OAuth2 |

---

## Priorité d'implémentation

1. **RSS** (le plus simple) : CERT-FR, The Hacker News, BleepingComputer
2. **API gratuite** : Hacker News, NIST NVD
3. **OAuth** (complexe) : Reddit

## Dépendances Java recommandées

```xml
<!-- RSS Parser -->
<dependency>
    <groupId>com.rometools</groupId>
    <artifactId>rome</artifactId>
    <version>2.1.0</version>
</dependency>

<!-- HTTP Client -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```