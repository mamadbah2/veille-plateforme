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
}
