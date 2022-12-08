package com.example.sistemagestual;

import java.util.ArrayList;

public class conjuntoUbicaciones {

    private final ArrayList<UbicacionIndoor> nodos = new ArrayList<>();
    private final ArrayList<CaminoIndoor> caminos = new ArrayList<>();
    private final int identificadorUltimoNodo = -1;

    public conjuntoUbicaciones() {
        inicializarGrafo();
    }

    private void inicializarGrafo() {

        //UbicacionIndoor Aula3_3, Aula0_1, Edi2Piso3, Edi2Piso2, Edi2Piso1,Edi2Acceso;
        //CaminoIndoor AulasCaminoEscaleras1,AulasBajada1,AulasBajada2,AulasBajada3,AulasCaminoSecretaria1,AulasCaminoSecretaria2;


        //EDIFICIO 2 : AULAS
        nodos.add(new UbicacionIndoor("al Aula 3.3", 2, 3, 1));
        nodos.add(new UbicacionIndoor("al Aula Julio Ortega", 2, 1, 4));
        nodos.add(new UbicacionIndoor("las escaleras de la 3ª planta", 2, 3, 2));
        nodos.add(new UbicacionIndoor("las escaleras de la 2ª planta", 2, 2, 3));
        nodos.add(new UbicacionIndoor("las escaleras de la 1ª planta", 2, 1, 5));
        nodos.add(new UbicacionIndoor("consejería", 2, 1, 6));

        caminos.add(new CaminoIndoor(1, 1, 2, 270, 10, false));
        caminos.add(new CaminoIndoor(2, 2, 3, 20, 15, true));
        caminos.add(new CaminoIndoor(3, 3, 5, 140, 25, true));
        caminos.add(new CaminoIndoor(4, 5, 6, 120, 12, false));
        caminos.add(new CaminoIndoor(5, 4, 6, 230, 10, false));

        //EDIFICIO 1 : DESPACHOS
        nodos.add(new UbicacionIndoor("la administración", 1, 1, 7));
        nodos.add(new UbicacionIndoor("las escaleras junto a la puerta de acceso", 1, 1, 15));
        nodos.add(new UbicacionIndoor("las escaleras junto consejería de la 1ª planta", 1, 1, 14));
        nodos.add(new UbicacionIndoor("las escaleras de la 2ª planta", 1, 2, 10));
        nodos.add(new UbicacionIndoor("las escaleras de la 3ª planta", 1, 3, 12));
        nodos.add(new UbicacionIndoor("las escaleras de la planta baja", 1, -1, 9));
        nodos.add(new UbicacionIndoor("la cafetería", 1, -1, 8));
        nodos.add(new UbicacionIndoor("la biblioteca", 1, 2, 11));
        nodos.add(new UbicacionIndoor("el despacho de Marcelino", 1, 3, 13));

        caminos.add(new CaminoIndoor(6, 15, 7, 150, 4, false));
        caminos.add(new CaminoIndoor(7, 14, 7, 10, 23, false));
        caminos.add(new CaminoIndoor(8, 14, 10, 60, 3, true));
        caminos.add(new CaminoIndoor(9, 15, 9, 140, 2, true));
        caminos.add(new CaminoIndoor(10, 10, 12, 20, 12, true));
        caminos.add(new CaminoIndoor(11, 9, 8, 185, 16, false));
        caminos.add(new CaminoIndoor(12, 10, 11, 280, 24, false));
        caminos.add(new CaminoIndoor(13, 12, 13, 140, 12, false));

        caminos.add(new CaminoIndoor(14, 6, 15, 24, 20, false));


    }

