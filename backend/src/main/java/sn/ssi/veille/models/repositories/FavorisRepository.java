package sn.ssi.veille.models.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import sn.ssi.veille.models.entities.Favoris;


@Repository
public interface FavorisRepository extends MongoRepository<Favoris, String> {

}
