package com.example.mizuno.prog_garden;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GardenTraceActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button button01;
    private TextView edittext01;
    private AsyncTaskGetJson getjson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gardentrace);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent i = getIntent();
        String id = i.getStringExtra("id");

        button01 = (Button) findViewById(R.id.button);
        edittext01 = (TextView)findViewById(R.id.editText1);
        getjson = new AsyncTaskGetJson(this,id);
        edittext01.setText(id);
        getjson.execute();

        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(GardenTraceActivity.this, GardenActivity.class);
                startActivity(intent1);

            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng latlng = new LatLng(34.738249,137.9592173);
        mMap.addMarker(new MarkerOptions().position(latlng).title("静岡理工科大学"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
    }


    public void mapLocation(double geo[]){

        mMap.clear();
        for(int i = 0; i < geo.length; i+=2) {
            LatLng latlng = new LatLng(geo[i], geo[i+1]);
            mMap.addMarker(new MarkerOptions().position(latlng));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 18));
        }

    }
}
