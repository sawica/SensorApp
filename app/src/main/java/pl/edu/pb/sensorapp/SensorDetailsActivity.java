package pl.edu.pb.sensorapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

public class SensorDetailsActivity extends AppCompatActivity implements SensorEventListener {
    public static final String KEY_EXTRA_SENSOR = "sensorId";
    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView sensorTextView;
    private TextView sensorLabelTextView;
    private TextView sensorDetailsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_details);
        int id = Integer.parseInt(String.valueOf(getIntent().getExtras().get(KEY_EXTRA_SENSOR)));

        sensorTextView = findViewById(R.id.sensor_name_label);
        sensorLabelTextView = findViewById(R.id.sensor_label);
        sensorDetailsTextView = findViewById(R.id.sensor_details_label);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getSensorList(Sensor.TYPE_ALL).get(id);

        if (sensor == null){
            sensorTextView.setText(R.string.missing_sensor);
        }
        else {
            sensorTextView.setText(sensor.getName());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(sensor != null){
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        float currentValue = sensorEvent.values[0];

        sensorDetailsTextView.setText(String.valueOf(currentValue));

        switch (sensorType){
            case Sensor.TYPE_LIGHT:
                sensorLabelTextView.setText(R.string.light_sensor_label);
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                sensorLabelTextView.setText(R.string.temperature_sensor_label);
                break;
            default:
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}