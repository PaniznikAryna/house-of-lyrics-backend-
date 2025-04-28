package com.houseoflyrics.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @ManyToOne
    @JoinColumn(name = "id_instrument", nullable = false)
    private MusicalInstrument musicalInstrument;

    @Column(name = "nickname", nullable = false, length = 255)
    private String nickname;
    @Column(name = "mail", nullable = false, length = 255, unique = true)
    private String mail;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @CreationTimestamp
    @Column(name = "registration_date", updatable = false)
    private LocalDateTime registrationDate = LocalDateTime.now();

    @Column(name = "profile_picture", nullable = false, length = 255)
    private String profilePicture;

    @Column(name = "admin", nullable = false)
    private boolean admin;

    public Users(){

    }

    public Users(MusicalInstrument musicalInstrument, String nickname, String mail, String password, String profilePicture, boolean admin) {
        this.musicalInstrument = musicalInstrument;
        this.nickname = nickname;
        this.mail = mail;
        this.password = password;
        this.profilePicture = profilePicture;
        this.admin = admin;
    }


    public MusicalInstrument getMusicalInstrument() {
        return musicalInstrument;
    }

    public void setMusicalInstrument(MusicalInstrument musicalInstrument) {
        this.musicalInstrument = musicalInstrument;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public String getNickname(){
        return nickname;
    }
    public void setNickname(String nickname){
        this.nickname = nickname;
    }

}
