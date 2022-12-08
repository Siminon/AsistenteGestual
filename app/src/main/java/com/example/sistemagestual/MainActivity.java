package com.example.sistemagestual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    int identOrigen = 0;
    int identDestino = 0;
    TextView resultados;
    Button buscar;
    conjuntoUbicaciones ubicaciones;
    Spinner origenText;
    Spinner destinoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        ubicaciones = new conjuntoUbicaciones();
        buscar = (Button) findViewById(R.id.buscarBotton);

        //my button clic
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Buscar();
            }
        });

        gpsBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGPS();
            }
        });


        String[] arraySpinnerOrigen = new String[] {
                "Seleccionar Origen", "Aula 3.3", "Aula Julio Ortega" , "Consejeria Edificio 1" , "Administracion", "Cafeteria" , "Biblioteca" , "Despachos"
        };

        String[] arraySpinnerDestino = new String[] {
                "Seleccionar Destino", "Aula 3.3",  "Aula Julio Ortega", "Consejeria Edificio 1", "Administracion", "Cafeteria" , "Biblioteca" , "Despachos"
        };

        origenText = (Spinner) findViewById(R.id.origenSpinner);
        destinoText = (Spinner) findViewById(R.id.destinoSpinner);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinnerOrigen);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        origenText.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinnerDestino);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinoText.setAdapter(adapter2);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void Buscar(){
        String textOrigen = origenText.getSelectedItem().toString();
        identOrigen = asociarIdentificador(textOrigen);
        String textDestino= destinoText.getSelectedItem().toString();
        identDestino = asociarIdentificador(textDestino);

        if (identOrigen != identDestino && !textOrigen.equals("Seleccionar Origen") && !textDestino.equals("Seleccionar Destino")) {
            ArrayList<ArrayList<Integer>> camino = ubicaciones.generarCamino(identOrigen, identDestino);

            Intent rutaActivity = new Intent(this, Ruta.class);
            rutaActivity.putExtra("ORIGEN", identOrigen);
            rutaActivity.putExtra("DESTINO",identDestino);
            startActivity(rutaActivity);
        }
    }

    private int asociarIdentificador(String ubi){
        int ident = 0;
        if(ubi.equals("Aula 3.3")){
            ident = 1;
        } else if (ubi.equals("Aula Julio Ortega")) {
            ident = 4;
        } else if (ubi.equals("Consejeria Edificio 1") ){
            ident = 6;
        } else if (ubi.equals("Administracion") ){
            ident = 7;
        } else if (ubi.equals("Cafeteria") ){
            ident = 8;
        } else if (ubi.equals("Biblioteca") ){
            ident = 11;
        } else if (ubi.equals("Despachos")){
            ident = 13;
        }
        return ident;
    }



}