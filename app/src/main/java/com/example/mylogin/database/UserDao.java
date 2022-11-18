package com.example.mylogin.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mylogin.model.HondanaUser;

import java.util.List;
@Dao

public interface UserDao {
    @Insert
    void insert (HondanaUser hondanaUser);

    @Update
    void update (HondanaUser hondanaUser);

    @Delete
    void delete (HondanaUser hondanaUser);

    @Query("SELECT * FROM users")
    List<HondanaUser> getAll();

    @Query("SELECT * FROM users WHERE UserID= :UserID")
    HondanaUser findById (int UserID);

    @Query("SELECT * FROM users WHERE UserID in (:UsersIDs)")
    List<HondanaUser>loadAllByIds(int[] UsersIDs);

    @Query("SELECT * FROM users WHERE email LIKE :email limit 1")
    HondanaUser findByEmail(String email);

    @Query("SELECT * FROM users WHERE userName LIKE :userName and password = :password")
    HondanaUser findByPass(String userName, String password);
}
