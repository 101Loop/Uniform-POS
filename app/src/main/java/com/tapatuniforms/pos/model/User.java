package com.tapatuniforms.pos.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.auth0.android.jwt.JWT;

import java.util.Calendar;
import java.util.Objects;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String email;
    private String mobile;
    private String token;
    private String pin;
    private String createDate;
    private String lastLogin;

    public User(long id, String name, String email, String mobile, String token, String pin,
                String createDate, String lastLogin) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.token = token;
        this.pin = pin;
        this.createDate = createDate;
        this.lastLogin = lastLogin;
    }

    @Ignore
    public User(String token) {
        JWT jwt = new JWT(token);

        this.id = Long.valueOf(Objects.requireNonNull(jwt.getClaim("user_id").asString()));
        this.name = jwt.getClaim("name").asString();
        this.mobile = jwt.getClaim("mobile").asString();
        this.email = jwt.getClaim("email").asString();
        this.token = token;
        this.pin = "";

        String date = Calendar.getInstance().getTime().toString();
        createDate = date;
        lastLogin = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }
}
