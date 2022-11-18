package com.example.mylogin.repository;

import com.example.mylogin.database.UserDao;
import com.example.mylogin.model.HondanaUser;

import java.util.List;

public class UserRepository implements UserDao{
    private UserDao dao;
    public UserRepository (UserDao userDao) {this.dao=userDao;}

    @Override
    public void insert(HondanaUser hondanaUser) {
        dao.insert(hondanaUser);
    }

    @Override
    public void update(HondanaUser hondanaUser) {
        dao.update(hondanaUser);
    }

    @Override
    public void delete(HondanaUser hondanaUser) {
        dao.delete(hondanaUser);
    }

    @Override
    public List<HondanaUser> getAll() {
        return  dao.getAll();
    }

    @Override
    public HondanaUser findById(int UserID) {
        return dao.findById(UserID);
    }

    @Override
    public List<HondanaUser> loadAllByIds(int[] UsersIDs) {
        return dao.loadAllByIds(UsersIDs);
    }

    @Override
    public HondanaUser findByEmail(String email) {
        return dao.findByEmail(email);
    }

    @Override
    public HondanaUser findByPass(String userName, String password) {
        return dao.findByPass(userName, password);
    }
}
