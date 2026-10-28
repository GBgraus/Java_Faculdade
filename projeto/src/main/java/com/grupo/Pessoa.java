package com.exemlo;

public class Pessoa {
    private int id;
    private String nome;
    private int idade;
    private double altura;

    public Pessoa(String nome, int idade, double altura){
        this(0, nome, idade, altura);
    }
    public Pessoa(int id, String nome, int idade, double altura){
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.altura = altura;
    }
}