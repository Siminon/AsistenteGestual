package com.example.sistemagestual;

import java.util.ArrayList;

public class CaminoIndoor {

    private int identificador;
    private int nodoOrigen;
    private int nodoDestino;
    private int grados;
    private int pasos;
    private boolean bajadaSubida ;
    private String nombreOrigen;
    private String nombreDestino;


    public CaminoIndoor(){
        grados = 0;
        pasos = 0;
        nodoDestino = 0;
        nodoOrigen = 0;
        identificador = 0;
        bajadaSubida = false;
    }

    public CaminoIndoor(  int identificador_ , int nodoOrigen_ ,int nodoDestino_, int grados_, int pasos_, boolean bajada ){
        grados = grados_;
        pasos = pasos_;
        nodoDestino = nodoDestino_;
        nodoOrigen = nodoOrigen_;
        identificador = identificador_;
        bajadaSubida = bajada;
    }

    public ArrayList<Integer> getCamino (int origen , int destino){

        ArrayList<Integer> camino = new ArrayList<>();

        if ( origen == nodoOrigen && destino == nodoDestino) {
            camino.add( identificador );
            camino.add( grados );
            camino.add( pasos );
        } else if (origen == nodoDestino && destino == nodoOrigen ){
            camino.add( identificador );
            int reversa = 360 - grados + 1;
            camino.add(  reversa );
            camino.add( pasos );
        }
        return camino;
    }

    public int getNodoOrigen(){
        return nodoOrigen;
    }

    public int getNodoDestino() {
        return nodoDestino;
    }

    public int getIdentificador() {
        return identificador;
    }

    public int getGrados(){
        return grados;
    }


}
