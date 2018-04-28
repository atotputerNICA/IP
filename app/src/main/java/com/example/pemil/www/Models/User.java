package com.example.pemil.www.Models;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by pemil on 28.04.2018.
 *
 * User class.
 */

//@IgnoreExtraProperties
public class User {
    private Bitmap profileImage;
    private String name;
    private String surname;
    private String country;
    private String birthdate;
    private Long telephone;
    private String email;
    private String id;
    private String password;

    public User(Bitmap photo, String name, String surname, String country,
                String birthdate, Long telephone, String email, String id) {
        this.profileImage = photo;
        this.name = name;
        this.surname = surname;
        this.country = country;
        this.birthdate = birthdate;
        this.telephone = telephone;
        this.email = email;
        this.id = id;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public Long getTelephone() {
        return telephone;
    }

    public void setTelephone(Long telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void sendToDB() {
        String s = this.name + this.surname + this.id + this.email + this.telephone + this.country + this.birthdate;
        Log.e("SOMETHING TAG BLA BLA", s);
    }
}
