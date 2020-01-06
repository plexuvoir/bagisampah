package com.example.bagisampah;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PostSampah extends AppCompatActivity {
    private EditText namaSampah, deskripsiSampah, kategoriSampah, alamatSampah, hargaSampah;
    private Button btn_post;
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference mDatabase;
    private String namaUserString, nomorTeleponString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_sampah);
        namaSampah = findViewById(R.id.namaSampah);
        deskripsiSampah = findViewById(R.id.deskripsiSampah);
        kategoriSampah = findViewById(R.id.kategoriSampah);
        alamatSampah = findViewById(R.id.alamatSampah);
        hargaSampah = findViewById(R.id.hargaSampah);
        btn_post = findViewById(R.id.btn_post);
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        db.getReference("Users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String namaUser = dataSnapshot.child("nama").getValue(String.class);
                            namaUserString=namaUser;
                            System.out.println(namaUserString);
                            String nomorTelepon = dataSnapshot.child("nomorHP").getValue(String.class);
                            nomorTeleponString= nomorTelepon;
                            System.out.println(nomorTeleponString);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imgString = "https://www.finroll.com/wp-content/uploads/2019/10/sampah-plastik1.jpg";
                String namaSampahString = namaSampah.getText().toString();
                Log.d("nss", namaSampahString);
                String deskripsiSampahString = deskripsiSampah.getText().toString();
                String kategoriSampahString = kategoriSampah.getText().toString();
                String latlocSampahString = "Latloc Sampah 0";
                String longlocSampahString = "Longloc Sampah 0";
                String hargaSampahString = hargaSampah.getText().toString();
                String statusSampahString = "Available";
                String jarakSampahString = "Jarak Sampah 0";
                String userString = auth.getCurrentUser().getUid();
                Log.d("user", userString);
                String alamatSampahString = alamatSampah.getText().toString();
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
                Log.d("nts", nomorTeleponString);
                mDatabase.child("DBSampah").push().setValue(dataMap);
                Log.d("testcuy", "onClick: test");
                Intent intent = new Intent(PostSampah.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
