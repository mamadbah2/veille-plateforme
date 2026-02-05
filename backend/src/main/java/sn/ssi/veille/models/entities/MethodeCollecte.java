package sn.ssi.veille.models.entities;

/**
 * Énumération des méthodes de collecte de données.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
public enum MethodeCollecte {
    /**
     * Collecte via API REST.
     */
    API,

    /**
     * Collecte via web scraping (HTML parsing).
     */
    SCRAPING,

    /**
     * Collecte via flux RSS/Atom.
     */
    RSS,

    /**
     * Collecte via Playwright (JavaScript rendering).
     */
    PLAYWRIGHT
}
