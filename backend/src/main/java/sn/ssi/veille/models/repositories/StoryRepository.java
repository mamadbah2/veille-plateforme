package sn.ssi.veille.models.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import sn.ssi.veille.models.entities.Story;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StoryRepository extends MongoRepository<Story, String> {

    // Trouver les stories mises à jour récemment
    List<Story> findByDateMiseAJourAfterOrderByDateMiseAJourDesc(LocalDateTime date);

    // Trouver les stories publiées
    List<Story> findByEtat(Story.EtatStory etat);

    // Compter les stories par catégorie (pour l'Explorer)
    long countByCategoriesContaining(String categoryName);

    // Filtrer les stories par catégorie
    List<Story> findByCategoriesContaining(String categoryName);
}
