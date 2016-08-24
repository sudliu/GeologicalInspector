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

    private static final String TAG ="GeologicalInspector" ;
    public SensorManager mSM;
    public Sensor mAcc;
    public Sensor mMag;
    public TextView tv_accx;
    public TextView tv_accy;
    public TextView tv_accz;
    public TextView tv_magx;
    public TextView tv_magy;
    public TextView tv_magz;
    public TextView tv_magxb;
    public TextView tv_magyb;
    public TextView tv_magzb;
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
    double vnx;
    double vny;
    double vnz;
    double strike;
    double dip;

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
        tv_magxb = (TextView) findViewById(R.id.textView_v_magxb);
        tv_magyb = (TextView) findViewById(R.id.textView_v_magyb);
        tv_magzb = (TextView) findViewById(R.id.textView_v_magzb);
        tv_vnx = (TextView) findViewById(R.id.textView_v_vnx);
        tv_vny = (TextView) findViewById(R.id.textView_v_vny);
        tv_vnz = (TextView) findViewById(R.id.textView_v_vnz);
        tv_angf = (TextView) findViewById(R.id.textView_v_angf);
        tv_angi = (TextView) findViewById(R.id.textView_v_angi);
        tv_angp = (TextView) findViewById(R.id.textView_v_angp);
        tv_strike = (TextView) findViewById(R.id.textView_v_strike);
        tv_dip = (TextView) findViewById(R.id.textView_v_dip);
    }

    void updateData(){
        calcAngles();
        calcVector();
        calcStrike();
        calcDip();

        tv_accx.setText(String.valueOf(accx));
        tv_accy.setText(String.valueOf(accy));
        tv_accz.setText(String.valueOf(accz));
        tv_magx.setText(String.valueOf(magx));
        tv_magy.setText(String.valueOf(magy));
        tv_magz.setText(String.valueOf(magz));
        tv_angf.setText(String.valueOf((angf)));
        tv_angi.setText(String.valueOf((angi)));
        tv_angp.setText(String.valueOf((angp)));
        /*
        tv_angf.setText(String.valueOf(Math.toDegrees(angf)));
        tv_angi.setText(String.valueOf(Math.toDegrees(angi)));
        tv_angp.setText(String.valueOf(Math.toDegrees(angp)));
        */
        tv_vnx.setText(String.valueOf(vnx));
        tv_vny.setText(String.valueOf(vny));
        tv_vnz.setText(String.valueOf(vnz));
        tv_strike.setText(String.valueOf(strike));
        tv_dip.setText(String.valueOf(dip));
        tv_magxb.setText(String.valueOf(magxb));
        tv_magyb.setText(String.valueOf(magyb));
        tv_magzb.setText(String.valueOf(magzb));
    }

    void calcAngles(){
        angf = Math.atan((double)(accy/accz));
        angi = Math.atan(accx/Math.sqrt(accy*accy + accz*accz)) * -1;
        angp = Math.atan(((magx-magxb)*Math.cos(angi) +(magy-magyb)*Math.sin(angi)*Math.sin(angf) + (magz-magzb)*Math.sin(angi)*Math.cos(angf))/((magy-magyb)*Math.cos(angf) - (magz - magzb)*Math.sin(angf)));



        Log.d(TAG, "Angles updated: phi:"+angf+" theta:"+angi+" psi:"+angp);
    }

    void calcVector(){
        vnx = (Math.sin(angf)*Math.sin(angp))+(Math.cos(angf)*Math.cos(angp)*Math.sin(angi));
        vny = (Math.cos(angf)*Math.sin(angi)*Math.sin(angp)) - (Math.cos(angp)*Math.sin(angi));
        vnz = Math.cos(angf)*Math.cos(angi);

        Log.d(TAG, "Vn updated: X:"+vnx+" Y:"+vny+" Z:"+vnz);
    }

    void calcStrike(){
        if(vnz > 0){
            if(vny > 0) {
                if (vnx > 0) {
                    strike = Math.atan(vnx/vny);
                } else {
                    strike = Math.PI * 2 + Math.atan(vnx/vny);
                }
            } else{
                strike = Math.PI + Math.atan(vnx/vny);
            }
        }else {
            if(vny < 0) {
                if (vnx > 0) {
                    strike = Math.PI * 2 + Math.atan(vnx/vny);
                } else {
                    strike = Math.atan(vnx/vny);
                }
            } else{
                strike = Math.PI + Math.atan(vnx/vny);
            }
        }
        Log.d(TAG, "Strike:" + strike);

    }

    void calcDip(){
        if(vnz > 0){
            dip = Math.atan(Math.sqrt(vnx*vnx+vny*vny)/Math.vnz);
        } else{
            dip = Math.atan(Math.sqrt(vnx*vnx+vny*vny)/Math.vnz)*-1;
        }

        Log.d(TAG, "Dip: "+dip);
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
            accy = event.values[1];
            accz = event.values[2];

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
