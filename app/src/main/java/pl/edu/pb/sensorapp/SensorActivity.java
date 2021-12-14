package pl.edu.pb.sensorapp;

import static pl.edu.pb.sensorapp.R.color.*;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class SensorActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    private boolean sensorVisible;
    private static final String KEY_IS_VISIBLE = "sensorVisible";
    SensorAdapter adapter;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_VISIBLE, sensorVisible);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sensor_activity);
        if (savedInstanceState != null){
            sensorVisible = savedInstanceState.getBoolean(KEY_IS_VISIBLE);
        }
        RecyclerView recyclerView = findViewById(R.id.sensor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        if (adapter == null){
            adapter = new SensorAdapter(sensorList);
            recyclerView.setAdapter(adapter);
        }
        else{
            adapter.notifyDataSetChanged();
        }


    }


    private class SensorAdapter extends RecyclerView.Adapter<SensorHolder> {
        private final List<Sensor> sensorList;
        public SensorAdapter(List<Sensor> sensorList){
            this.sensorList = sensorList;
        }

        @NonNull
        @Override
        public SensorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new SensorHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SensorHolder holder, int position) {
            Sensor sensor = sensorList.get(position);
            holder.bind(sensor);
        }

        @Override
        public int getItemCount() {
            return sensorList.size();
        }
    }

    private class SensorHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView SensorNameTextView;
        private final ImageView SensorIconImageView;
        private final LinearLayout SensorView;
        Sensor sensor;

        public SensorHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.sensor_list_item, parent, false));
            itemView.setOnClickListener(this);

            SensorNameTextView = itemView.findViewById(R.id.sensor_name);
            SensorIconImageView = itemView.findViewById(R.id.sensor_icon);
            SensorView = itemView.findViewById(R.id.sensor_view);

        }


        @SuppressLint("ResourceAsColor")
        public void bind(Sensor sensor) {
            this.sensor = sensor;

            SensorNameTextView.setText(sensor.getName());
            SensorIconImageView.setImageResource(R.drawable.ic_sensor_foreground);

            if(SensorNameTextView.getText() == "Goldfish Ambient Temperature sensor" || SensorNameTextView.getText() == "Goldfish Light sensor")
               SensorView.setBackgroundColor(colorPrimaryLight);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), SensorDetailsActivity.class);
            intent.putExtra(SensorDetailsActivity.KEY_EXTRA_SENSOR, sensorList.indexOf(sensor));
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sensor_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.sensor_counter);

        if (sensorVisible) {
            menuItem.setTitle(R.string.hide_sensor_counter);
        } else {
            menuItem.setTitle(R.string.show_sensor_counter);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sensor_counter) {
            sensorVisible = !sensorVisible;
            this.invalidateOptionsMenu();
            updateSubtitle();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateSubtitle() {
        String subtitle = getString(R.string.sensors_count, sensorList.size());

        if (!sensorVisible) {
            subtitle = null;
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity) this;
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);

    }
}
