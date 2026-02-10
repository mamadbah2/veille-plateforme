package sn.ssi.veille.models.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import sn.ssi.veille.models.entities.Source;

import java.util.List;
import java.util.Optional;

@Repository
public interface SourceRepository extends MongoRepository<Source, String> {

    Optional<Source> findByUrl(String url);

    List<Source> findByActiveTrue();

    boolean existsByNomSource(String nomSource);

    Optional<Source> findByNomSource(String nomSource);
}