    public ArrayList<ArrayList<Integer>> generarCamino(int identificadorOrigen, int identificadorDestino) {

        boolean origen = false;
        boolean destino = false;
        UbicacionIndoor nodoOrigen = null;
        UbicacionIndoor nodoDestino = null;
        UbicacionIndoor axiliar;

        for (int i = 0; i < nodos.size() && (!origen || !destino); i++) {
            if (identificadorOrigen == nodos.get(i).getIdentificador()) {
                nodoOrigen = nodos.get(i);
                origen = true;
            }
            if (identificadorDestino == nodos.get(i).getIdentificador()) {
                nodoDestino = nodos.get(i);
                destino = true;
            }
        }

        ArrayList<ArrayList<Integer>> caminoTotal = new ArrayList<>(2);

        ArrayList<Integer> subCamino = new ArrayList<>(3);
        ArrayList<ArrayList<Integer>> multiplesSubCaminos = new ArrayList<>(3);
        boolean encontrado = false;

        if (origen && destino) {

            //1. SI ES NECESARIO CAMBIAR DE EDIFICIO
            if (nodoOrigen.getEdificio() != nodoDestino.getEdificio()) {

                //Bajar a planta baja para cambiar de edificio
                if (nodoOrigen.getEdificio() == 2 && nodoOrigen.getPlanta() != 1) {
                    UbicacionIndoor nodoAux = null;
                    for (int j = 0; j < nodos.size(); j++) {
                        if (nodos.get(j).getIdentificador() == 6) {
                            nodoAux = nodos.get(j);
                        }
                    }
                    multiplesSubCaminos = cambiarPlantaEdificio2(nodoOrigen, nodoAux);
                    caminoTotal.addAll(multiplesSubCaminos);
                    nodoOrigen = nodoAux;
                } else if (nodoOrigen.getEdificio() == 1 && nodoOrigen.getPlanta() != 1) {
                    UbicacionIndoor nodoAux = null;
                    for (int j = 0; j < nodos.size(); j++) {
                        if (nodos.get(j).getIdentificador() == 15) {
                            nodoAux = nodos.get(j);
                        }
                    }
                    multiplesSubCaminos = cambiarPlantaEdificio1(nodoOrigen, nodoAux);
                    caminoTotal.addAll(multiplesSubCaminos);
                    nodoOrigen = nodoAux;
                } else {
                    if (nodoOrigen.getEdificio() == 2 && nodoOrigen.getIdentificador() != 6) {
                        encontrado = false;
                        identificadorOrigen = nodoOrigen.getIdentificador();
                        identificadorDestino = 6;
                        for (int i = 0; i < caminos.size() && !encontrado; i++) {
                            if ((identificadorOrigen == caminos.get(i).getNodoOrigen() && identificadorDestino == caminos.get(i).getNodoDestino()) ||
                                    (identificadorDestino == caminos.get(i).getNodoOrigen() && identificadorOrigen == caminos.get(i).getNodoDestino())) {
                                encontrado = true;
                                subCamino = caminos.get(i).getCamino(identificadorOrigen, identificadorDestino);
                                caminoTotal.add(subCamino);
                            }
                        }
                    } else if (nodoOrigen.getEdificio() == 1 && nodoOrigen.getIdentificador() != 15) {
                        encontrado = false;
                        identificadorOrigen = nodoOrigen.getIdentificador();
                        identificadorDestino = 15;
                        for (int i = 0; i < caminos.size() && !encontrado; i++) {
                            if ((identificadorOrigen == caminos.get(i).getNodoOrigen() && identificadorDestino == caminos.get(i).getNodoDestino()) ||
                                    (identificadorDestino == caminos.get(i).getNodoOrigen() && identificadorOrigen == caminos.get(i).getNodoDestino())) {
                                encontrado = true;
                                subCamino = caminos.get(i).getCamino(identificadorOrigen, identificadorDestino);
                                caminoTotal.add(subCamino);
                            }
                        }
                    }
                }

                //CAMBIO DE EDIFICIO
                if (nodoOrigen.getEdificio() == 1) {
                    encontrado = false;
                    identificadorOrigen = 15;
                    identificadorDestino = 6;
                    for (int i = 0; i < caminos.size() && !encontrado; i++) {
                        if ((identificadorOrigen == caminos.get(i).getNodoOrigen() && identificadorDestino == caminos.get(i).getNodoDestino()) ||
                                (identificadorDestino == caminos.get(i).getNodoOrigen() && identificadorOrigen == caminos.get(i).getNodoDestino())) {
                            encontrado = true;
                            subCamino = caminos.get(i).getCamino(identificadorOrigen, identificadorDestino);
                            caminoTotal.add(subCamino);

                            for (int j = 0; j < nodos.size(); j++) {
                                if (nodos.get(j).getIdentificador() == 6) {
                                    nodoOrigen = nodos.get(j);
                                }
                            }
                        }
                    }
                } else {
                    encontrado = false;
                    identificadorOrigen = 6;
                    identificadorDestino = 15;
                    for (int i = 0; i < caminos.size() && !encontrado; i++) {
                        if ((identificadorOrigen == caminos.get(i).getNodoOrigen() && identificadorDestino == caminos.get(i).getNodoDestino()) ||
                                (identificadorDestino == caminos.get(i).getNodoOrigen() && identificadorOrigen == caminos.get(i).getNodoDestino())) {
                            encontrado = true;
                            subCamino = caminos.get(i).getCamino(identificadorOrigen, identificadorDestino);
                            caminoTotal.add(subCamino);
                        }
                    }

                    for (int j = 0; j < nodos.size(); j++) {
                        if (nodos.get(j).getIdentificador() == 15) {
                            nodoOrigen = nodos.get(j);
                        }
                    }
                }


                identificadorOrigen = nodoOrigen.getIdentificador();
                identificadorDestino = nodoDestino.getIdentificador();

            }

            //2. IR A LA PLANTA DE DESTINO
            if (nodoOrigen.getPlanta() != nodoDestino.getPlanta()) {
                if (nodoOrigen.getEdificio() == 2) {
                    multiplesSubCaminos = cambiarPlantaEdificio2(nodoOrigen, nodoDestino);
                    caminoTotal.addAll(multiplesSubCaminos);
                    identificadorOrigen = checkPointPlantaEdificio2(nodoDestino.getPlanta());
                } else {
                    multiplesSubCaminos = cambiarPlantaEdificio1(nodoOrigen, nodoDestino);
                    caminoTotal.addAll(multiplesSubCaminos);
                    identificadorOrigen = checkPointPlantaEdificio1(nodoDestino.getPlanta());
                }
            }

            //3. BUSCAR EL CAMINO ADECUADO
            encontrado = false;
            for (int i = 0; i < caminos.size() && !encontrado; i++) {
                if ((identificadorOrigen == caminos.get(i).getNodoOrigen() && identificadorDestino == caminos.get(i).getNodoDestino()) ||
                        (identificadorDestino == caminos.get(i).getNodoOrigen() && identificadorOrigen == caminos.get(i).getNodoDestino())) {
                    encontrado = true;
                    subCamino = caminos.get(i).getCamino(identificadorOrigen, identificadorDestino);
                    caminoTotal.add(subCamino);
                }
            }

        }

        return caminoTotal;
    }


