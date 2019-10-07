package edu.ggc.lutz.diceroller_solution;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class ColorChooser extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private RadioButton rbStandard, rbCustom;
    private RadioGroup rgColor;
    private Spinner spinner;
    private TextView tvRed, tvGreen, tvBlue;
    private SeekBar sbRed, sbGreen, sbBlue;
    private int r, g, b;
    private TextView tvDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_chooser);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rbStandard = findViewById(R.id.rbStandard);
        rbCustom = findViewById(R.id.rbCustom);
        rgColor = findViewById(R.id.rgColor);
        tvDisplay = findViewById(R.id.tvDisplay);

        spinner = findViewById(R.id.spinner);

        tvRed = findViewById(R.id.tvRed);
        tvGreen = findViewById(R.id.tvGreen);
        tvBlue = findViewById(R.id.tvBlue);

        sbRed = findViewById(R.id.sbRed);
        sbGreen = findViewById(R.id.sbGreen);
        sbBlue = findViewById(R.id.sbBlue);


        rgColor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = rgColor.getCheckedRadioButtonId();
                switch (id){
                    case R.id.rbStandard:
                        Log.i("CHECK","DOES THIS WORK");
                        spinner.setVisibility(View.VISIBLE);
                        tvRed.setVisibility(View.GONE);
                        sbRed.setVisibility(View.GONE);
                        tvGreen.setVisibility(View.GONE);
                        sbGreen.setVisibility(View.GONE);
                        tvBlue.setVisibility(View.GONE);
                        sbBlue.setVisibility(View.GONE);
                        break;

                    case R.id.rbCustom:
                        Log.v("CHECK", "PLEASE WORK TOO");
                        spinner.setVisibility(View.GONE);
                        tvRed.setVisibility(View.VISIBLE);
                        sbRed.setVisibility(View.VISIBLE);
                        tvGreen.setVisibility(View.VISIBLE);
                        sbGreen.setVisibility(View.VISIBLE);
                        tvBlue.setVisibility(View.VISIBLE);
                        sbBlue.setVisibility(View.VISIBLE);
                        break;
                }

            }
        }
        );

        //Deals with the standard chooses given in the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                    switch (i){
                        case 0:
                            //Red
                            r = 255; g = 0; b = 0;
                            tvDisplay.setBackgroundColor(Color.rgb(r, g, b));
                            break;

                        case 1:
                            //Green
                             r = 0; g = 255; b = 0;
                            tvDisplay.setBackgroundColor(Color.rgb(r, g, b));
                            break;

                        case 2:
                            //Blue
                             r = 0; g = 0; b = 255;
                            tvDisplay.setBackgroundColor(Color.rgb(r, g, b));
                            break;

                        case 3:
                            //Cyan
                            r = 0; g = 255; b = 255;
                            tvDisplay.setBackgroundColor(Color.rgb(r, g, b));
                            break;

                        case 4:
                            //Magenta
                            r = 255; g = 0; b = 255;
                            tvDisplay.setBackgroundColor(Color.rgb(r, g, b));
                            break;

                        case 5:
                            //Yellow
                            r = 255; g = 255; b = 0;
                            tvDisplay.setBackgroundColor(Color.rgb(r, g, b));
                            break;
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Deals with the Seekbar and what the user chooses
//        sbRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                r = sbRed.getProgress();
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//
//        sbBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean bool) {
//                b = sbBlue.getProgress();
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//
//        sbGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                g = sbGreen.getProgress();
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });



        sbRed.setOnSeekBarChangeListener(this);
        sbGreen.setOnSeekBarChangeListener(this);
        sbBlue.setOnSeekBarChangeListener(this);
    }


    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
        //get current ARGB values
        r = sbRed.getProgress();
        g = sbGreen.getProgress();
        b = sbBlue.getProgress();

                int id = seekBar.getId();

        if(id == R.id.sbRed)
            r = progress;
        else if(id == R.id.sbGreen)
            g = progress;
        else if(id == R.id.sbBlue)
            b = progress;

        tvDisplay.setBackgroundColor(Color.rgb(r,g,b));
    }
    public void onStartTrackingTouch(SeekBar seekBar) {
        //Only required due to implements
    }
    public void onStopTrackingTouch(SeekBar seekBar) {
        //Only required due to implements
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull  MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home:
             Intent intent = getIntent();
             int col = Color.rgb(r, g, b);
             Log.v("CHECK","COLOR: " + r + g + b);
             intent.putExtra("color", col);
             setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
