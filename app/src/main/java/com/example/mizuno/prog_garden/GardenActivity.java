package com.example.mizuno.prog_garden;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GardenActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private Button button01, button02, button03;
    private EditText edittext01;
    private AsyncHttp asynchttp;
    private int stopgps = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garden);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        button01 = (Button) findViewById(R.id.button);
        button02 = (Button) findViewById(R.id.button2);
        button03 = (Button) findViewById(R.id.button3);
        edittext01 = (EditText)findViewById(R.id.editText1);

        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopgps = 1;
                Toast toast = Toast.makeText(getApplicationContext(), "開始しました。", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        button02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //asynchttp.cancel(true);
                stopgps = 2;
                Toast toast = Toast.makeText(getApplicationContext(), "終了します。", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        button03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //asynchttp.cancel(true);
                Intent intent1 = new Intent(GardenActivity.this, GardenTraceActivity.class);
                //intent1.putExtra("id", edittext01.getText());
                String id = String.valueOf(edittext01.getText());
                intent1.putExtra("id", id);
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

    public void mapLocation(double latitude, double longitude){

        mMap.clear();
        LatLng latlng = new LatLng(latitude,longitude);
        mMap.addMarker(new MarkerOptions().position(latlng));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 18));
    }

    @Override
    public void onLocationChanged(Location location) {

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        double elevation = location.getAltitude();

        TextView tv_lat = (TextView)findViewById(R.id.latitude);
        tv_lat.setText("Latitude: " + latitude);

        //経度の表示
        TextView tv_lng = (TextView)findViewById(R.id.longitude);
        tv_lng.setText("Longitude:" + longitude);

        TextView tv_elv = (TextView)findViewById(R.id.elevation);
        tv_elv.setText("Elevation:" + elevation);

        this.mapLocation(latitude, longitude);

        if(stopgps == 1) {
            int id = Integer.parseInt(String.valueOf(edittext01.getText()));
            asynchttp = new AsyncHttp(id);
            asynchttp.execute(latitude, longitude, elevation);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    protected void onPause() {

        if(locationManager != null){
            //位置情報の更新不要の場合は終了
            locationManager.removeUpdates(this);
        }

        super.onPause();
    }

    @Override
    protected void onResume() {

        if(locationManager != null){
            //位置情報の更新を取得
            locationManager.requestLocationUpdates(
                    //permissionにACCESS_FINE_LOCATIONを追加
                    LocationManager.GPS_PROVIDER,
                    //NETWORK_PROVIDERを利用する場合はpermissionにACCESS_COARSE_LOCATIONを追加
                    //LocationManager.NETWORK_PROVIDER
                    //networkから取得する場合こちらに切り替える
                    3000,// 通知のための最小時間間隔（ミリ秒）この場合は10秒に１回
                    0,// 通知のための最小距離間隔（メートル）
                    this
            );
        }

        super.onResume();
    }


}
