package org.liftoff.thepantry.user;

import org.liftoff.thepantry.forms.UserForm;
import org.liftoff.thepantry.models.User;

public interface UserService {

    public User save(UserForm userForm) throws EmailExistsException;
    public User findByEmail(String email);

}
