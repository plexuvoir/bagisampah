package com.example.bagisampah;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Filter extends AppCompatActivity {

    private SeekBar seekBarDistance, seekBarPrice;
    private TextView maxDistance, maxPrice;
    private int hargaMax;
    private String hargaMaxString;
    private String distanceMaxString;
    private Button btnTerapkan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        seekBarDistance = findViewById(R.id.seek_bar_distance);
        maxDistance = findViewById(R.id.max_distance);
        seekBarPrice = findViewById(R.id.seek_bar_price);
        maxPrice =findViewById(R.id.max_price);
        btnTerapkan = findViewById(R.id.btn_terapkan);
        Bundle extras = getIntent().getExtras();

        if (extras != null){
            hargaMax = extras.getInt("hargaMax");
            hargaMaxString = String.valueOf(hargaMax);
        }

        seekBarPrice.setMax(Integer.parseInt(hargaMaxString.substring(0, hargaMaxString.length()-3))+1);
        seekBarPrice.setProgress(Integer.parseInt(hargaMaxString.substring(0, hargaMaxString.length()-3))+1);
        maxPrice.setText("Rp "+(Integer.parseInt(hargaMaxString.substring(0, hargaMaxString.length()-3))+1)+"000");

        seekBarDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                maxDistance.setText((i+1)+" KM");
                distanceMaxString = String.valueOf((i+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                Toast.makeText(Filter.this, distanceMaxString, Toast.LENGTH_SHORT).show();
            }
        });
        seekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                maxPrice.setText("Rp "+(i+1)+"000");
                hargaMaxString = i+1+"000";
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(Filter.this, hargaMaxString, Toast.LENGTH_SHORT).show();
            }
        });

        btnTerapkan.setOnClickListener(view -> {
            Intent intent = new Intent(Filter.this, MainActivity.class);
            intent.putExtra("hargaMax", hargaMaxString);
            intent.putExtra("distanceMax", distanceMaxString);
            startActivity(intent);
        });
    }
}
