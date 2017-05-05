package com.example.angel.googlemapdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LocationActivity extends AppCompatActivity {

    private TextView txt1;
    private TextView txt2;
    private TextView txt3;
    String locationName;
    Double longitude,latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        txt1= (TextView) findViewById(R.id.txt1);
        txt2= (TextView) findViewById(R.id.txt2);
        txt3= (TextView) findViewById(R.id.txt3);

        Bundle bundle = getIntent().getExtras();
        locationName= bundle.getString("locationsearch");
        latitude=bundle.getDouble("latitude");
        longitude=bundle.getDouble("longitude");

        txt1.setText("LOCATION NAME : "+locationName);
        txt2.setText("LATITUDE : "+latitude);
        txt3.setText("LONGITUDE : "+longitude);

    }
}
