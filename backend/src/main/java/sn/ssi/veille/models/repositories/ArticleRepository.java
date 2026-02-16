package sn.ssi.veille.models.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import sn.ssi.veille.models.entities.Article;
import sn.ssi.veille.models.entities.Gravite;

@Repository
public interface ArticleRepository extends MongoRepository<Article, String> {

        Page<Article> findByCategorieId(String categorieId, Pageable pageable);

        Page<Article> findBySourceId(String sourceId, Pageable pageable);

        Page<Article> findByGravite(Gravite gravite, Pageable pageable);

        boolean existsByUrlOrigineAndSourceId(String urlOrigine, String sourceId);

        boolean existsByUrlOrigine(String urlOrigine);

        java.util.List<Article> findByDatePublicationAfter(java.time.LocalDateTime date);

        // Recherche simple
        Page<Article> findByTitreContainingIgnoreCaseOrContenuContainingIgnoreCase(String titre, String contenu,
                        Pageable pageable);

        // Recherche "Smart" : Titre OU Contenu OU Catégorie
        Page<Article> findByTitreContainingIgnoreCaseOrContenuContainingIgnoreCaseOrCategorieIdIn(
                        String titre, String contenu, java.util.List<String> categorieIds, Pageable pageable);

        // Recherche Combinée : Mot-clé + Catégorie
        @org.springframework.data.mongodb.repository.Query("{ '$and': [ { '$or': [ { 'titre': { '$regex': ?0, '$options': 'i' } }, { 'contenu': { '$regex': ?0, '$options': 'i' } } ] }, { 'categorieId': ?1 } ] }")
        Page<Article> searchByKeywordAndCategory(String keyword, String categorieId, Pageable pageable);

        // Recherche AGGREGATION pour les Tags Tendances (Plus simple que CustomRepo)
        @org.springframework.data.mongodb.repository.Aggregation(pipeline = {
                        "{ '$match': { 'datePublication': { '$gte': ?0 } } }",
                        "{ '$unwind': '$tags' }",
                        "{ '$group': { '_id': '$tags', 'count': { '$sum': 1 } } }",
                        "{ '$sort': { 'count': -1 } }",
                        "{ '$limit': ?1 }"
        })
        java.util.List<TagCountResult> findTrendingTags(java.time.LocalDateTime since, int limit);

        // Recherche de Tags par Regex (pour l'autocomplétion)
        @org.springframework.data.mongodb.repository.Aggregation(pipeline = {
                        "{ '$unwind': '$tags' }",
                        "{ '$match': { 'tags': { '$regex': ?0, '$options': 'i' } } }",
                        "{ '$group': { '_id': '$tags', 'count': { '$sum': 1 } } }",
                        "{ '$sort': { 'count': -1 } }",
                        "{ '$limit': ?1 }"
        })
        java.util.List<TagCountResult> findMatchingTags(String regex, int limit);

        // Record interne pour le mapping du résulat
        record TagCountResult(@org.springframework.data.annotation.Id String tag, long count) {
        }
}
