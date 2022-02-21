package com.example.service;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private float[] current_light;
    private float[] last_light;

    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView lightAngle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lightAngle = findViewById(R.id.lightValue);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        current_light = new float[1];
        current_light[0] = 0;
        last_light = new float[1];
        last_light[0] = 0;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            lightAngle.setText(String.valueOf(event.values[0]));
            current_light = event.values.clone();

            if (current_light[0] > last_light[0] || current_light[0] > 500) {
                stop();
                Toast.makeText(getApplicationContext(), "Too brightly!", Toast.LENGTH_SHORT).show();
            } else {
                play();
            }

            last_light = current_light;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void play() {
        Intent i = new Intent(this, MediaService.class);
        startService(i);
    }


    public void stop() {
        Intent i = new Intent(this, MediaService.class);
        stopService(i);
    }
}