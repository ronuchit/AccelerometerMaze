package de.vogella.android.sensor;

import android.app.Activity;
import android.graphics.Color;
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
  //Add stuff for calculating position
  private static final float NS2S = 1.0f / 1000000000.0f;
  private boolean vel_first = true, acc_first = true;
  private long prevTime;
  private double prev_vel_x, prev_vel_y, prev_vel_z;
  private double curr_acc_x, curr_acc_y, curr_acc_z;
  private double pos_x = 0.0, pos_y = 0.0, pos_z = 0.0;
  
  
/** Called when the activity is first created. */

  @Override
  public void onCreate(Bundle savedInstanceState) {
  
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sensor_test);
    view = findViewById(R.id.textView);
    view.setBackgroundColor(Color.GREEN);

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
          System.out.println("Pos: " + pos_x + "   " + pos_y + "   " + pos_z);
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