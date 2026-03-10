package com.alura.literalura.model;

import jakarta.persistence.*;

@Entity
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Integer nacimiento;
    private Integer muerte;

    public Autor(){}

    public Autor(String nombre, Integer nacimiento, Integer muerte){
        this.nombre = nombre;
        this.nacimiento = nacimiento;
        this.muerte = muerte;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getNacimiento() {
        return nacimiento;
    }

    public Integer getMuerte() {
        return muerte;
    }

    @Override
    public String toString() {
        return """
                -----------------------
                Autor: %s
                Nacimiento: %s
                Muerte: %s
                -----------------------
                """.formatted(nombre, nacimiento, muerte);
    }
}