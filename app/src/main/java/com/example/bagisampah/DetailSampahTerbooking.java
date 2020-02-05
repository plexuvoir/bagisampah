package com.example.bagisampah;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class DetailSampahTerbooking extends AppCompatActivity {

    private ImageView imgSampah;
    private TextView namaSampah, deskripsiSampah, hargaSampah, namaUser, kontakUser, alamatUser;
    private Button btnWhatsapp, btnCancel;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private String namaUserString, nomorTeleponString;
    private FirebaseDatabase db;
    private String eimgSampah;
    private String enamaSampah, edeskripsiSampah, ehargaSampah, enamaUser, ekontakUser, ealamatUser, ekontakUserWithoutZero, ekey, ekategoriSampah, euid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sampah_terbooking);
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
        btnCancel = findViewById(R.id.btn_cancel);

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

        btnCancel.setOnClickListener(view -> {
            uploadData();
        });

    }
    private void uploadData(){
        String statusSampahString = "Available";
        String idPengambil = "idPengambil0";
        String namaPengambil = "namaPengambil0";
        String nomorPengambil = "nomorPengambil0";
        mDatabase.child("DBSampah").child(ekey).child("statusSampah").setValue(statusSampahString);
        mDatabase.child("DBSampah").child(ekey).child("idPengambil").setValue(idPengambil);
        mDatabase.child("DBSampah").child(ekey).child("namaPengambil").setValue(namaPengambil);
        mDatabase.child("DBSampah").child(ekey).child("nomorPengambil").setValue(nomorPengambil).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(DetailSampahTerbooking.this,MainActivity.class);
                intent.putExtra("fragmentToLoad",R.id.nav_cari_sampah);
                startActivity(intent);
            }
        });
    }

}
