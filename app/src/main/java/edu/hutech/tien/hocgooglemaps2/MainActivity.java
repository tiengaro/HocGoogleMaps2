package edu.hutech.tien.hocgooglemaps2;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private GoogleMap mMap;

    Spinner spnTypeMap;
    ArrayList<String> arrTypeMaps;
    ArrayAdapter<String> adapterTypeMap;

    ProgressDialog progressDialog;

    //13.093040, 109.296604

    private GoogleMap.OnMyLocationChangeListener listener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng local = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(local));
            if (mMap != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(local, 16));
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
    }

    private void addEvents() {
        spnTypeMap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case 1:
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case 2:
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                    case 3:
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    case 4:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void addControls() {
        spnTypeMap = (Spinner) findViewById(R.id.spnTypeMap);
        arrTypeMaps = new ArrayList<>();
        arrTypeMaps.addAll(Arrays.asList(getResources().getStringArray(R.array.arrTypeMaps)));
        adapterTypeMap = new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.simple_spinner_item,
                arrTypeMaps
        );
        adapterTypeMap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTypeMap.setAdapter(adapterTypeMap);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Thông báo");
        progressDialog.setMessage("Đang tải bản đồ. Vui lòng đợi...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                progressDialog.dismiss();
//                LatLng myHome = new LatLng(13.093040, 109.296604);
//                mMap.addMarker(new MarkerOptions().position(myHome)
//                        .title("My Home")
//                        .snippet("Đây là nhà của NhockCon"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myHome, 18));

                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);

                mMap.setOnMyLocationChangeListener(listener);
            }
        });


    }
}
