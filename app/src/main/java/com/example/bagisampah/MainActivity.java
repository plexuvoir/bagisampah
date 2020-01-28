package com.example.bagisampah;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.util.CrashUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private int itemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAndRequestPermissions();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment()).commit();

        Bundle extras = getIntent().getExtras();

        if (extras != null){
           itemID = extras.getInt("fragmentToLoad");
            Fragment selectedFragment = null;
            MenuItem menuItem;
            boolean isChecked;
            switch (itemID){
                case R.id.nav_cari_sampah:
                    selectedFragment = new SearchFragment();
                    menuItem = bottomNav.getMenu().getItem(0);
                    isChecked = menuItem.getItemId() == itemID;
                    menuItem.setChecked(isChecked);
                    break;
                case R.id.nav_terbooking:
                    selectedFragment = new TerbookingFragment();
                    menuItem = bottomNav.getMenu().getItem(1);
                    isChecked = menuItem.getItemId() == itemID;
                    menuItem.setChecked(isChecked);
                    break;
                case R.id.nav_bagi_sampah:
                    selectedFragment = new BagiSampahFragment();
                    menuItem = bottomNav.getMenu().getItem(2);
                    isChecked = menuItem.getItemId() == itemID;
                    menuItem.setChecked(isChecked);
                    break;
                case R.id.nav_sampah_saya:
                    selectedFragment = new SampahSayaFragment();
                    menuItem = bottomNav.getMenu().getItem(3);
                    isChecked = menuItem.getItemId() == itemID;
                    menuItem.setChecked(isChecked);
                    break;
                case R.id.nav_profil:
                    selectedFragment = new ProfilFragment();
                    menuItem = bottomNav.getMenu().getItem(4);
                    isChecked = menuItem.getItemId() == itemID;
                    menuItem.setChecked(isChecked);
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            extras=null;
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()){
                case R.id.nav_cari_sampah:
                    selectedFragment = new SearchFragment();
                    //Intent intent = getIntent();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    overridePendingTransition(0, 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                    break;
                case R.id.nav_terbooking:
                    selectedFragment = new TerbookingFragment();
                    break;
                case R.id.nav_bagi_sampah:
                    selectedFragment = new BagiSampahFragment();
                    break;
                case R.id.nav_sampah_saya:
                    selectedFragment = new SampahSayaFragment();
                    break;
                case R.id.nav_profil:
                    selectedFragment = new ProfilFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };

    private  boolean checkAndRequestPermissions() {
        int permissionLocC = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionLocF = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionInternet = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET);
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
