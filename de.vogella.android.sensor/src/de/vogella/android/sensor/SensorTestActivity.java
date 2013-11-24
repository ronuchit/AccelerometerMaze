package de.vogella.android.sensor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class SensorTestActivity extends Activity implements SensorEventListener {
  private SensorManager sensorManager;
  private boolean color = false;
  private View view;
  private long lastUpdate;
  public final static String EXTRA_MESSAGE = "de.vogella.android.sensor.MESSAGE";
  //Add stuff for calculating position
  private static final float NS2S = 1.0f / 1000000000.0f;
  private final float[] deltaRotationVector = new float[4];
  private float timestamp;
  private final static double EPSILON = 0.00001;
  //End of stuff added for calculating position
  File externalStorageDir = Environment.getExternalStorageDirectory();
  File myFile = new File(externalStorageDir , "test1.txt");
  
  
/** Called when the activity is first created. */

  @Override
  public void onCreate(Bundle savedInstanceState) {
  
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    if(myFile.exists()) {
        try
        {
            PrintWriter writer = new PrintWriter(myFile);
            writer.write("");
            writer.close();
         } catch(IOException e) {
            System.out.println("IO Exception");
         }
     }
     else
     {   
         try {
             myFile.createNewFile();
         } catch (IOException e) {
             System.out.println("hello this failed");
         }
     }
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sensor_test);
    view = findViewById(R.id.textView);
    view.setBackgroundColor(Color.GREEN);

    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    lastUpdate = System.currentTimeMillis();
  }

  @Override
  public void onSensorChanged(SensorEvent event) {
    if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
      getAccelerometer(event);
    }
    // This timestep's delta rotation to be multiplied by the current rotation
    // after computing it from the gyro sample data.
    if (timestamp != 0) {
        final float dT = (event.timestamp - timestamp) * NS2S;
        // Axis of the rotation sample, not normalized yet.
        float axisX = event.values[0];
        float axisY = event.values[1];
        float axisZ = event.values[2];
 
        // Calculate the angular speed of the sample
        double omegaMagnitude = Math.sqrt(axisX*axisX + axisY*axisY + axisZ*axisZ);
 
        // Normalize the rotation vector if it's big enough to get the axis
        if (omegaMagnitude > EPSILON) {
            axisX /= omegaMagnitude;
            axisY /= omegaMagnitude;
            axisZ /= omegaMagnitude;
        }
 
        // Integrate around this axis with the angular speed by the timestep
        // in order to get a delta rotation from this sample over the timestep
        // We will convert this axis-angle representation of the delta rotation
        // into a quaternion before turning it into the rotation matrix.
        double thetaOverTwo = omegaMagnitude * dT / 2.0f;
        double sinThetaOverTwo = Math.sin(thetaOverTwo);
        double cosThetaOverTwo = Math.cos(thetaOverTwo);
        deltaRotationVector[0] = (float) sinThetaOverTwo * axisX;
        deltaRotationVector[1] = (float) sinThetaOverTwo * axisY;
        deltaRotationVector[2] = (float) sinThetaOverTwo * axisZ;
        deltaRotationVector[3] = (float) cosThetaOverTwo;
    }
    timestamp = event.timestamp;
    float[] deltaRotationMatrix = new float[9];
    SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
    // User code should concatenate the delta rotation we computed with the current rotation
    // in order to get the updated rotation.
    // rotationCurrent = rotationCurrent * deltaRotationMatrix;

  }

  private void getAccelerometer(SensorEvent event) {
    float[] values = event.values;
    // Movement
    float x = values[0];
    float y = values[1];
    float z = values[2];
    System.out.println(x + " " + y + " " + z);
    if(myFile.exists()) {
       try
       {
           PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(myFile, true)));
           writer.println(x + " " + y + " " + z);
           writer.close();
        } catch(IOException e) {
           System.out.println("IO Exception");
        }
    }
    else
    {   
        try {
            myFile.createNewFile();
        } catch (IOException e) {
            System.out.println("hello this failed");
        }
    }

    long actualTime = System.currentTimeMillis();
    if (Math.abs(x) > 0.5 || Math.abs(y) > 0.5 || Math.abs(z) > 0.5) //
    {
      if (actualTime - lastUpdate < 200) {
        return;
      }
      lastUpdate = actualTime;
      Toast.makeText(this, "Device was shuffed", Toast.LENGTH_SHORT)
          .show();
      if (y > 0.5 ) {
        view.setBackgroundColor(Color.GREEN);

      } else {
        view.setBackgroundColor(Color.RED);
      }
      color = !color;
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {

  }

  @Override
  protected void onResume() {
    super.onResume();
    // register this class as a listener for the orientation and
    // accelerometer sensors
    sensorManager.registerListener(this,
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
        SensorManager.SENSOR_DELAY_NORMAL);
  }

  @Override
  protected void onPause() {
    // unregister listener
    super.onPause();
    sensorManager.unregisterListener(this);
  }
} 