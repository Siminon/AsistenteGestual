package com.example.sistemagestual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.hardware.SensorEvent;
import android.widget.TextView;
import android.hardware.SensorEventListener;
import android.widget.Toast;
import android.view.View;

import java.util.ArrayList;
import java.util.Locale;

public class Ruta extends AppCompatActivity {
    public static final Integer ActivityRecognitionRequestCode = 1;
    private SensorManager sensorManager;
    private Sensor stepDetector, orientationDetector;
    private ImageView arrowImage, locImage;
    private float DegreeStart = 0f;
    TextView StepEditText, DirectionText;
    private boolean stepDetectorAvailable, orientationDetectorAvailable = false;
    private int totalSteps = 0;
    private int step = 0;
    private int subida = 0;
    private int identOrigen, identDestino;
    private Button nextStep;
    private conjuntoUbicaciones ubicaciones;
    private ArrayList<ArrayList<Integer>> route;
    private ArrayList<PointF> touchPoints = null;
    private final float THRESHOLD = 8.0f;
    private final float THRESHOLD2 = 50.0f;
    private int plantaActual = 0;
    private TextToSpeech textToSpeechEngine;
    private int remainingSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ruta_layout);
        touchPoints = new ArrayList<>();
        getSupportActionBar().hide();

        // Tomar los valores seleccionados por el usuario y generar la ruta
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            identOrigen = extra.getInt("ORIGEN");
            identDestino = extra.getInt("DESTINO");
        }

        ubicaciones = new conjuntoUbicaciones();
        route = ubicaciones.generarCamino(identOrigen, identDestino);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, ActivityRecognitionRequestCode);
            }
        }

        this.textToSpeechEngine = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.SUCCESS) {
                    Log.e("TTS", "Inicio de la síntesis fallido");
                }
            }
        });

        nextStep = findViewById(R.id.nextStep);

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                nextStep();
            }
        });

        Movement.getInstance().addListener((event, movement) -> {
            if (movement > THRESHOLD) {
                GlobalData.getInstance().elapseTime = System.nanoTime() - GlobalData.getInstance().startTime;
                if (GlobalData.getInstance().elapseTime > GlobalData.getInstance().waitTime) {
                    nextStep.performClick();
                    GlobalData.updateLevel(plantaActual);
                    GlobalData.getInstance().startTime = System.nanoTime();
                }
            }
        });

        Movement2.getInstance().addListener((event, movement) -> {
            if (movement > THRESHOLD2) {
                Locale lang = new Locale("es");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    lang = this.textToSpeechEngine.getDefaultVoice().getLocale();
                }

                String texto = "error";
                String es = "le quedan " + (route.get(step).get(2)-totalSteps) + " pasos aproximadamente, con ángulo de " + arrowImage.getRotation() + " grados para llegar a" +
                        ubicaciones.asociarNombreDestino(route.get(step).get(0), route.get(step).get(1));
                String en = "aproximately " + (route.get(step).get(2)-totalSteps) + " steps remain, with angle of " + arrowImage.getRotation() + " degrees to reach" +
                        ubicaciones.asociarNombreDestino(route.get(step).get(0), route.get(step).get(1));

                if(lang.getLanguage().equals("es"))
                    texto = es;
                else
                    texto = en;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    this.textToSpeechEngine.speak(texto, TextToSpeech.QUEUE_FLUSH, null, "tts1");
                }
            }
        });

        ConstraintLayout mylayout = findViewById(R.id.touchlayout);

        mylayout.setOnTouchListener(new View.OnTouchListener() {
            public void setPoints(MotionEvent event) {
                touchPoints.clear();
                for (int index = 0; index < event.getPointerCount(); ++index)
                    touchPoints.add(new PointF(event.getX(index), event.getY(index)));
            }

            public void clear() {
                touchPoints = new ArrayList<>();
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventType = event.getActionMasked();

                switch (eventType) {
                    case MotionEvent.ACTION_DOWN:
                        this.setPoints(event);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        this.setPoints(event);
                        break;
                    case MotionEvent.ACTION_UP:
                        this.clear();
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        this.setPoints(event);
                        if (touchPoints.size() == 2) {
                            toPopUp(v, true);
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        this.clear();
                        toPopUp(v, false);
                        break;
                }
                return true;
            }
        });

        arrowImage = (ImageView) findViewById(R.id.compass_image);
        locImage = (ImageView) findViewById(R.id.locImage);
        StepEditText = (TextView) findViewById(R.id.stepText);
        DirectionText = findViewById(R.id.degreeText);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        if (stepDetector != null) {
            stepDetectorAvailable = true;
        } else {
            Toast.makeText(this, "No se encontro STEP_DETECTOR", Toast.LENGTH_SHORT).show();
            stepDetectorAvailable = false;
        }

        orientationDetector = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        if (orientationDetector != null) {
            orientationDetectorAvailable = true;
        } else {
            Toast.makeText(this, "No se detecto ORIENTATION", Toast.LENGTH_SHORT).show();
            orientationDetectorAvailable = false;
        }

        StepEditText.setText("Pasos restantes: " + String.valueOf(route.get(0).get(2)));

        String location = "loccamino"+route.get(step).get(0) + "destino"+String.valueOf(ubicaciones.identImagenDestino(route.get(step).get(0), route.get(step).get(1)));
        locImage.setImageResource(getResources().getIdentifier(location, "drawable", getPackageName()));
        DirectionText.setText("Dirígete a " + ubicaciones.asociarNombreDestino(route.get(step).get(0), route.get(step).get(1)));
        plantaActual = ubicaciones.plantaActual(route.get(step).get(0), route.get(step).get(1));
        GlobalData.updateLevel(plantaActual);
    }

    SensorEventListener StepsSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            totalSteps += sensorEvent.values[0];
            remainingSteps = route.get(step).get(2) - totalSteps;
            if (remainingSteps < 0) {
                remainingSteps = 0;
            }
            StepEditText.setText("Pasos restantes: " + String.valueOf(remainingSteps));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            StepEditText.setText("Algo detecta");
        }
    };

    SensorEventListener OrientationSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (subida == 0) {
                float degree = Math.round(event.values[0]);
                RotateAnimation ra = new RotateAnimation(
                        (float) route.get(step).get(1) + DegreeStart,
                        degree,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);

                ra.setFillAfter(true);
                ra.setDuration(210);

                arrowImage.startAnimation(ra);
                DegreeStart = -degree;
            } else {
                arrowImage.setRotation(0 + DegreeStart);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        Movement.getInstance().stop();
        Movement2.getInstance().stop();
        if (stepDetectorAvailable)
            sensorManager.unregisterListener(StepsSensorEventListener);
        if (orientationDetectorAvailable)
            sensorManager.unregisterListener(OrientationSensorEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Movement.getInstance().start();
        Movement2.getInstance().start();

        if (stepDetectorAvailable)
            sensorManager.registerListener(StepsSensorEventListener, stepDetector, SensorManager.SENSOR_DELAY_GAME);
        if (orientationDetectorAvailable)
            sensorManager.registerListener(OrientationSensorEventListener, orientationDetector, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ActivityRecognitionRequestCode && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }
    }

    public void nextStep() {
        if (step < route.size() - 1) {
            step += 1;
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.confirmation_effect);
            mp.start();
            totalSteps = 0;
            plantaActual = ubicaciones.plantaActual(route.get(step).get(0), route.get(step).get(1));
            StepEditText.setText("Pasos restantes: " + String.valueOf(route.get(step).get(2)));
            GlobalData.updateLevel(plantaActual);

            subida = ubicaciones.detectarSubidaBajada(route.get(step).get(0), route.get(step).get(1));

            Locale lang = new Locale("es");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                lang = this.textToSpeechEngine.getDefaultVoice().getLocale();
            }

            if (step == route.size()-1) {
                if(lang.getLanguage().equals("es")) {
                    nextStep.setText("Finalizar");
                } else {
                    nextStep.setText("Finish");
                }
            }

            if (subida == 1) {
                String arrow = "escalerassubida";
                arrowImage.setImageResource(getResources().getIdentifier(arrow, "drawable", getPackageName()));
            } else if (subida == 2) {
                String arrow = "escalerasbajada";
                arrowImage.setImageResource(getResources().getIdentifier(arrow, "drawable", getPackageName()));
            } else {
                String arrow = "arrow";
                arrowImage.setImageResource(getResources().getIdentifier(arrow, "drawable", getPackageName()));
            }

            String location = "loccamino"+route.get(step).get(0) + "destino"+String.valueOf(ubicaciones.identImagenDestino(route.get(step).get(0), route.get(step).get(1)));
            locImage.setImageResource(getResources().getIdentifier(location, "drawable", getPackageName()));

            DirectionText.setText("Dirígete a " + ubicaciones.asociarNombreDestino(route.get(step).get(0), route.get(step).get(1)));
        } else {
            Intent volverMenu = new Intent(Ruta.this, MainActivity.class);
            startActivity(volverMenu);
            this.finish();
        }
    }

    public void toPopUp(View view, boolean flag) {
        Intent intent = new Intent(Ruta.this, PopUp.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        if (flag)
            intent.putExtra("keep", true);
        else
            intent.putExtra("keep", false);

        startActivity(intent);
    }
}
