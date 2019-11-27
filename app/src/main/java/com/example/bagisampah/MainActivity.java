package com.example.bagisampah;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.util.CrashUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public List<List_Sampah> list_sampahs = new ArrayList<List_Sampah>();
    public RecyclerView recycler_sampah;
    public RecyclerView.Adapter adapter_sampah;
    private FirebaseDatabase db, db2;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseAuth auth;
    HashMap<String,String> sampah = new HashMap<>();
    private SampahAdapter adapter;
    private Button addSampah;
    private DatabaseReference mDatabase;
    String namaUserString, nomorTeleponString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycler_sampah = findViewById(R.id.recycler_sampah);
        recycler_sampah.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recycler_sampah.setLayoutManager(linearLayoutManager);
        db = FirebaseDatabase.getInstance();
        db2 = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        addSampah = findViewById(R.id.addSampah);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        db.getReference("DBSampah").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_sampahs.clear();
                for (DataSnapshot sn : dataSnapshot.getChildren()){
                    String img = sn.child("img").getValue(String.class);
                    String nama = sn.child("namaSampah").getValue(String.class);
                    String deskripsi = sn.child("deskripsiSampah").getValue(String.class);
                    String kategori = sn.child("kategoriSampah").getValue(String.class);
                    String latloc = sn.child("latlocSampah").getValue(String.class);
                    String longloc = sn.child("longlocSampah").getValue(String.class);
                    String harga = sn.child("hargaSampah").getValue(String.class);
                    String status = sn.child("statusSampah").getValue(String.class);
                    String jarak = sn.child("jarakSampah").getValue(String.class);
                    String alamat = sn.child("alamatSampah").getValue(String.class);
                    String uid = sn.child("user").getValue(String.class);
                    String namaUser = sn.child("namaUser").getValue(String.class);
                    String nomorTelepon = sn.child("nomorTelepon").getValue(String.class);
                    list_sampahs.add(new List_Sampah(img, nama, deskripsi, kategori, latloc, longloc, harga, status, jarak, alamat, uid, namaUser, nomorTelepon));
                }
                adapter = new SampahAdapter(MainActivity.this, list_sampahs);
                recycler_sampah.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        db2.getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dn : dataSnapshot.getChildren()){
                    String namaUser = dn.child("nama").getValue(String.class);
                    namaUserString=namaUser;
                    System.out.println(namaUserString);
                    String nomorTelepon = dn.child("nomorHP").getValue(String.class);
                    nomorTeleponString= nomorTelepon;
                    System.out.println(nomorTeleponString);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        addSampah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imgString = "https://www.finroll.com/wp-content/uploads/2019/10/sampah-plastik1.jpg";
                String namaSampahString = "Nama Sampah 0";
                String deskripsiSampahString = "Deskripsi Sampah 0";
                String kategoriSampahString = "Kategori Sampah 0";
                String latlocSampahString = "Latloc Sampah 0";
                String longlocSampahString = "Longloc Sampah 0";
                String hargaSampahString = "Harga Sampah 0";
                String statusSampahString = "Status Sampah 0";
                String jarakSampahString = "Jarak Sampah 0";
                String userString = auth.getCurrentUser().getUid();
                Log.d("user", userString);
                String alamatSampahString = "Alamat Sampah 0";
                HashMap<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put("img", imgString);
                dataMap.put("namaSampah", namaSampahString);
                dataMap.put("deskripsiSampah", deskripsiSampahString);
                dataMap.put("kategoriSampah", kategoriSampahString);
                dataMap.put("latlocSampah", latlocSampahString);
                dataMap.put("longlocSampah", longlocSampahString);
                dataMap.put("hargaSampah", hargaSampahString);
                dataMap.put("statusSampah", statusSampahString);
                dataMap.put("jarakSampah", jarakSampahString);
                dataMap.put("user", userString);
                dataMap.put("alamatSampah", alamatSampahString);
                dataMap.put("namaUser", namaUserString);
                dataMap.put("nomorTelepon", nomorTeleponString);
                mDatabase.child("DBSampah").push().setValue(dataMap);
            }
        });
    }
}
