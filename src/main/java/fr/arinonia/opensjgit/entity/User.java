package fr.arinonia.opensjgit.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String creation_date;//TODO maybe just use Date
    private boolean using_2fa = false;//TODO 2fa will be not implemented yet

    //Empty constructor for all the reflection process
    public User() {}

    public User(final Long id, final String username, final String email, final String password, final String creation_date, final boolean using_2fa) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.creation_date = creation_date;
        this.using_2fa = using_2fa;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getCreation_date() {
        return this.creation_date;
    }

    public void setCreation_date(final String creation_date) {
        this.creation_date = creation_date;
    }

    public boolean isUsing_2fa() {
        return this.using_2fa;
    }

    public void setUsing_2fa(final boolean using_2fa) {
        this.using_2fa = using_2fa;
    }
}
