package com.houseoflyrics.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private int id_instrument;
    private String nickname;
    private String mail;
    private String password;
    @CreationTimestamp
    @Column(name = "registration_date", updatable = false)
    private LocalDateTime registration_date;
    private String profile_picture;
    private boolean admin;

    public User(){

    }

    public User(int id_instrument, String nickname, String mail, String password, LocalDateTime registration_date, String profile_picture, boolean admin){
        this.id_instrument = id_instrument;
        this.nickname = nickname;
        this.mail = mail;
        this.password = password;
        this.registration_date = registration_date;
        this.profile_picture = profile_picture;
        this.admin = admin;
    }

    public int getId_instrument() {
        return id_instrument;
    }

    public void setId_instrument(int id_instrument) {
        this.id_instrument = id_instrument;
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

    public LocalDateTime getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(LocalDateTime registration_date) {
        this.registration_date = registration_date;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
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