    //Función que genera el path para subir y bajar una planta
    private ArrayList<ArrayList<Integer>> cambiarPlantaEdificio2(UbicacionIndoor origen, UbicacionIndoor destino) {
        ArrayList<ArrayList<Integer>> cambio = new ArrayList<>();
        boolean encontrado = false;
        int identificadorOrigen = origen.getIdentificador();
        int identificadorDestino = destino.getIdentificador();

        // SI QUEREMOS SUBIR
        if (origen.getPlanta() < destino.getPlanta()) {
            if (origen.getPlanta() == 1) {
                //Si no se esta en secretaría ( checkPointPrincipal del edificio ), ir a secretaria
                if (identificadorOrigen != 6) {
                    identificadorDestino = 6;
                    for (int i = 0; i < caminos.size() && !encontrado; i++) {
                        if ((identificadorOrigen == caminos.get(i).getNodoOrigen() && identificadorDestino == caminos.get(i).getNodoDestino()) ||
                                (identificadorDestino == caminos.get(i).getNodoOrigen() && identificadorOrigen == caminos.get(i).getNodoDestino())) {
                            cambio.add(caminos.get(i).getCamino(identificadorOrigen, identificadorDestino));
                            encontrado = true;
                        }
                    }
                }

                //Desde secretaria ir a las escaleras
                encontrado = false;
                identificadorOrigen = 6;
                identificadorDestino = 5;
                for (int i = 0; i < caminos.size() && !encontrado; i++) {
                    if ((identificadorOrigen == caminos.get(i).getNodoOrigen() && identificadorDestino == caminos.get(i).getNodoDestino()) ||
                            (identificadorDestino == caminos.get(i).getNodoOrigen() && identificadorOrigen == caminos.get(i).getNodoDestino())) {
                        cambio.add(caminos.get(i).getCamino(identificadorOrigen, identificadorDestino));
                        encontrado = true;
                    }
                }

                //Subir las escaleras
                encontrado = false;
                identificadorOrigen = 5;
                identificadorDestino = 3;
                for (int i = 0; i < caminos.size() && !encontrado; i++) {
                    if ((identificadorOrigen == caminos.get(i).getNodoOrigen() && identificadorDestino == caminos.get(i).getNodoDestino()) ||
                            (identificadorDestino == caminos.get(i).getNodoOrigen() && identificadorOrigen == caminos.get(i).getNodoDestino())) {
                        cambio.add(caminos.get(i).getCamino(identificadorOrigen, identificadorDestino));
                        encontrado = true;
                    }
                }

                // Si el destino esta en planta 3 si es necesario
                if (destino.getPlanta() == 3) {
                    encontrado = false;
                    identificadorOrigen = 3;
                    identificadorDestino = 2;
                    for (int i = 0; i < caminos.size() && !encontrado; i++) {
                        if ((identificadorOrigen == caminos.get(i).getNodoOrigen() && identificadorDestino == caminos.get(i).getNodoDestino()) ||
                                (identificadorDestino == caminos.get(i).getNodoOrigen() && identificadorOrigen == caminos.get(i).getNodoDestino())) {
                            cambio.add(caminos.get(i).getCamino(identificadorOrigen, identificadorDestino));
                            encontrado = true;
                        }
                    }
                }
            }
        } else {
            // SI QUEREMOS BAJAR

            //En el caso de que estemos en el piso 3
            if (origen.getPlanta() == 3) {
                //Ir a las escales
                encontrado = false;
                identificadorOrigen = origen.getIdentificador();
                identificadorDestino = 2;
                for (int i = 0; i < caminos.size() && !encontrado; i++) {
                    if ((identificadorOrigen == caminos.get(i).getNodoOrigen() && identificadorDestino == caminos.get(i).getNodoDestino()) ||
                            (identificadorDestino == caminos.get(i).getNodoOrigen() && identificadorOrigen == caminos.get(i).getNodoDestino())) {
                        cambio.add(caminos.get(i).getCamino(identificadorOrigen, identificadorDestino));
                        encontrado = true;
                    }
                }

                //bajar al 2
                encontrado = false;
                identificadorOrigen = 2;
                identificadorDestino = 3;
                for (int i = 0; i < caminos.size() && !encontrado; i++) {
                    if ((identificadorOrigen == caminos.get(i).getNodoOrigen() && identificadorDestino == caminos.get(i).getNodoDestino()) ||
                            (identificadorDestino == caminos.get(i).getNodoOrigen() && identificadorOrigen == caminos.get(i).getNodoDestino())) {
                        cambio.add(caminos.get(i).getCamino(identificadorOrigen, identificadorDestino));
                        encontrado = true;
                    }
                }
            }

            //Bajar a la planta uno si es necesario
            if (destino.getPlanta() == 1) {
                //Bajamos a planta 1
                encontrado = false;
                identificadorOrigen = 3;
                identificadorDestino = 5;
                for (int i = 0; i < caminos.size() && !encontrado; i++) {
                    if ((identificadorOrigen == caminos.get(i).getNodoOrigen() && identificadorDestino == caminos.get(i).getNodoDestino()) ||
                            (identificadorDestino == caminos.get(i).getNodoOrigen() && identificadorOrigen == caminos.get(i).getNodoDestino())) {
                        cambio.add(caminos.get(i).getCamino(identificadorOrigen, identificadorDestino));
                        encontrado = true;
                    }
                }
            }

            //Ir hasta secretaria ( checkPointPrincipal del edificio )
            encontrado = false;
            identificadorOrigen = 5;
            identificadorDestino = 6;
            for (int i = 0; i < caminos.size() && !encontrado; i++) {
                if ((identificadorOrigen == caminos.get(i).getNodoOrigen() && identificadorDestino == caminos.get(i).getNodoDestino()) ||
                        (identificadorDestino == caminos.get(i).getNodoOrigen() && identificadorOrigen == caminos.get(i).getNodoDestino())) {
                    cambio.add(caminos.get(i).getCamino(identificadorOrigen, identificadorDestino));
                    encontrado = true;
                }
            }


        }

        return cambio;
    }

