package com.example.mylogin.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import java.io.Serializable;

@Entity (tableName = "users")
public class HondanaUser implements Serializable{
    @NonNull
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "userID")
    private int userID;

    @NonNull
    @ColumnInfo (name = "userName")
    private String userName;

    @NonNull
    @ColumnInfo (name = "email")
    private String email;

    @NonNull
    @ColumnInfo (name = "password")
    private String password;

    @Ignore
    public HondanaUser() { }

    public HondanaUser(int userID, @NonNull String userName, @NonNull String email, @NonNull String password) {
        this.userID = userID;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @NonNull
    public String getUserName() {
        return userName;
    }

    public void setUserName(@NonNull String userName) {
        this.userName = userName;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }
}
