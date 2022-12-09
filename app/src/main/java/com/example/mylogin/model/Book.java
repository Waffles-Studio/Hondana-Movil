package com.example.mylogin.model;

public class Book {

    String IDLibro;
    String Titulo;
    String Autor;
    String ISBN;
    String Editorial;
    String NumeroPaginas;
    String Calificacion;
    String VinculoLibro;
    String VinculoImagen;
    String Sinopsis;

    public Book(){}

    public Book (String Autor,String Titulo)
    {
        this.Autor = Autor;
        this.Titulo = Titulo;

    }

    public String getIDLibro() {
        return IDLibro;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getNumeroPaginas() {
        return NumeroPaginas;
    }

    public String getCalificacion() {
        return Calificacion;
    }

    public String getVinculoLibro() {
        return VinculoLibro;
    }

    public String getVinculoImagen() {
        return VinculoImagen;
    }

    public String getEditorial() {
        return Editorial;
    }


    public String getSinopsis() {
        return Sinopsis;
    }

    public String getTitulo(){
        return Titulo;
    }

    public String getAutor(){
        return Autor;
    }

}
