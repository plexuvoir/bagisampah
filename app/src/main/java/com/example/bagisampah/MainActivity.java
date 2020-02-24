package com.example.bagisampah;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.util.CrashUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    private FirebaseDatabase db;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private int itemID;
    private ApplicationClass applicationClass;
    private FirebaseAuth auth;
    String CHANNEL_ID = "com.bagisamah.tesnotifikasi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAndRequestPermissions();
        applicationClass = ApplicationClass.getInstance();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment()).commit();

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

//        db.getReference("DBSampah").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot sn : dataSnapshot.getChildren()){
//                    if(sn.child("statusSampah").getValue(String.class).equalsIgnoreCase("Terbooking")&& sn.child("user").getValue(String.class).equalsIgnoreCase(auth.getCurrentUser().getUid())){
//
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        db.getReference("DBSampah").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //String uid = ds.child("UID").getValue(String.class);
                for (DataSnapshot sn : dataSnapshot.getChildren()) {

                    String uid = sn.child("user").getValue(String.class);
                    if (auth.getCurrentUser().getUid().equalsIgnoreCase(uid)) {
                        String notifyBook = sn.child("notifyBook").getValue(String.class);
                        String status = sn.child("statusSampah").getValue(String.class);
                        String key = sn.getKey();
                        String namaSampah = sn.child("namaSampah").getValue(String.class);


                        if(status.equalsIgnoreCase("Terbooking") && notifyBook.equalsIgnoreCase("1")){
                            Intent intent = new Intent(MainActivity.this,MainActivity.class);
                            intent.putExtra("fragmentToLoad",R.id.nav_sampah_saya);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                    .setContentIntent(pendingIntent)
                                    .setAutoCancel(true)
                                    .setSmallIcon(R.drawable.ic_os_notification_fallback_white_24dp)
                                    .setContentTitle("Sampah Diambil")
                                    .setContentText("Sampah "+namaSampah+" telah dibooking")
                                    .setLights(Color.RED, 1000, 300)
                                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                    .setVibrate(new long[]{100, 200, 300, 400, 500})
                                    .setDefaults(Notification.DEFAULT_VIBRATE)
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                NotificationChannel channel = new NotificationChannel(
                                        CHANNEL_ID, "Sampah Diambil", NotificationManager.IMPORTANCE_DEFAULT
                                );
                                channel.setDescription("Sampah "+namaSampah+" telah dibooking");
                                channel.setShowBadge(true);
                                channel.canShowBadge();
                                channel.enableLights(true);
                                channel.setLightColor(Color.RED);
                                channel.enableVibration(true);
                                channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});
                                notificationManager.createNotificationChannel(channel);
                            }



                            notificationManager.notify(1, mBuilder.build());
                            if(notifyBook.equalsIgnoreCase("1")){
                                notificationManager.notify(1, mBuilder.build());
                                db.getReference("DBSampah").child(key).child("notifyBook")
                                        .setValue("0");
                            }
                        }else if(status.equalsIgnoreCase("Available") && notifyBook.equalsIgnoreCase("1")){
                            Intent intent = new Intent(MainActivity.this,MainActivity.class);
                            intent.putExtra("fragmentToLoad",R.id.nav_sampah_saya);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                    .setContentIntent(pendingIntent)
                                    .setAutoCancel(true)
                                    .setSmallIcon(R.drawable.ic_os_notification_fallback_white_24dp)
                                    .setContentTitle("Sampah Dibatalkan")
                                    .setContentText("Sampah "+namaSampah+" tidak jadi diambil oleh pemesan")
                                    .setLights(Color.RED, 1000, 300)
                                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                    .setVibrate(new long[]{100, 200, 300, 400, 500})
                                    .setDefaults(Notification.DEFAULT_VIBRATE)
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                NotificationChannel channel = new NotificationChannel(
                                        CHANNEL_ID, "Sampah Dibatalkan", NotificationManager.IMPORTANCE_DEFAULT
                                );
                                channel.setDescription("Sampah "+namaSampah+" tidak jadi diambil oleh pemesan");
                                channel.setShowBadge(true);
                                channel.canShowBadge();
                                channel.enableLights(true);
                                channel.setLightColor(Color.RED);
                                channel.enableVibration(true);
                                channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});
                                notificationManager.createNotificationChannel(channel);
                            }



                            notificationManager.notify(1, mBuilder.build());
                            if(notifyBook.equalsIgnoreCase("1")){
                                notificationManager.notify(1, mBuilder.build());
                                db.getReference("DBSampah").child(key).child("notifyBook")
                                        .setValue("0");
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





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

    @Override
    protected void onDestroy() {
        DataFilter.setNullAll();
        super.onDestroy();
    }


    private  boolean checkAndRequestPermissions() {
        int permissionLocC = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionLocF = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionInternet = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET);
        int permissionStorageWrite = ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionStorageRead = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionManageDoc = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.MANAGE_DOCUMENTS);
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
        if (permissionStorageWrite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionStorageRead != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionManageDoc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.MANAGE_DOCUMENTS);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }





}

