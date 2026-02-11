package sn.ssi.veille.utils;

import java.util.List;

public class VectorUtils {

    /**
     * Calcule la similarité cosinus entre deux vecteurs.
     * 
     * @param v1 Premier vecteur
     * @param v2 Deuxième vecteur
     * @return Score entre 0.0 (opposés/orthogonaux) et 1.0 (identiques)
     */
    public static double cosineSimilarity(List<Double> v1, List<Double> v2) {
        if (v1 == null || v2 == null || v1.isEmpty() || v2.isEmpty() || v1.size() != v2.size()) {
            return 0.0;
        }

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < v1.size(); i++) {
            dotProduct += v1.get(i) * v2.get(i);
            normA += Math.pow(v1.get(i), 2);
            normB += Math.pow(v2.get(i), 2);
        }

        if (normA == 0 || normB == 0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
