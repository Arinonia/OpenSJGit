package fr.arinonia.opensjgit.entity;

import jakarta.persistence.*;

import java.util.Objects;

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
    private String profile_picture = "";
    private boolean confirmed_mail = false;
    @Enumerated(EnumType.STRING)
    private Rank rank = Rank.USER;

    //Empty constructor for all the reflection process
    public User() {}

    public User(final Long id, final String username, final String email, final String password,
                final String creation_date, final boolean using_2fa, final String profile_picture,
                final boolean confirmed_mail) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.creation_date = creation_date;
        this.using_2fa = using_2fa;
        this.profile_picture = profile_picture;
        this.confirmed_mail = confirmed_mail;
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

    public String getProfile_picture() {
        return this.profile_picture;
    }

    public void setProfile_picture(final String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public boolean isConfirmed_mail() {
        return this.confirmed_mail;
    }

    public void setConfirmed_mail(final boolean confirmed_mail) {
        this.confirmed_mail = confirmed_mail;
    }

    public Rank getRank() {
        return this.rank;
    }

    public void setRank(final Rank rank) {
        this.rank = rank;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
