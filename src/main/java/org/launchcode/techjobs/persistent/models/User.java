package org.launchcode.techjobs.persistent.models;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class User extends AbstractEntity {

    @NotNull
    private String pwHash;

    private boolean isAdmin;

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public User() {}

    public User(String username, String password) {
        this.setName(username);
        this.pwHash = encoder.encode(password);
        this.isAdmin = isAdmin;
    }

    public boolean isMatchingPassword(String password) {
        return encoder.matches(password, this.pwHash);
    }

    public String getPwHash() {
        return pwHash;
    }
    public boolean isAdmin() {
        return isAdmin;
    }

}