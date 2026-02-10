package sn.ssi.veille.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sn.ssi.veille.models.entities.MethodeCollecte;
import sn.ssi.veille.models.entities.Source;
import sn.ssi.veille.models.entities.Source.SourceType;
import sn.ssi.veille.models.repositories.SourceRepository;

import java.util.List;

/**
 * Initialisation des sources par défaut au démarrage si la base est vide.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SourceInitializer {

        private final SourceRepository sourceRepository;
        private final sn.ssi.veille.models.repositories.CategorieRepository categorieRepository;

        @Bean
        public CommandLineRunner initSources() {
                return args -> {
                        log.info("Initialisation des catégories et sources...");

                        // 1. Initialiser les 5 Catégories Globales et récupérer leurs IDs
                        java.util.Map<String, sn.ssi.veille.models.entities.Categorie> categories = new java.util.HashMap<>();

                        List<sn.ssi.veille.models.entities.Categorie> catDefs = List.of(
                                        sn.ssi.veille.models.entities.Categorie.builder()
                                                        .nomCategorie("PROGRAMMING")
                                                        .description("Développement logiciel, langages, architecture")
                                                        .couleur("#3B82F6") // Blue
                                                        .icone("code")
                                                        .build(),
                                        sn.ssi.veille.models.entities.Categorie.builder()
                                                        .nomCategorie("DATA_SCIENCE")
                                                        .description("IA, Machine Learning, Big Data")
                                                        .couleur("#10B981") // Green
                                                        .icone("brain")
                                                        .build(),
                                        sn.ssi.veille.models.entities.Categorie.builder()
                                                        .nomCategorie("CYBERSECURITY")
                                                        .description("Sécurité offensive/défensive, vulnérabilités")
                                                        .couleur("#EF4444") // Red
                                                        .icone("shield-check")
                                                        .build(),
                                        sn.ssi.veille.models.entities.Categorie.builder()
                                                        .nomCategorie("DEVOPS")
                                                        .description("Cloud, CI/CD, Infrastructure")
                                                        .couleur("#F59E0B") // Amber
                                                        .icone("server")
                                                        .build(),
                                        sn.ssi.veille.models.entities.Categorie.builder()
                                                        .nomCategorie("TECHNOLOGY")
                                                        .description("Tech news, business, startups")
                                                        .couleur("#6B7280") // Gray
                                                        .icone("chip")
                                                        .build());

                        for (sn.ssi.veille.models.entities.Categorie catDef : catDefs) {
                                sn.ssi.veille.models.entities.Categorie cat = categorieRepository
                                                .findByNomCategorie(catDef.getNomCategorie())
                                                .orElseGet(() -> categorieRepository.save(catDef));
                                categories.put(cat.getNomCategorie(), cat);
                        }
                        log.info("Catégories vérifiées : {} chargées.", categories.size());

                        // 2. Initialiser les Sources avec les "Vrais" IDs de catégories
                        List<Source> defaultSources = List.of(
                                        // === PROGRAMMING ===
                                        Source.builder().nomSource("Dev.to").url("https://dev.to/feed")
                                                        .methodeCollecte(MethodeCollecte.RSS)
                                                        .sourceType(SourceType.COMMUNITY)
                                                        .categorieParDefaut(categories.get("PROGRAMMING").getId())
                                                        .description("Communauté de développeurs")
                                                        .trustScore(8).active(true).priorite(8).build(),
                                        Source.builder().nomSource("GitHub Blog").url("https://github.blog/feed/")
                                                        .methodeCollecte(MethodeCollecte.RSS)
                                                        .sourceType(SourceType.OFFICIAL)
                                                        .categorieParDefaut(categories.get("PROGRAMMING").getId())
                                                        .description("Actualités et engineering GitHub")
                                                        .trustScore(9).active(true).priorite(8).build(),
                                        Source.builder().nomSource("InfoQ").url("https://www.infoq.com/feed/")
                                                        .methodeCollecte(MethodeCollecte.RSS)
                                                        .sourceType(SourceType.MEDIA)
                                                        .categorieParDefaut(categories.get("PROGRAMMING").getId())
                                                        .description("Architecture logicielle et développement")
                                                        .trustScore(9).active(true).priorite(9).build(),

                                        // === DEVOPS & CLOUD ===
                                        Source.builder().nomSource("AWS News Blog")
                                                        .url("https://aws.amazon.com/blogs/aws/feed/")
                                                        .methodeCollecte(MethodeCollecte.RSS)
                                                        .sourceType(SourceType.OFFICIAL)
                                                        .categorieParDefaut(categories.get("DEVOPS").getId())
                                                        .description("Nouveautés AWS officielles")
                                                        .trustScore(10).active(true).priorite(9).build(),
                                        Source.builder().nomSource("Microsoft Azure Blog")
                                                        .url("https://azure.microsoft.com/en-us/blog/feed/")
                                                        .methodeCollecte(MethodeCollecte.RSS)
                                                        .sourceType(SourceType.OFFICIAL)
                                                        .categorieParDefaut(categories.get("DEVOPS").getId())
                                                        .description("Nouveautés Microsoft Azure")
                                                        .trustScore(10).active(true).priorite(9).build(),
                                        Source.builder().nomSource("Google Cloud Blog")
                                                        .url("https://cloud.google.com/blog/rss")
                                                        .methodeCollecte(MethodeCollecte.RSS)
                                                        .sourceType(SourceType.OFFICIAL)
                                                        .categorieParDefaut(categories.get("DEVOPS").getId())
                                                        .description("Actualités Google Cloud Platform")
                                                        .trustScore(10).active(true).priorite(10).build(),
                                        Source.builder().nomSource("Kubernetes Blog")
                                                        .url("https://kubernetes.io/feed.xml")
                                                        .methodeCollecte(MethodeCollecte.RSS)
                                                        .sourceType(SourceType.OFFICIAL)
                                                        .categorieParDefaut(categories.get("DEVOPS").getId())
                                                        .description("Actualités Kubernetes")
                                                        .trustScore(10).active(true).priorite(9).build(),

                                        // === DATA SCIENCE & AI ===
                                        Source.builder().nomSource("OpenAI Blog").url("https://openai.com/news/rss.xml")
                                                        .methodeCollecte(MethodeCollecte.RSS)
                                                        .sourceType(SourceType.OFFICIAL)
                                                        .categorieParDefaut(categories.get("DATA_SCIENCE").getId())
                                                        .description("Actualités OpenAI")
                                                        .trustScore(10).active(true).priorite(10).build(),
                                        Source.builder().nomSource("Google DeepMind")
                                                        .url("https://deepmind.google/discover/blog/rss.xml")
                                                        .methodeCollecte(MethodeCollecte.RSS)
                                                        .sourceType(SourceType.OFFICIAL)
                                                        .categorieParDefaut(categories.get("DATA_SCIENCE").getId())
                                                        .description("Recherche IA par Google DeepMind")
                                                        .trustScore(10).active(true).priorite(10).build(),
                                        Source.builder().nomSource("Anthropic Research")
                                                        .url("https://www.anthropic.com/rss")
                                                        .methodeCollecte(MethodeCollecte.RSS)
                                                        .sourceType(SourceType.OFFICIAL)
                                                        .categorieParDefaut(categories.get("DATA_SCIENCE").getId())
                                                        .description("Recherche et Sécurité IA par Anthropic")
                                                        .trustScore(10).active(true).priorite(10).build(),
                                        Source.builder().nomSource("Hugging Face Blog")
                                                        .url("https://huggingface.co/blog/feed.xml")
                                                        .methodeCollecte(MethodeCollecte.RSS)
                                                        .sourceType(SourceType.COMMUNITY)
                                                        .categorieParDefaut(categories.get("DATA_SCIENCE").getId())
                                                        .description("Blog IA et Open Source")
                                                        .trustScore(9).active(true).priorite(9).build(),

                                        // === CYBERSECURITY ===
                                        Source.builder().nomSource("Hacker News")
                                                        .url("https://hacker-news.firebaseio.com/v0/")
                                                        .methodeCollecte(MethodeCollecte.API)
                                                        .sourceType(SourceType.COMMUNITY)
                                                        .categorieParDefaut(categories.get("CYBERSECURITY").getId())
                                                        .description("Actualités tech et startup via YCombinator")
                                                        .trustScore(8).active(true).priorite(6).build(),
                                        Source.builder().nomSource("NIST NVD")
                                                        .url("https://services.nvd.nist.gov/rest/json/cves/2.0")
                                                        .methodeCollecte(MethodeCollecte.API)
                                                        .sourceType(SourceType.OFFICIAL)
                                                        .categorieParDefaut(categories.get("CYBERSECURITY").getId())
                                                        .description("National Vulnerability Database (CVEs)")
                                                        .trustScore(10).active(true).priorite(10).build(),
                                        Source.builder().nomSource("CERT-FR Alertes")
                                                        .url("https://www.cert.ssi.gouv.fr/alerte/feed/")
                                                        .methodeCollecte(MethodeCollecte.RSS)
                                                        .sourceType(SourceType.OFFICIAL)
                                                        .categorieParDefaut(categories.get("CYBERSECURITY").getId())
                                                        .description("Alertes de sécurité officielles du CERT-FR")
                                                        .trustScore(10).active(true).priorite(10).build(),
                                        Source.builder().nomSource("BleepingComputer")
                                                        .url("https://www.bleepingcomputer.com/feed/")
                                                        .methodeCollecte(MethodeCollecte.RSS)
                                                        .sourceType(SourceType.MEDIA)
                                                        .categorieParDefaut(categories.get("CYBERSECURITY").getId())
                                                        .description("Actualités cybersécurité et tech")
                                                        .trustScore(8).active(true).priorite(7).build(),
                                        Source.builder().nomSource("The Hacker News")
                                                        .url("https://thehackernews.com/feeds/posts/default")
                                                        .methodeCollecte(MethodeCollecte.RSS)
                                                        .sourceType(SourceType.MEDIA)
                                                        .categorieParDefaut(categories.get("CYBERSECURITY").getId())
                                                        .description("News cybersécurité")
                                                        .trustScore(7).active(true).priorite(7).build(),
                                        Source.builder().nomSource("Reddit Netsec")
                                                        .url("https://www.reddit.com/r/netsec/.rss")
                                                        .methodeCollecte(MethodeCollecte.RSS)
                                                        .sourceType(SourceType.COMMUNITY)
                                                        .categorieParDefaut(categories.get("CYBERSECURITY").getId())
                                                        .description("Communauté Reddit Network Security")
                                                        .trustScore(6).active(true).priorite(5).build(),
                                        Source.builder().nomSource("CISA Alerts")
                                                        .url("https://www.cisa.gov/cybersecurity-advisories/all.xml")
                                                        .methodeCollecte(MethodeCollecte.RSS)
                                                        .sourceType(SourceType.OFFICIAL)
                                                        .categorieParDefaut(categories.get("CYBERSECURITY").getId())
                                                        .description("Cybersecurity and Infrastructure Security Agency")
                                                        .trustScore(10).active(true).priorite(9).build(),
                                        Source.builder().nomSource("Krebs on Security")
                                                        .url("https://krebsonsecurity.com/feed/")
                                                        .methodeCollecte(MethodeCollecte.RSS)
                                                        .sourceType(SourceType.BLOG)
                                                        .categorieParDefaut(categories.get("CYBERSECURITY").getId())
                                                        .description("Journalisme d'investigation cybersécurité")
                                                        .trustScore(9).active(true).priorite(8).build(),
                                        Source.builder().nomSource("Schneier on Security")
                                                        .url("https://www.schneier.com/feed/atom/")
                                                        .methodeCollecte(MethodeCollecte.RSS)
                                                        .sourceType(SourceType.BLOG)
                                                        .categorieParDefaut(categories.get("CYBERSECURITY").getId())
                                                        .description("Blog de Bruce Schneier")
                                                        .trustScore(9).active(true).priorite(7).build(),
                                        Source.builder().nomSource("Zataz").url("https://www.zataz.com/")
                                                        .methodeCollecte(MethodeCollecte.SCRAPING)
                                                        .sourceType(SourceType.BLOG)
                                                        .categorieParDefaut(categories.get("CYBERSECURITY").getId())
                                                        .description("Blog Zataz (Scraping HTML)")
                                                        .selectorTitle("h2.post-title a").trustScore(6).active(true)
                                                        .priorite(5).build(),

                                        // === TECHNOLOGY & BUSINESS ===
                                        Source.builder().nomSource("The Verge")
                                                        .url("https://www.theverge.com/rss/index.xml")
                                                        .methodeCollecte(MethodeCollecte.RSS)
                                                        .sourceType(SourceType.MEDIA)
                                                        .categorieParDefaut(categories.get("TECHNOLOGY").getId())
                                                        .description("Actualités technologiques et culture")
                                                        .trustScore(8).active(true).priorite(7).build(),
                                        Source.builder().nomSource("Ars Technica")
                                                        .url("https://feeds.arstechnica.com/arstechnica/index")
                                                        .methodeCollecte(MethodeCollecte.RSS)
                                                        .sourceType(SourceType.MEDIA)
                                                        .categorieParDefaut(categories.get("TECHNOLOGY").getId())
                                                        .description("Analyses technologiques approfondies")
                                                        .trustScore(9).active(true).priorite(7).build(),
                                        Source.builder().nomSource("Wired Security")
                                                        .url("https://www.wired.com/feed/category/security/latest/rss")
                                                        .methodeCollecte(MethodeCollecte.RSS)
                                                        .sourceType(SourceType.MEDIA)
                                                        .categorieParDefaut(categories.get("TECHNOLOGY").getId())
                                                        .description("Articles sécurité Wired")
                                                        .trustScore(8).active(true).priorite(6).build(),
                                        Source.builder().nomSource("TechCrunch").url("https://techcrunch.com/feed/")
                                                        .methodeCollecte(MethodeCollecte.RSS)
                                                        .sourceType(SourceType.MEDIA)
                                                        .categorieParDefaut(categories.get("TECHNOLOGY").getId())
                                                        .description("Actualités technologiques")
                                                        .trustScore(7).active(true).priorite(5).build());

                        int added = 0;
                        int updated = 0;
                        for (Source sourceDef : defaultSources) {
                                java.util.Optional<Source> existingOpt = sourceRepository
                                                .findByNomSource(sourceDef.getNomSource());

                                if (existingOpt.isPresent()) {
                                        // Update existing source
                                        Source existing = existingOpt.get();
                                        if (!existing.getCategorieParDefaut().equals(sourceDef.getCategorieParDefaut())
                                                        ||
                                                        !existing.getDescription().equals(sourceDef.getDescription()) ||
                                                        existing.getTrustScore() != sourceDef.getTrustScore()) {

                                                existing.setCategorieParDefaut(sourceDef.getCategorieParDefaut());
                                                existing.setDescription(sourceDef.getDescription());
                                                existing.setTrustScore(sourceDef.getTrustScore());
                                                // Keep other fields like ID, active status preference etc.

                                                sourceRepository.save(existing);
                                                updated++;
                                        }
                                } else {
                                        // Create new source
                                        sourceRepository.save(sourceDef);
                                        added++;
                                }
                        }

                        if (added > 0 || updated > 0) {
                                log.info("Sources Mises à jour : {} ajoutées, {} modifiées (Total: {}).",
                                                added, updated, sourceRepository.count());
                        } else {
                                log.info("Toutes les sources sont à jour.");
                        }
                };

        }
}