    private ArrayList<ArrayList<Integer>> cambiarPlantaEdificio1(UbicacionIndoor origen, UbicacionIndoor destino) {
        ArrayList<ArrayList<Integer>> cambio = new ArrayList<>();
        boolean encontrado = false;
        int identificadorOrigen = origen.getIdentificador();
        int identificadorDestino = destino.getIdentificador();

        if (origen.getPlanta() != 1) {
            if (identificadorOrigen != checkPointPlantaEdificio1(origen.getPlanta())) {
                encontrado = false;
                identificadorOrigen = origen.getIdentificador();
                identificadorDestino = checkPointPlantaEdificio1(origen.getPlanta());
                for (int i = 0; i < caminos.size() && !encontrado; i++) {
                    if ((identificadorOrigen == caminos.get(i).getNodoOrigen() && identificadorDestino == caminos.get(i).getNodoDestino()) ||
                            (identificadorDestino == caminos.get(i).getNodoOrigen() && identificadorOrigen == caminos.get(i).getNodoDestino())) {
                        cambio.add(caminos.get(i).getCamino(identificadorOrigen, identificadorDestino));
                        encontrado = true;
                    }
                }
            }
        } else {
            if (destino.getPlanta() > 1) {
                encontrado = false;
                identificadorOrigen = origen.getIdentificador();
                identificadorDestino = 14;
            } else {
                encontrado = false;
                identificadorOrigen = origen.getIdentificador();
                identificadorDestino = 15;
            }

            if (identificadorOrigen != identificadorDestino) {
                encontrado = false;
                identificadorOrigen = origen.getIdentificador();
                identificadorDestino = checkPointPlantaEdificio1(origen.getPlanta());
                for (int i = 0; i < caminos.size() && !encontrado; i++) {
                    if ((identificadorOrigen == caminos.get(i).getNodoOrigen() && identificadorDestino == caminos.get(i).getNodoDestino()) ||
                            (identificadorDestino == caminos.get(i).getNodoOrigen() && identificadorOrigen == caminos.get(i).getNodoDestino())) {
                        cambio.add(caminos.get(i).getCamino(identificadorOrigen, identificadorDestino));
                        encontrado = true;
                    }
                }
            }
        }


        // SI QUEREMOS SUBIR
        if (origen.getPlanta() < destino.getPlanta()) {
            //Si estamos en la planta baja subir
            if (origen.getPlanta() == -1) {
                encontrado = false;
                identificadorOrigen = checkPointPlantaEdificio1(-1);
                identificadorDestino = 15;
                for (int i = 0; i < caminos.size() && !encontrado; i++) {
                    if ((identificadorOrigen == caminos.get(i).getNodoOrigen() && identificadorDestino == caminos.get(i).getNodoDestino()) ||
                            (identificadorDestino == caminos.get(i).getNodoOrigen() && identificadorOrigen == caminos.get(i).getNodoDestino())) {
                        cambio.add(caminos.get(i).getCamino(identificadorOrigen, identificadorDestino));
                        encontrado = true;
                    }
                }
                identificadorOrigen = 15;
            }
            if (origen.getPlanta() < 2 && destino.getPlanta() > 1) {
                //Si no se esta en secretaría ( checkPointPrincipal del edificio ), ir a secretaria
                if (identificadorOrigen != 7) {
                    encontrado = false;
                    identificadorDestino = 7;
                    for (int i = 0; i < caminos.size() && !encontrado; i++) {
                        if ((identificadorOrigen == caminos.get(i).getNodoOrigen() && identificadorDestino == caminos.get(i).getNodoDestino()) ||
                                (identificadorDestino == caminos.get(i).getNodoOrigen() && identificadorOrigen == caminos.get(i).getNodoDestino())) {
                            cambio.add(caminos.get(i).getCamino(identificadorOrigen, identificadorDestino));
                            encontrado = true;
                        }
                    }

                    //Desde secretaria ir a las escaleras
                    encontrado = false;
                    identificadorOrigen = 7;
                    identificadorDestino = 14;
                    for (int i = 0; i < caminos.size() && !encontrado; i++) {
                        if ((identificadorOrigen == caminos.get(i).getNodoOrigen() && identificadorDestino == caminos.get(i).getNodoDestino()) ||
                                (identificadorDestino == caminos.get(i).getNodoOrigen() && identificadorOrigen == caminos.get(i).getNodoDestino())) {
                            cambio.add(caminos.get(i).getCamino(identificadorOrigen, identificadorDestino));
                            encontrado = true;
                        }
                    }

                    //Subir las escaleras
                    encontrado = false;
                    identificadorOrigen = 14;
                    identificadorDestino = 10;
                    for (int i = 0; i < caminos.size() && !encontrado; i++) {
                        if ((identificadorOrigen == caminos.get(i).getNodoOrigen() && identificadorDestino == caminos.get(i).getNodoDestino()) ||
                                (identificadorDestino == caminos.get(i).getNodoOrigen() && identificadorOrigen == caminos.get(i).getNodoDestino())) {
                            cambio.add(caminos.get(i).getCamino(identificadorOrigen, identificadorDestino));
                            encontrado = true;
                        }
                    }
                }

            }
            // Si el destino esta en planta 3 si es necesario
            if (destino.getPlanta() == 3) {
                encontrado = false;
                identificadorOrigen = 10;
                identificadorDestino = 12;
                for (int i = 0; i < caminos.size() && !encontrado; i++) {
                    if ((identificadorOrigen == caminos.get(i).getNodoOrigen() && identificadorDestino == caminos.get(i).getNodoDestino()) ||
                            (identificadorDestino == caminos.get(i).getNodoOrigen() && identificadorOrigen == caminos.get(i).getNodoDestino())) {
                        cambio.add(caminos.get(i).getCamino(identificadorOrigen, identificadorDestino));
                        encontrado = true;
                    }
                }
            }
        } else {
            // SI QUEREMOS BAJAR
            if (origen.getPlanta() > 1) {
                //En el caso de que estemos en el piso 3
                if (origen.getPlanta() == 3) {
                    //bajar al 2
                    encontrado = false;
                    identificadorOrigen = 12;
                    identificadorDestino = 10;
                    for (int i = 0; i < caminos.size() && !encontrado; i++) {
                        if ((identificadorOrigen == caminos.get(i).getNodoOrigen() && identificadorDestino == caminos.get(i).getNodoDestino()) ||
                                (identificadorDestino == caminos.get(i).getNodoOrigen() && identificadorOrigen == caminos.get(i).getNodoDestino())) {
                            cambio.add(caminos.get(i).getCamino(identificadorOrigen, identificadorDestino));
                            encontrado = true;
                        }
                    }
                }

                //Bajar a la planta uno si es necesario
                if (destino.getPlanta() < 2) {
                    //Bajamos a planta 1
                    encontrado = false;
                    identificadorOrigen = 10;
                    identificadorDestino = 14;
                    for (int i = 0; i < caminos.size() && !encontrado; i++) {
                        if ((identificadorOrigen == caminos.get(i).getNodoOrigen() && identificadorDestino == caminos.get(i).getNodoDestino()) ||
                                (identificadorDestino == caminos.get(i).getNodoOrigen() && identificadorOrigen == caminos.get(i).getNodoDestino())) {
                            cambio.add(caminos.get(i).getCamino(identificadorOrigen, identificadorDestino));
                            encontrado = true;
                        }
                    }

                    //Ir a secretaria
                    encontrado = false;
                    identificadorOrigen = 14;
                    identificadorDestino = 7;
                    for (int i = 0; i < caminos.size() && !encontrado; i++) {
                        if ((identificadorOrigen == caminos.get(i).getNodoOrigen() && identificadorDestino == caminos.get(i).getNodoDestino()) ||
                                (identificadorDestino == caminos.get(i).getNodoOrigen() && identificadorOrigen == caminos.get(i).getNodoDestino())) {
                            cambio.add(caminos.get(i).getCamino(identificadorOrigen, identificadorDestino));
                            encontrado = true;
                        }
                    }

                    if (destino.getIdentificador() != 7) {
                        encontrado = false;
                        identificadorOrigen = 7;
                        identificadorDestino = 15;
                        for (int i = 0; i < caminos.size() && !encontrado; i++) {
                            if ((identificadorOrigen == caminos.get(i).getNodoOrigen() && identificadorDestino == caminos.get(i).getNodoDestino()) ||
                                    (identificadorDestino == caminos.get(i).getNodoOrigen() && identificadorOrigen == caminos.get(i).getNodoDestino())) {
                                cambio.add(caminos.get(i).getCamino(identificadorOrigen, identificadorDestino));
                                encontrado = true;
                            }
                        }
                    }
                }


            }

            //Bajar a la planta baja si es necesario
            if (destino.getPlanta() == -1) {

                //Bajamos a planta baja
                encontrado = false;
                identificadorOrigen = 15;
                identificadorDestino = 9;
                for (int i = 0; i < caminos.size() && !encontrado; i++) {
                    if ((identificadorOrigen == caminos.get(i).getNodoOrigen() && identificadorDestino == caminos.get(i).getNodoDestino()) ||
                            (identificadorDestino == caminos.get(i).getNodoOrigen() && identificadorOrigen == caminos.get(i).getNodoDestino())) {
                        cambio.add(caminos.get(i).getCamino(identificadorOrigen, identificadorDestino));
                        encontrado = true;
                    }
                }
            }

        }
        return cambio;
    }

