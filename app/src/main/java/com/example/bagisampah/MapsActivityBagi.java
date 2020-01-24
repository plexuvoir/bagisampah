package com.example.bagisampah;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import com.google.maps.android.ui.IconGenerator;

public class MapsActivityBagi extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button btnSetLokasi;
    BottomSheetBehavior bottomSheetBehavior;
    double curLat, curLong, latSelected, longSelected;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    String address, city, state, country, postalCode, knownName;
    TextView txtLokasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_bagi);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        checkAndRequestPermissions();
        View bottomSheetInfo = findViewById(R.id.bottom_sheet_info);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetInfo);
        txtLokasi = findViewById(R.id.txt_lokasi);
        btnSetLokasi = findViewById(R.id.btn_set_lokasi);

        btnSetLokasi.setOnClickListener(view -> {
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putLong("lat", Double.doubleToRawLongBits(latSelected));
//            editor.putLong("long", Double.doubleToRawLongBits(longSelected));
//            editor.putString("address",address );
//            editor.commit();
            onBackPressed();

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

        if (checkAndRequestPermissions()) {
            GPSTracker gps = new GPSTracker(MapsActivityBagi.this);
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            curLat=latitude;
            curLong=longitude;
        }
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(curLat,curLong),15.0f));

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                //markerOptions.title(latLng.latitude + " : " + latLng.longitude);


                try {
                    addAddress(latLng.latitude, latLng.longitude);
                } catch(IOException e){
                    e.printStackTrace();
                }

                latSelected = latLng.latitude;
                longSelected = latLng.longitude;



                // Clears the previously touched position
                googleMap.clear();

                // Animating to the touched position
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                googleMap.addMarker(markerOptions);

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                txtLokasi.setText(address);

            }
        });
    }
    private void addAddress(double latitude, double longitude)  throws IOException  {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        System.out.println(address);
        city = addresses.get(0).getLocality();
        state = addresses.get(0).getAdminArea();
        country = addresses.get(0).getCountryName();
        postalCode = addresses.get(0).getPostalCode();
        knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
    }

    private  boolean checkAndRequestPermissions() {
        int permissionLocC = ContextCompat.checkSelfPermission(MapsActivityBagi.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionLocF = ContextCompat.checkSelfPermission(MapsActivityBagi.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionInternet = ContextCompat.checkSelfPermission(MapsActivityBagi.this, Manifest.permission.INTERNET);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocC != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (permissionLocF != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionInternet != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
}
