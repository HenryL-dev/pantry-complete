package org.liftoff.thepantry.models;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotNull;

@javax.persistence.Entity
public class UserEntity extends AbstractEntity {

    @NotNull
    private String username;

    @NotNull
    private String pwHash;



    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserEntity() {}

    public UserEntity(String username, String password) {
        this.username = username;
        this.pwHash = encoder.encode(password);
    }

    public String getUsername() {
        return username;
    }

    public boolean isMatchingPassword(String password) {
        return encoder.matches(password, pwHash);
    }




}