    //Función que devuelve el identificador del punto principal de una planta
    private int checkPointPlantaEdificio2(int planta) {
        int identificadorDelCheckPoint = 0;
        if (planta == 1) {
            identificadorDelCheckPoint = 6;
        } else if (planta == 2) {
            identificadorDelCheckPoint = 3;
        } else {
            identificadorDelCheckPoint = 2;
        }

        return identificadorDelCheckPoint;
    }

    private int checkPointPlantaEdificio1(int planta) {
        int identificadorDelCheckPoint = 0;
        if (planta == 1) {
            identificadorDelCheckPoint = 15;
        } else if (planta == 2) {
            identificadorDelCheckPoint = 10;
        } else if (planta == -1) {
            identificadorDelCheckPoint = 9;
        } else {
            identificadorDelCheckPoint = 12;
        }

        return identificadorDelCheckPoint;
    }

    public String asociarNombreDestino(int identificador, int grados) {
        boolean terminado = false;
        String nombre = "";
        int identificadorDestino = 0;
        UbicacionIndoor nodo;

        if (identificador != 20) {
            for (int i = 0; i < caminos.size() && !terminado; i++) {
                if (caminos.get(i).getIdentificador() == identificador) {
                    if (caminos.get(i).getGrados() == grados) {
                        terminado = true;
                        identificadorDestino = caminos.get(i).getNodoDestino();
                    } else {
                        terminado = true;
                        identificadorDestino = caminos.get(i).getNodoOrigen();
                    }
                }
            }

            terminado = false;
            for (int i = 0; i < nodos.size() && !terminado; i++) {
                if (nodos.get(i).getIdentificador() == identificadorDestino) {
                    nombre = nodos.get(i).getNombre();
                    terminado = true;
                }
            }
        } else {
            nombre = "el edificio de enfrente cruzando el patio";
        }

        return nombre;
    }

