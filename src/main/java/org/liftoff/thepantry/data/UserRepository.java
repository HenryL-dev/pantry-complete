package org.liftoff.thepantry.data;

import org.liftoff.thepantry.models.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Chris Bay
 */
public interface UserRepository extends CrudRepository<User, Integer> {

    User findByUsername(String username);

}
