package com.example.assignment2part1;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private Handler mHandler = new Handler();
    private LineGraphSeries<DataPoint> series;
    private LineGraphSeries<DataPoint> series1;
    private LineGraphSeries<DataPoint> series2;
    private LineGraphSeries<DataPoint> series3;

    private static final String TAG = "MainActivity";

    private SensorManager sensorManager;
    private Sensor accelerometer;
    double xValues;
    double yValues;
    double zValues;

    double counter = 0;

    double magnitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<>(new DataPoint[] {});
        series1 = new LineGraphSeries<>(new DataPoint[] {});
        series2 = new LineGraphSeries<>(new DataPoint[] {});
        series3 = new LineGraphSeries<>(new DataPoint[] {});


        graph.addSeries(series);
        //graph.addSeries(series1);
        //graph.addSeries(series2);
        //graph.addSeries(series3);
        //series.setColor(R.color.colorPrimary);
        series1.setColor(R.color.colorAccent);
        //series1.setColor(R.color.colorPrimaryDark);
        //series3.setColor(R.color.Black);


        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(1000);
        graph.getViewport().setXAxisBoundsManual(true);

        addRandomDataPoint();


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        }





    private void addRandomDataPoint(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                series.appendData(new DataPoint(counter,xValues),false,1000);
                addRandomDataPoint();

                //series1.appendData(new DataPoint(counter,yValues),false,1000);
                //addRandomDataPoint();

                //series2.appendData(new DataPoint(counter,zValues),false,1000);
               // addRandomDataPoint();

               // series3.appendData(new DataPoint(counter,magnitude),false,10000);
               // addRandomDataPoint();

            }
        },0);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            double x = sensorEvent.values[0];
            double y = sensorEvent.values[1];
            double z = sensorEvent.values[2];

            double m = Math.sqrt((Math.pow(x,2))+(Math.pow(y,2))+(Math.pow(z,2)));

           magnitude = m;

            counter++;
            xValues = x;
            yValues = y;
            zValues = z;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    protected void onResume() {
        super.onResume();
        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
    }
}