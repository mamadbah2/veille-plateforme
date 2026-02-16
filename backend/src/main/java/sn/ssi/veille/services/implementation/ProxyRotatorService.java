package sn.ssi.veille.services.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Service de rotation de proxies gratuits pour contourner les anti-bots.
 * Récupère une liste de proxies HTTP depuis ProxyScrape et les fait tourner.
 */
@Slf4j
@Service
public class ProxyRotatorService {

    private static final String PROXY_API = "https://api.proxyscrape.com/v4/free-proxy-list/get?request=display_proxies&proxy_format=protocolipport&format=text&timeout=5000";

    private final CopyOnWriteArrayList<ProxyInfo> proxies = new CopyOnWriteArrayList<>();
    private final Set<String> blacklisted = Collections.synchronizedSet(new HashSet<>());

    // Pool de User-Agents réalistes (2024-2025)
    private static final List<String> USER_AGENTS = List.of(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 14_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:123.0) Gecko/20100101 Firefox/123.0",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 14.3; rv:123.0) Gecko/20100101 Firefox/123.0",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36 Edg/121.0.0.0",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 14_3) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Safari/605.1.15");

    /**
     * Retourne un proxy aléatoire non-blacklisté.
     * 
     * @return ProxyInfo ou null si aucun proxy disponible
     */
    public ProxyInfo getNext() {
        List<ProxyInfo> available = proxies.stream()
                .filter(p -> !blacklisted.contains(p.key()))
                .toList();

        if (available.isEmpty()) {
            log.debug("Aucun proxy disponible (liste vide ou tous blacklistés)");
            return null;
        }

        return available.get(ThreadLocalRandom.current().nextInt(available.size()));
    }

    /**
     * Marque un proxy comme défaillant (blacklisté temporairement).
     */
    public void markBad(ProxyInfo proxy) {
        if (proxy != null) {
            blacklisted.add(proxy.key());
            log.debug("Proxy blacklisté: {}", proxy.key());
        }
    }

    /**
     * Retourne un User-Agent aléatoire du pool.
     */
    public String getRandomUserAgent() {
        return USER_AGENTS.get(ThreadLocalRandom.current().nextInt(USER_AGENTS.size()));
    }

    /**
     * Vérifie si des proxies sont disponibles.
     */
    public boolean hasProxies() {
        return proxies.stream().anyMatch(p -> !blacklisted.contains(p.key()));
    }

    /**
     * Nombre de proxies actifs (non blacklistés).
     */
    public int activeCount() {
        return (int) proxies.stream().filter(p -> !blacklisted.contains(p.key())).count();
    }

    /**
     * Rafraîchit la liste de proxies toutes les 10 minutes.
     * Aussi appelé au démarrage (initialDelay=0).
     */
    @Scheduled(fixedDelay = 600_000, initialDelay = 5000)
    public void refreshProxies() {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(java.time.Duration.ofSeconds(10))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(PROXY_API))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<ProxyInfo> newProxies = new ArrayList<>();

                for (String line : response.body().split("\n")) {
                    line = line.trim();
                    if (line.isEmpty())
                        continue;

                    // Format: "http://ip:port" ou "ip:port"
                    try {
                        String cleaned = line.replaceFirst("^https?://", "");
                        String[] parts = cleaned.split(":");
                        if (parts.length == 2) {
                            String host = parts[0].trim();
                            int port = Integer.parseInt(parts[1].trim());
                            newProxies.add(new ProxyInfo(host, port));
                        }
                    } catch (Exception e) {
                        // Skip malformed line
                    }
                }

                if (!newProxies.isEmpty()) {
                    proxies.clear();
                    proxies.addAll(newProxies);
                    blacklisted.clear(); // Reset blacklist on refresh
                    log.info("🔄 Proxies rafraîchis : {} proxies disponibles", newProxies.size());
                }
            }
        } catch (Exception e) {
            log.warn("Impossible de récupérer les proxies: {}", e.getMessage());
        }
    }

    /**
     * DTO pour un proxy (host + port).
     */
    public record ProxyInfo(String host, int port) {
        public String key() {
            return host + ":" + port;
        }

        public java.net.Proxy toJavaProxy() {
            return new java.net.Proxy(java.net.Proxy.Type.HTTP,
                    new java.net.InetSocketAddress(host, port));
        }
    }
}
