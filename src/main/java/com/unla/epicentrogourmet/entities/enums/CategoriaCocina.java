package com.unla.epicentrogourmet.entities.enums;

public enum CategoriaCocina {

    COCINERO(150000),
    COCINERO_AYUDANTE(100000),
    LAVAPLATOS(50000);

    private final double plus;
    CategoriaCocina(double plus){
        this.plus = plus;
    }

    public double getPlus(){
        return plus;
    }

}
