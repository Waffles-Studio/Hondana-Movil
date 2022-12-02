package com.example.mylogin.model;

public class Book {
    String autor;
    String nombre;
    public Book(){}

    public Book (String autor)
    {
        this.autor = autor;
        this.nombre = nombre;
    }


    public String getNombre(){
        return nombre;
    }

    public void setNombre(){
        this.nombre=nombre;
    }

    public String getAutor(){
        return autor;
    }

    public void setAutor(){
       this.autor=autor;
    }


}
