package com.example.demo.Dto;

public class ElementosPagina {
    private int numero;
    private boolean actual;

    public int getNumero() {
        return numero;
    }

    public boolean isActual() {
        return actual;
    }

    public ElementosPagina(int numero,boolean actual){
        this.numero=numero;
        this.actual=actual;
    }

    public ElementosPagina(){
        super();
    }

}
