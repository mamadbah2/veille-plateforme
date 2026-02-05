package sn.ssi.veille.models.entities;

/**
 * Énumération des niveaux de gravité des articles.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
public enum Gravite {
    /**
     * Information générale, pas de risque immédiat.
     */
    INFORMATION(1),

    /**
     * Avis de sécurité, attention recommandée.
     */
    AVIS(2),

    /**
     * Vulnérabilité importante à surveiller.
     */
    IMPORTANT(3),

    /**
     * Menace élevée nécessitant une action rapide.
     */
    ELEVE(4),

    /**
     * Situation critique, action immédiate requise.
     */
    CRITIQUE(5);

    private final int niveau;

    Gravite(int niveau) {
        this.niveau = niveau;
    }

    public int getNiveau() {
        return niveau;
    }

    /**
     * Retourne la gravité correspondant au niveau donné.
     *
     * @param niveau le niveau de gravité (1-5)
     * @return la gravité correspondante ou INFORMATION par défaut
     */
    public static Gravite fromNiveau(int niveau) {
        for (Gravite g : values()) {
            if (g.niveau == niveau) {
                return g;
            }
        }
        return INFORMATION;
    }
}
