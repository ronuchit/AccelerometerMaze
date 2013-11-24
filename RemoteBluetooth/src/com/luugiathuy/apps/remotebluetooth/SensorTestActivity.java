package com.luugiathuy.apps.remotebluetooth;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SensorTestActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager1;
    private SensorManager sensorManager2;
    private View view;
    public final static String EXTRA_MESSAGE = "de.vogella.android.sensor.MESSAGE";
    private static final float NS2S = 1.0f / 1000000000.0f;
    private static final int UP = 0;
    private static final int DOWN = 1;
    private static final int LEFT = 2;
    private static final int RIGHT = 3;
    private static final int GYRO_SAMPLES = 5;
    private boolean vel_first = true, acc_first = true;
    private long prevTime;
    private double prev_vel_x, prev_vel_y, prev_vel_z;
    private double curr_acc_x, curr_acc_y, curr_acc_z;
    private double pos_x = 0.0, pos_y = 0.0, pos_z = 0.0;
    private double[][] positions = new double[10][2];
    private int positions_count = 0;
    private double motion_threshold = 0.25;
    private double zero_threshold = 0.12;
    private boolean can_move = false;
    private int pos_count = 0;
    
  /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
      requestWindowFeature(Window.FEATURE_NO_TITLE);
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
          WindowManager.LayoutParams.FLAG_FULLSCREEN);
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_sensor_test);

      sensorManager1 = (SensorManager) getSystemService(SENSOR_SERVICE);
      sensorManager2 = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
      double filter_constant;
      if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
          float[] vel = event.values;
          if (!vel_first && !acc_first) {
            // implement complementary filter (low and high pass) and numerical integration to eliminate drift and generate accurate readings
              filter_constant = 0.701;
              // pos_x = filter_constant * (pos_x + (prevTime - System.currentTimeMillis()) / 1000.0 * (prev_vel_x + vel[0]) / 1.0) + (1 - filter_constant) * curr_acc_x;
              pos_x = filter_constant * (pos_x + NS2S * (prev_vel_x + vel[0]) / 2.0) + (1 - filter_constant) * curr_acc_x;
            // pos_y = filter_constant * (pos_y + (prevTime - System.currentTimeMillis()) / 1000.0 * (prev_vel_y + vel[1]) / 1.0) + (1 - filter_constant) * curr_acc_y;
              pos_y = filter_constant * (pos_y + NS2S * (prev_vel_y + vel[1]) / 2.0) + (1 - filter_constant) * curr_acc_y;
            // pos_z = filter_constant * (pos_z + (prevTime - System.currentTimeMillis()) / 1000.0 * (prev_vel_z + vel[2]) / 1.0) + (1 - filter_constant) * curr_acc_z;
            pos_z = filter_constant * (pos_z + NS2S * vel[2]) + (1 - filter_constant) * curr_acc_z;
            double[] pos_arr = {pos_x, pos_y};
            positions[positions_count] = pos_arr;
            positions_count++;
            if (positions_count == GYRO_SAMPLES) {
              determine_rotation(positions);
              positions_count = 0;
            }
          }
          prev_vel_x = vel[0];
          prev_vel_y = vel[1];
          prev_vel_z = vel[2];
          prevTime = System.currentTimeMillis();
          vel_first = false;
      } else if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
          float[] acc = event.values;
          curr_acc_x = acc[0];
          curr_acc_y = acc[1];
          curr_acc_z = acc[2];
          acc_first = false;
      }
    }
    
    public void determine_rotation(double[][] positions) {
        double[] curr_avg = {0.0, 0.0};
        for (double[] arr : positions) {
          curr_avg[0] += arr[0];
          curr_avg[1] += arr[1];
        }
        curr_avg[0] /= positions.length;
        curr_avg[1] /= positions.length;
        System.out.println("curr avg: " + curr_avg[0] + "   " + curr_avg[1] + " ... can move: " + can_move);

        if ((Math.abs(curr_avg[0]) < zero_threshold) && (Math.abs(curr_avg[1]) < zero_threshold)) {
          if (pos_count < 3) {
            pos_count++;
          } else {
            can_move = true;
          }
        } else {
          if (curr_avg[0] > motion_threshold) {
            if (!(curr_avg[1] > motion_threshold && curr_avg[1] > curr_avg[0]) && !(curr_avg[1] < -motion_threshold && -curr_avg[1] > curr_avg[0])) {
                if (can_move) {
                  String s = "" + pos_x + pos_y + pos_z;
                  System.out.println(s);
                  RemoteBluetooth.getCommand().write(0); //Up
                  return;
                }
              }        
          }
          if (curr_avg[0] < -motion_threshold) {
            if (!(curr_avg[1] < -motion_threshold && curr_avg[1] < curr_avg[0]) && !(curr_avg[1] > motion_threshold && -curr_avg[1] < curr_avg[0])) {
                if (can_move) {
                    String s = "" + pos_x + pos_y + pos_z;
                    System.out.println(s);
                    RemoteBluetooth.getCommand().write(1);  //Down
                    return;
                }
              }
          }
          if (curr_avg[1] > motion_threshold) {
            if (can_move) {
                String s = "" + pos_x + pos_y + pos_z;
                System.out.println(s);
                RemoteBluetooth.getCommand().write(2); //Left
                return;
            }
          }
          if (curr_avg[1] < -motion_threshold) {
            System.out.println("here");
            if (can_move) {
                String s = "" + pos_x + pos_y + pos_z;
                System.out.println(s);
                RemoteBluetooth.getCommand().write(3); //Right
              return;
            }
          }
        }
      }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        System.out.println("Hopefully this never gets printed...");
    }

    @Override
    protected void onResume() {
      super.onResume();
      // register this class as a listener for the orientation and
      // accelerometer sensors
      sensorManager1.registerListener(this,
      sensorManager1.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
          SensorManager.SENSOR_DELAY_NORMAL);
      sensorManager2.registerListener(this,
      sensorManager2.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
          SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
      // unregister listener
      super.onPause();
      sensorManager1.unregisterListener(this);
      sensorManager2.unregisterListener(this);
    }
  } 
