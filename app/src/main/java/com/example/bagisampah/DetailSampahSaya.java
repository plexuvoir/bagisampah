package com.example.bagisampah;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetailSampahSaya extends AppCompatActivity {

    private ImageView imgSampah;
    private TextView namaSampah, deskripsiSampah, hargaSampah, namaUser, kontakUser, alamatUser;
    private Button btnEdit;
    private FirebaseDatabase db;
    private String eimgSampah, ekey, euid, namaUserString, nomorTeleponString;
    private String enamaSampah, edeskripsiSampah, ehargaSampah, ealamatUser, ekategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sampah_saya);

        db = FirebaseDatabase.getInstance();


        getSupportActionBar().setTitle("Detail Sampah");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);



        imgSampah = findViewById(R.id.img_sampah);
        namaSampah = findViewById(R.id.txt_nama_sampah);
        deskripsiSampah = findViewById(R.id.txt_deskripsi_sampah);
        hargaSampah = findViewById(R.id.txt_harga);
        namaUser = findViewById(R.id.txt_nama_user);
        kontakUser = findViewById(R.id.txt_kontak_user);
        alamatUser = findViewById(R.id.txt_alamat_user);
        btnEdit = findViewById(R.id.btn_edit);

        btnEdit.setOnClickListener(view -> {
            Intent intent = new Intent(DetailSampahSaya.this,EditSampah.class);
            intent.putExtra("imgSampah",eimgSampah);
            intent.putExtra("namaSampah",enamaSampah);
            intent.putExtra("deskripsiSampah",edeskripsiSampah);
            intent.putExtra("hargaSampah",ehargaSampah);
            intent.putExtra("alamatUser",ealamatUser);
            intent.putExtra("kategoriSampah",ekategori);
            intent.putExtra("key", ekey);
            startActivity(intent);
        });



        Bundle extras = getIntent().getExtras();

        if (extras != null){
            eimgSampah = extras.getString("imgSampah");
            enamaSampah = extras.getString("namaSampah");
            edeskripsiSampah = extras.getString("deskripsiSampah");
            ehargaSampah = extras.getString("hargaSampah");
            ealamatUser = extras.getString("alamatUser");
            ekategori = extras.getString("kategoriSampah");
            ekey = extras.getString("key");
            euid = extras.getString("uid");
            Log.d("euid", "onCreate: "+euid);

            db.getReference("Users").child(euid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String mNamaUser = dataSnapshot.child("nama").getValue(String.class);
                    namaUserString=mNamaUser;
                    String nomorTelepon = dataSnapshot.child("nomorHP").getValue(String.class);
                    nomorTeleponString= nomorTelepon;
                    namaUser.setText(namaUserString);
                    kontakUser.setText(nomorTeleponString);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }




        Picasso.get().load(eimgSampah).into(imgSampah);
        namaSampah.setText(enamaSampah);
        deskripsiSampah.setText(edeskripsiSampah);
        hargaSampah.setText("Rp."+ehargaSampah);
        alamatUser.setText(ealamatUser);



    }
}
