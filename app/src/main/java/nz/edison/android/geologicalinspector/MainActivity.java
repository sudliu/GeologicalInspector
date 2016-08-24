package nz.edison.android.geologicalinspector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    public SensorManager mSM;
    public Sensor mAcc;
    public Sensor mMag;
    public TextView tv_accx;
    public TextView tv_accy;
    public TextView tv_accz;
    public TextView tv_magx;
    public TextView tv_magy;
    public TextView tv_magz;
    public TextView tv_vnx;
    public TextView tv_vny;
    public TextView tv_vnz;
    public TextView tv_angf;
    public TextView tv_angi;
    public TextView tv_angp;
    public TextView tv_strike;
    public TextView tv_dip;

    float accx;
    float accy;
    float accz;
    float magx;
    float magy;
    float magz;
    float magxb;
    float magyb;
    float magzb;

    double angf;
    double angi;
    double angp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSM = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAcc = mSM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMag = mSM.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED);

        mSM.registerListener(this, mAcc, SensorManager.SENSOR_DELAY_NORMAL);
        mSM.registerListener(this, mMag, SensorManager.SENSOR_DELAY_NORMAL);

        tv_accx = (TextView) findViewById(R.id.textView_v_accx);
        tv_accy = (TextView) findViewById(R.id.textView_v_accy);
        tv_accz = (TextView) findViewById(R.id.textView_v_accz);
        tv_magx = (TextView) findViewById(R.id.textView_v_magx);
        tv_magy = (TextView) findViewById(R.id.textView_v_magy);
        tv_magz = (TextView) findViewById(R.id.textView_v_magz);
        tv_vnx = (TextView) findViewById(R.id.textView_v_vnx);
        tv_vny = (TextView) findViewById(R.id.textView_v_vny);
        tv_vnz = (TextView) findViewById(R.id.textView_v_vnz);
        tv_angf = (TextView) findViewById(R.id.textView_v_angf);
        tv_angi = (TextView) findViewById(R.id.textView_v_angi);
        tv_angp = (TextView) findViewById(R.id.textView_v_angp);
        tv_strike = (TextView) findViewById(R.id.textView_v_strike);
        tv_dip= (TextView) findViewById(R.id.textView_v_dip);
    }

    void updateData(){

    }

    void calcAngles(){
        angf = Math.atan((double)(accy/accz));
        angi = Math.atan(accx/Math.sqrt((accy*accy) + (accz*accz))) * -1;
        angp = Math.atan(((magx-magxb)*Math.cos(angi) +(magy-magyb)*Math.sin(angi)*Math.sin(angf) + (magz-magzb)*Math.sin(angi)*Math.cos(angi))/())
    }

    void calcVector(){

    }

    void calcStrike(){

    }

    void calcDip(){

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event){
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accx = event.values[0];
            accy = event.values[0];
            accz = event.values[0];

            Log.d(TAG, "Acc updated: x:"+accx+" Y:"+accy+" Z:"+accz);
            updateData();
        }
        else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED) {
            magx = event.values[0];
            magy = event.values[1];
            magz = event.values[2];
            magxb = event.values[3];
            magyb = event.values[4];
            magzb = event.values[5];

            Log.d(TAG, "Mag updated: x:"+magx+" Y:"+magy+" Z:"+magz+" BiasX:"+magxb+" BiasY:"+magzb);

            updateData();
        } else
            Log.d(TAG, "Unknown sensor type");

    }
}
