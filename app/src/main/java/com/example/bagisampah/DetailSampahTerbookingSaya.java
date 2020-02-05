package com.example.bagisampah;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class DetailSampahTerbookingSaya extends AppCompatActivity {
    private ImageView imgSampah;
    private TextView namaSampah, deskripsiSampah, hargaSampah, namaUser, kontakUser, alamatUser, namaPengambil;
    private Button btnWhatsapp;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private String namaUserString, nomorTeleponString;
    private FirebaseDatabase db;
    private String eimgSampah, enamaPengambil, enomorPengambil;
    private String enamaSampah, edeskripsiSampah, ehargaSampah, enamaUser, ekontakUser, ealamatUser, ekontakUserWithoutZero, ekey, ekategoriSampah, euid;

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

        db = FirebaseDatabase.getInstance();


        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        Bundle extras = getIntent().getExtras();

        if (extras != null){
            eimgSampah = extras.getString("imgSampah");
            enamaSampah = extras.getString("namaSampah");
            edeskripsiSampah = extras.getString("deskripsiSampah");
            ehargaSampah = extras.getString("hargaSampah");
            enamaUser = extras.getString("namaUser");
            ekontakUser = extras.getString("kontakUser");
            ealamatUser = extras.getString("alamatUser");
            ekategoriSampah = extras.getString("kategoriSampah");
            ekey = extras.getString("key");
            euid = extras.getString("uid");
            enamaPengambil = extras.getString("namaPengambil");
            enomorPengambil = extras.getString("nomorPengambil");
        }

        Picasso.get().load(eimgSampah).into(imgSampah);
        namaSampah.setText(enamaSampah);
        deskripsiSampah.setText(edeskripsiSampah);
        hargaSampah.setText("Rp."+ehargaSampah);
        namaUser.setText(enamaUser);
        kontakUser.setText(ekontakUser);
        alamatUser.setText(ealamatUser);
        namaPengambil.setText(enamaPengambil);

        btnWhatsapp.setOnClickListener(view -> {
            try {
                String text = "Hai, Apakah sampah jadi diambil?";// Replace with your message.
                if (enomorPengambil.charAt(0)==0){
                    ekontakUserWithoutZero = enomorPengambil.substring(1);
                } else {
                    ekontakUserWithoutZero = enomorPengambil;
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
