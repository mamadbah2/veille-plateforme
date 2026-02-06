package sn.ssi.veille.models.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import sn.ssi.veille.models.entities.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

}
