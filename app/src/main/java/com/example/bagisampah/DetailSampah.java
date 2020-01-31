package com.example.bagisampah;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class DetailSampah extends AppCompatActivity {

    private ImageView imgSampah;
    private TextView namaSampah, deskripsiSampah, hargaSampah, namaUser, kontakUser, alamatUser;
    private Button btnWhatsapp, btnAmbil;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private String namaUserString, nomorTeleponString;
    private FirebaseDatabase db;
    private String eimgSampah;
    private String enamaSampah, edeskripsiSampah, ehargaSampah, enamaUser, ekontakUser, ealamatUser, ekontakUserWithoutZero, ekey, ekategoriSampah, euid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sampah);


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
        btnAmbil = findViewById(R.id.btn_ambil);

        db = FirebaseDatabase.getInstance();


        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();



        Bundle extras = getIntent().getExtras();

        if (extras != null){
            Log.d("jalan", "onCreate: ");
            eimgSampah = extras.getString("imgSampah");
            Log.d("img", "onCreate: "+eimgSampah);
            enamaSampah = extras.getString("namaSampah");
            Log.d("nama", "onCreate: "+enamaSampah);
            edeskripsiSampah = extras.getString("deskripsiSampah");
            ehargaSampah = extras.getString("hargaSampah");
            enamaUser = extras.getString("namaUser");
            ekontakUser = extras.getString("kontakUser");
            ealamatUser = extras.getString("alamatUser");
            ekategoriSampah = extras.getString("kategoriSampah");
            ekey = extras.getString("key");
            Log.d("ekey", "onCreate: "+ekey);
            euid = extras.getString("uid");
            Log.d("euid", "onCreate: "+euid);
        }

        Picasso.get().load(eimgSampah).into(imgSampah);
        namaSampah.setText(enamaSampah);
        deskripsiSampah.setText(edeskripsiSampah);
        hargaSampah.setText("Rp."+ehargaSampah);
        namaUser.setText(enamaUser);
        kontakUser.setText(ekontakUser);
        alamatUser.setText(ealamatUser);

        btnWhatsapp.setOnClickListener(view -> {
            try {
                String text = "Hai sampah ini ready?";// Replace with your message.
                if (ekontakUser.charAt(0)==0){
                    ekontakUserWithoutZero = ekontakUser.substring(1);
                } else {
                    ekontakUserWithoutZero = ekontakUser;
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

        db.getReference("Users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String namaUser = dataSnapshot.child("nama").getValue(String.class);
                namaUserString=namaUser;
                String nomorTelepon = dataSnapshot.child("nomorHP").getValue(String.class);
                nomorTeleponString= nomorTelepon;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnAmbil.setOnClickListener(view -> {
            uploadData();
        });

    }

    private void uploadData(){

        String imgString = eimgSampah;
        String namaSampahString = enamaSampah;
        String deskripsiSampahString = deskripsiSampah.getText().toString();
        String kategoriSampahString = ekategoriSampah;
        String latlocSampahString = "Latloc Sampah 0";
        String longlocSampahString = "Longloc Sampah 0";
        String hargaSampahString = ehargaSampah;
        String statusSampahString = "Terbooking";
        String jarakSampahString = "Jarak Sampah 0";
        String userString = euid;
        String alamatSampahString = ealamatUser;
        String idPengambil = auth.getCurrentUser().getUid();
        String namaPengambil = namaUserString;
        String nomorPengambil = nomorTeleponString;
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
        dataMap.put("namaUser", enamaUser);
        dataMap.put("nomorTelepon", ekontakUser);
        dataMap.put("idPengambil", idPengambil);
        dataMap.put("namaPengambil", namaPengambil);
        dataMap.put("nomorPengambil", nomorPengambil);
        mDatabase.child("DBSampah").child(ekey).setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(DetailSampah.this,MainActivity.class);
                intent.putExtra("fragmentToLoad",R.id.nav_terbooking);
                startActivity(intent);
                //startActivity(new Intent(EditSampah.this, DetailSampahSaya.class));

            }
        });

    }

}
