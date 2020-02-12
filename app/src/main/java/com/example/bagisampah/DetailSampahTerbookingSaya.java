package com.example.bagisampah;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class DetailSampahTerbookingSaya extends AppCompatActivity {
    private ImageView imgSampah;
    private TextView namaSampah, deskripsiSampah, hargaSampah, namaUser, kontakUser, alamatUser, namaPengambil;
    private Button btnWhatsapp;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private String namaUserString, nomorTeleponString, namaPengambilString, nomorPengambilString;
    private FirebaseDatabase db;
    private ImageView imgGmap;
    private String eimgSampah, elat, elong;
    private String enamaSampah, edeskripsiSampah, ehargaSampah, ealamatUser, ekontakUserWithoutZero, ekey, ekategoriSampah, euid, eidPengambil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sampah_terbooking_saya);

        getSupportActionBar().setTitle("Detail Sampah");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        imgSampah = findViewById(R.id.img_sampah);
        namaSampah = findViewById(R.id.txt_nama_sampah);
        deskripsiSampah = findViewById(R.id.txt_deskripsi_sampah);
        hargaSampah = findViewById(R.id.txt_harga);
        namaUser = findViewById(R.id.txt_nama_user);
        kontakUser = findViewById(R.id.txt_kontak_user);
        alamatUser = findViewById(R.id.txt_alamat_user);
        btnWhatsapp = findViewById(R.id.btn_Whatsapp);
        namaPengambil = findViewById(R.id.txt_nama_pengambil);
        imgGmap = findViewById(R.id.view_gmap);

        db = FirebaseDatabase.getInstance();


        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        Bundle extras = getIntent().getExtras();

        if (extras != null){
            eimgSampah = extras.getString("imgSampah");
            enamaSampah = extras.getString("namaSampah");
            edeskripsiSampah = extras.getString("deskripsiSampah");
            ehargaSampah = extras.getString("hargaSampah");
            ealamatUser = extras.getString("alamatUser");
            ekategoriSampah = extras.getString("kategoriSampah");
            ekey = extras.getString("key");
            euid = extras.getString("uid");
            eidPengambil = extras.getString("idPengambil");
            elat = extras.getString("latLoc");
            Log.d("elat", "onCreate: "+elat);
            elong = extras.getString("longLoc");

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

            db.getReference("Users").child(eidPengambil).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String namaUser = dataSnapshot.child("nama").getValue(String.class);
                    namaPengambilString=namaUser;
                    String nomorTelepon = dataSnapshot.child("nomorHP").getValue(String.class);
                    nomorPengambilString= nomorTelepon;
                    namaPengambil.setText(namaPengambilString);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        String URLgmap = "http://maps.google.com/maps/api/staticmap?center=" + elat + "," + elong + "&markers=color:red%7Clabel:%7C"+ elat +","+ elong +"&zoom=15&size=600x400&sensor=false&key=AIzaSyCZacxowaOXMkI9Ryx8nSRescj8e60AL44";
        Picasso.get().load(URLgmap).into(imgGmap);
        Picasso.get().load(eimgSampah).into(imgSampah);
        namaSampah.setText(enamaSampah);
        deskripsiSampah.setText(edeskripsiSampah);
        hargaSampah.setText("Rp."+ehargaSampah);
        alamatUser.setText(ealamatUser);




        imgGmap.setOnClickListener(view -> {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr="+elat+","+elong+""));
            startActivity(intent);
        });


        btnWhatsapp.setOnClickListener(view -> {
            try {
                String text = "Hai, Apakah sampah jadi diambil?";// Replace with your message.
                if (nomorPengambilString.charAt(0)==0){
                    ekontakUserWithoutZero = nomorPengambilString.substring(1);
                } else {
                    ekontakUserWithoutZero = nomorPengambilString;
                }
                String toNumber = "62"+ekontakUserWithoutZero; // Replace with mobile phone number without +Sign or leading zeros, but with country cod
                //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.


                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
                startActivity(intent);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });



    }
}
