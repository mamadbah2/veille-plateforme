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

        @Bean
        public CommandLineRunner initSources() {
                return args -> {
                        if (sourceRepository.count() == 0) {
                                log.info("Base de données sources vide. Initialisation des sources par défaut...");

                                List<Source> defaultSources = List.of(
                                                // 1. Hacker News (API)
                                                Source.builder()
                                                                .nomSource("Hacker News")
                                                                .url("https://hacker-news.firebaseio.com/v0/")
                                                                .methodeCollecte(MethodeCollecte.API)
                                                                .sourceType(SourceType.COMMUNITY)
                                                                .categorieParDefaut("General")
                                                                .description("Actualités tech et startup via YCombinator")
                                                                .trustScore(8)
                                                                .active(true)
                                                                .priorite(6)
                                                                .build(),

                                                // 2. NIST NVD (API)
                                                Source.builder()
                                                                .nomSource("NIST NVD")
                                                                .url("https://services.nvd.nist.gov/rest/json/cves/2.0")
                                                                .methodeCollecte(MethodeCollecte.API)
                                                                .sourceType(SourceType.OFFICIAL)
                                                                .categorieParDefaut("Vulnerabilite")
                                                                .description("National Vulnerability Database (CVEs)")
                                                                .trustScore(10)
                                                                .active(true)
                                                                .priorite(10)
                                                                .build(),

                                                // 3. CERT-FR (RSS)
                                                Source.builder()
                                                                .nomSource("CERT-FR Alertes")
                                                                .url("https://www.cert.ssi.gouv.fr/alerte/feed/")
                                                                .methodeCollecte(MethodeCollecte.RSS)
                                                                .sourceType(SourceType.OFFICIAL)
                                                                .categorieParDefaut("Securite")
                                                                .description("Alertes de sécurité officielles du CERT-FR")
                                                                .trustScore(10)
                                                                .active(true)
                                                                .priorite(10)
                                                                .build(),

                                                // 4. BleepingComputer (RSS)
                                                Source.builder()
                                                                .nomSource("BleepingComputer")
                                                                .url("https://www.bleepingcomputer.com/feed/")
                                                                .methodeCollecte(MethodeCollecte.RSS)
                                                                .sourceType(SourceType.MEDIA)
                                                                .categorieParDefaut("Cybersecurite")
                                                                .description("Actualités cybersécurité et tech")
                                                                .trustScore(8)
                                                                .active(true)
                                                                .priorite(7)
                                                                .build(),

                                                // 5. The Hacker News (RSS)
                                                Source.builder()
                                                                .nomSource("The Hacker News")
                                                                .url("https://thehackernews.com/feeds/posts/default")
                                                                .methodeCollecte(MethodeCollecte.RSS)
                                                                .sourceType(SourceType.MEDIA)
                                                                .categorieParDefaut("Cybersecurite")
                                                                .description("News cybersécurité")
                                                                .trustScore(7)
                                                                .active(true)
                                                                .priorite(7)
                                                                .build(),

                                                // 6. Reddit Netsec (RSS)
                                                Source.builder()
                                                                .nomSource("Reddit Netsec")
                                                                .url("https://www.reddit.com/r/netsec/.rss")
                                                                .methodeCollecte(MethodeCollecte.RSS)
                                                                .sourceType(SourceType.COMMUNITY)
                                                                .categorieParDefaut("Cybersecurite")
                                                                .description("Communauté Reddit Network Security")
                                                                .trustScore(6)
                                                                .active(true)
                                                                .priorite(5)
                                                                .build(),

                                                // 7. CISA Alerts (RSS)
                                                Source.builder()
                                                                .nomSource("CISA Alerts")
                                                                .url("https://www.cisa.gov/cybersecurity-advisories/all.xml")
                                                                .methodeCollecte(MethodeCollecte.RSS)
                                                                .sourceType(SourceType.OFFICIAL)
                                                                .categorieParDefaut("Gouvernement")
                                                                .description("Cybersecurity and Infrastructure Security Agency")
                                                                .trustScore(10)
                                                                .active(true)
                                                                .priorite(9)
                                                                .build(),

                                                // 8. Krebs on Security (RSS)
                                                Source.builder()
                                                                .nomSource("Krebs on Security")
                                                                .url("https://krebsonsecurity.com/feed/")
                                                                .methodeCollecte(MethodeCollecte.RSS)
                                                                .sourceType(SourceType.BLOG)
                                                                .categorieParDefaut("Investigation")
                                                                .description("Journalisme d'investigation cybersécurité")
                                                                .trustScore(9)
                                                                .active(true)
                                                                .priorite(8)
                                                                .build(),

                                                // 9. Schneier on Security (RSS)
                                                Source.builder()
                                                                .nomSource("Schneier on Security")
                                                                .url("https://www.schneier.com/feed/atom/")
                                                                .methodeCollecte(MethodeCollecte.RSS)
                                                                .sourceType(SourceType.BLOG)
                                                                .categorieParDefaut("Cryptographie")
                                                                .description("Blog de Bruce Schneier")
                                                                .trustScore(9)
                                                                .active(true)
                                                                .priorite(7)
                                                                .build(),

                                                // 10. Wired Security (RSS)
                                                Source.builder()
                                                                .nomSource("Wired Security")
                                                                .url("https://www.wired.com/feed/category/security/latest/rss")
                                                                .methodeCollecte(MethodeCollecte.RSS)
                                                                .sourceType(SourceType.MEDIA)
                                                                .categorieParDefaut("Media")
                                                                .description("Articles sécurité Wired")
                                                                .trustScore(8)
                                                                .active(true)
                                                                .priorite(6)
                                                                .build(),

                                                // 11. TechCrunch (RSS)
                                                Source.builder()
                                                                .nomSource("TechCrunch")
                                                                .url("https://techcrunch.com/feed/")
                                                                .methodeCollecte(MethodeCollecte.RSS)
                                                                .sourceType(SourceType.MEDIA)
                                                                .categorieParDefaut("Startup")
                                                                .description("Actualités technologiques")
                                                                .trustScore(7)
                                                                .active(true)
                                                                .priorite(5)
                                                                .build(),

                                                // 12. Zataz (HTML Scraping via Playwright)
                                                Source.builder()
                                                                .nomSource("Zataz")
                                                                .url("https://www.zataz.com/")
                                                                .methodeCollecte(MethodeCollecte.SCRAPING) // Declenche
                                                                                                           // Playwright
                                                                .sourceType(SourceType.BLOG)
                                                                .categorieParDefaut("Investigation")
                                                                .description("Blog Zataz (Scraping HTML)")
                                                                .selectorTitle("h2.post-title a") // Selecteur CSS
                                                                .trustScore(6)
                                                                .active(true)
                                                                .priorite(5)
                                                                .build());

                                sourceRepository.saveAll(defaultSources);
                                log.info("{} sources par défaut initialisées.", defaultSources.size());
                        } else {
                                log.info("Sources déjà présentes en base. Pas d'initialisation requise.");
                        }
                };
        }
}
