package com.example.mylogin.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mylogin.model.HondanaUser;

import java.util.List;

public class ListaUsersViewModel extends ViewModel{
    private MutableLiveData<List<HondanaUser>> lista;

    public ListaUsersViewModel() {
        this.lista=new MutableLiveData<List<HondanaUser>>();
    }

    public LiveData<List<HondanaUser>> getUsers(){
        return lista;
    }

    public void setUsers(List<HondanaUser> lista){
        this.lista.setValue(lista);
    }
}
