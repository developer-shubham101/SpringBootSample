package in.newdevpoint.bootcamp.repository;

import in.newdevpoint.bootcamp.entity.ERole;
import in.newdevpoint.bootcamp.entity.Role;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
