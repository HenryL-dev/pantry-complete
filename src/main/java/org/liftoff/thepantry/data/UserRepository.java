package org.liftoff.thepantry.data;

import org.liftoff.thepantry.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Chris Bay
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByEmail(String email);
}