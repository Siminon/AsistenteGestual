package com.example.sistemagestual;

public class UbicacionIndoor {

    private String nombre;
    //Valores posible 1 (administracion) , 2 (aulas) 3 (fuera)
    private int edificio;
    private int planta;
    private int identificador;
    //Imagenes
    //Conexiones

    public UbicacionIndoor(){
        nombre="";
        edificio=0;
        planta=0;
        identificador=0;
    }

    public  UbicacionIndoor(String Nombre_, int Edificio_,int Planta_, int Identificador_){
        nombre = Nombre_;
        edificio=Edificio_;
        planta=Planta_;
        identificador=Identificador_;
    }

    public int getIdentificador() {
        return identificador;
    }

    public int getEdificio(){
        return edificio;
    }

    public int getPlanta() {
        return planta;
    }

    public String getNombre(){return  nombre;};
}
