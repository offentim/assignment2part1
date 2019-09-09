package com.example.assignment2part1;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
//import android.support.v7.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "MainActivity";
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor sensors;
    private SeekBar sBar;
    private TextView tView;

    private MediaPlayer mediaPlayerWalk;
    private MediaPlayer mediaPlayerRun;
    private MediaPlayer mediaPlayerStand;

    private LineChart mChart;
    private LineChart mChart2;
    private Thread thread;
    private boolean plotData = true;
    int vale = 2;

    float magnitude;



    Deque<Double> real = new ArrayDeque<Double>();

    int windowSize = 64;

    FFT fft = new FFT(windowSize);

    float plotFFT;

    LineData data2;

    ILineDataSet set4;

    int sbarprog;
    int n;


    private double maxWalk;
    private int iiWalk;
    //private int standing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sBar =  findViewById(R.id.seekBar1);
        tView = findViewById(R.id.textview1);
        //tView.setText(sBar.getProgress() + "/" + sBar.getMax());

        //sBar.setProgress(4);
        sBar.setMax(7);
        System.out.println(maxWalk);

        mediaPlayerWalk = MediaPlayer.create(this, R.raw.sound2);
        mediaPlayerRun = MediaPlayer.create(this,R.raw.sound1);





        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int v;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                v = progress;


                sbarprog = sBar.getProgress()+1;

                fft.setWindowSize((int)Math.pow(2,sbarprog));

                tView.setText(""+Integer.toString(fft.getWindowSize()));

            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //write custom code to on start progress
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        //for(int i=0; i<sensors.size(); i++){
        // Log.d(TAG, "onCreate: Sensor "+ i + ": " + sensors.get(i).toString());
        // }

        if (mAccelerometer != null) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        }
        mChart2 = (LineChart) findViewById(R.id.chart2);
        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.getDescription().setEnabled(true);
        mChart2.getDescription().setEnabled(true);


        // enable touch gestures
        mChart.setTouchEnabled(true);
        mChart2.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart2.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart2.setScaleEnabled(true);
        mChart.setDrawGridBackground(true);
        mChart2.setDrawGridBackground(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);
        mChart2.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.LTGRAY);
        mChart2.setBackgroundColor(Color.LTGRAY);

        LineData data1 = new LineData();
        data1.setValueTextColor(Color.BLUE);
        mChart2.setData(data1);

        data2 = mChart2.getData();
        set4 = data2.getDataSetByIndex(0);

        LineData data = new LineData();
        data.setValueTextColor(Color.BLUE);
        mChart.setData(data);

        Legend l = mChart.getLegend();
        Legend l2 = mChart2.getLegend();

        l.setForm(Legend.LegendForm.LINE);
        l2.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);
        l2.setTextColor(Color.WHITE);


        XAxis xl2 = mChart2.getXAxis();
        xl2.setTextColor(Color.BLACK);
        xl2.setDrawGridLines(true);
        xl2.setAvoidFirstLastClipping(true);
        xl2.setEnabled(true);

        YAxis leftAxis2 = mChart2.getAxisLeft();
        leftAxis2.setTextColor(Color.BLACK);
        leftAxis2.setDrawGridLines(true);
        //leftAxis2.setAxisMaximum(30f);
        //leftAxis2.setAxisMinimum(-10f);
        leftAxis2.setDrawGridLines(true);

        //YAxis rightAxis2 = mChart2.getAxisRight();
        //rightAxis2.setEnabled(false);

        mChart2.getAxisLeft().setDrawGridLines(false);
        mChart2.getXAxis().setDrawGridLines(false);
        mChart2.setDrawBorders(false);

        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(true);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setDrawGridLines(true);
        //leftAxis.setAxisMaximum(30f);
        //leftAxis.setAxisMinimum(-10f);
        leftAxis.setDrawGridLines(true);

        //YAxis rightAxis = mChart.getAxisRight();
        //rightAxis.setEnabled(false);

        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.setDrawBorders(false);

        feedMultiple();

    }





    private void addEntry(SensorEvent event) {

        LineData data = mChart.getData();


        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);
            ILineDataSet set1 = data.getDataSetByIndex(1);
            ILineDataSet set2 = data.getDataSetByIndex(2);
            ILineDataSet set3 = data.getDataSetByIndex(3);


            if (set == null) {
                set = createSet("X Value",Color.RED);
                data.addDataSet(set);

            }

            if(set1 == null){
                set1 = createSet("Y Value",Color.BLUE);
                data.addDataSet(set1);
            }

            if(set2 == null){
                set2 = createSet("Z Value",Color.GREEN);
                data.addDataSet(set2);
            }

            if(set3 == null){
                set3 = createSet("Magnitude",Color.BLACK);
                data.addDataSet(set3);
            }


            data.addEntry(new Entry(set.getEntryCount(),  event.values[0]), 0);
            data.addEntry(new Entry(set1.getEntryCount(), event.values[1]), 1);
            data.addEntry(new Entry(set2.getEntryCount(), event.values[2]), 2);
            data.addEntry(new Entry(set3.getEntryCount(), magnitude), 3);


            data.notifyDataChanged();


            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            // limit the number of visible entries

            mChart.setVisibleXRangeMaximum(150);

            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            mChart.moveViewToX(data.getEntryCount());

        }
    }

    private LineDataSet createSet(String name, int color) {

        LineDataSet set = new LineDataSet(null, name);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(1f);
        set.setColor(color);
        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        return set;
    }



    private void feedMultiple() {

        if (thread != null){
            thread.interrupt();
        }

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true){
                    plotData = true;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (thread != null) {
            thread.interrupt();
        }
        mSensorManager.unregisterListener(this);

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

       double m = Math.sqrt((Math.pow(x,2))+(Math.pow(y,2))+(Math.pow(z,2)));
       float mM = (float)m;

       magnitude = mM;


       real.add(m);

       double max = 0;
       int ii = 0;




       while (real.size()> fft.getWindowSize()){
           real.remove();
       }


       if(real.size() == fft.getWindowSize()){
           Object[] realD = real.toArray();
           double[] chat = new double[fft.getWindowSize()];
           for (int i = 0 ; i < fft.getWindowSize() ; i++){
               chat[i] = (Double)realD[i];

           }

           //set4=createSet("FFT",Color.BLUE);
           //data2.addDataSet(set4);



           //System.out.println(chat);
           double[] imagine = new double[fft.getWindowSize()];
           fft.fft(chat,imagine);





           float powerChat[] = new float[fft.getWindowSize()/2];
           List<Entry> fftSetData = new ArrayList<>();

           for(int i = 1; i < fft.getWindowSize()/2; i++){
               powerChat[i] = (float)Math.sqrt(chat[i]*chat[i]+imagine[i]*imagine[i]);
               //System.out.println(i);
               data2.addEntry(new Entry(i, powerChat[i]), 0);
               fftSetData.add(new Entry(i, powerChat[i]));

           }

           for(int i=0; i<powerChat.length;i++){
               if(powerChat[i]>max){
                   max = powerChat[i];
                   ii = i;

               }
               //System.out.println(max);
               //System.out.println(Integer.toString(ii));

           }
           if (max > 30 && max < 150) {
               mediaPlayerWalk.start();
           }else if(max < 30  && mediaPlayerWalk.isPlaying()){
               mediaPlayerWalk.pause();
           }
           else if(max >150){
               mediaPlayerWalk.pause();
               mediaPlayerRun.start();
           }
           else if((max<100 && mediaPlayerRun.isPlaying())){
               mediaPlayerRun.pause();
           }



           LineDataSet set5 = new LineDataSet(fftSetData,"ftt");
           data2 = new LineData(set5);
           set5.setHighlightEnabled(false);
           set5.setDrawValues(false);
           set5.setDrawCircles(false);

           mChart2.setData(data2);
           data2.notifyDataChanged();
           mChart2.notifyDataSetChanged();
           mChart2.setVisibleXRangeMaximum(150);
           mChart2.invalidate();
           //mChart2.moveViewToX(data2.getEntryCount());
           //System.out.println(Arrays.toString(chat));
       }

        if(plotData){
            addEntry(event);
            plotData = false;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onDestroy() {
        mSensorManager.unregisterListener(MainActivity.this);
        thread.interrupt();
        super.onDestroy();
    }
}