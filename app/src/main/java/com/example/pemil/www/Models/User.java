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

    private String name;
    private String surname;
    private String country;
    private String birthday;
    private Long telephone;
    private String email;
    private String id;
    private String password;

    public User(String name, String surname, String country, String birthday, Long telephone,
                String email, String id, String password) {
        this.name = name;
        this.surname = surname;
        this.country = country;
        this.birthday = birthday;
        this.telephone = telephone;
        this.email = email;
        this.id = id;
        this.password = password;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", country='" + country + '\'' +
                ", birthday='" + birthday + '\'' +
                ", telephone=" + telephone +
                ", email='" + email + '\'' +
                ", id='" + id + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
