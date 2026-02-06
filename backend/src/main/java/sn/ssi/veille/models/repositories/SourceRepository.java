package sn.ssi.veille.models.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import sn.ssi.veille.models.entities.Source;


@Repository
public interface SourceRepository extends MongoRepository<Source, String> {

}