    public int identImagenDestino(int identificador, int grados) {
        boolean terminado = false;
        int identificadorDestino = 0;
        UbicacionIndoor nodo;

        for (int i = 0; i < caminos.size() && !terminado; i++) {
            if (caminos.get(i).getIdentificador() == identificador) {
                if (caminos.get(i).getGrados() == grados) {
                    terminado = true;
                    identificadorDestino = caminos.get(i).getNodoDestino();
                } else {
                    terminado = true;
                    identificadorDestino = caminos.get(i).getNodoOrigen();
                }
            }
        }


        return identificadorDestino;
    }

    //0 igual , 1 subir y 2 bajar
    public int detectarSubidaBajada(int identificador, int grados){
        boolean terminado = false;
        int identificadorDestino = 0;
        int identificadorOrigen = 0;

        int subir = 0;
        UbicacionIndoor origen = null,destino = null;

        for (int i = 0; i < caminos.size() && !terminado; i++) {
            if (caminos.get(i).getIdentificador() == identificador) {
                if (caminos.get(i).getGrados() == grados) {
                    terminado = true;
                    identificadorDestino = caminos.get(i).getNodoDestino();
                    identificadorOrigen = caminos.get(i).getNodoOrigen();
                } else {
                    terminado = true;
                    identificadorDestino = caminos.get(i).getNodoOrigen();
                    identificadorOrigen = caminos.get(i).getNodoDestino();
                }
            }
        }

        boolean encontrado1 = false,encontrado2 = false;
        for(int i = 0 ; i < nodos.size() && (!encontrado1 || !encontrado2); i++){
            if(identificadorOrigen == nodos.get(i).getIdentificador()){
                origen = nodos.get(i);
                encontrado1 = true;
            } else if (identificadorDestino == nodos.get(i).getIdentificador() ){
                destino = nodos.get(i);
                encontrado2 = true;
            }
        }

        if(origen.getEdificio() == destino.getEdificio()){
            if(origen.getPlanta() == destino.getPlanta()){
                subir = 0;
            } else if (origen.getPlanta() < destino.getPlanta()) {
                subir = 1;
            } else if (origen.getPlanta() > destino.getPlanta()){
                subir = 2;
            }
        }

        return subir;
    }

    public int plantaActual(int identificador, int grados){
        boolean terminado = false;
        int identificadorDestino = 0;

        int planta = 0;

        if(identificador == 14) {
            return 0;
        }

        for (int i = 0; i < caminos.size() && !terminado; i++) {
            if (caminos.get(i).getIdentificador() == identificador) {
                if (caminos.get(i).getGrados() == grados) {
                    terminado = true;
                    identificadorDestino = caminos.get(i).getNodoDestino();
                } else {
                    terminado = true;
                    identificadorDestino = caminos.get(i).getNodoOrigen();
                }
            }
        }

        terminado = false;
        for(int i = 0 ; i < nodos.size() && !terminado; i++){
            if(nodos.get(i).getIdentificador() == identificadorDestino){
                planta = nodos.get(i).getPlanta();
                terminado = true;
            }
        }

        return planta;
    }

}

