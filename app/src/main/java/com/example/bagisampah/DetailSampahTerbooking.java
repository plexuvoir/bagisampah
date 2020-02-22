package com.example.bagisampah;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
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
    private ImageView imgGmap;
    private String eimgSampah, elat, elong;
    private String enamaSampah, edeskripsiSampah, ehargaSampah, ealamatUser, ekontakUserWithoutZero, ekey, ekategoriSampah, euid;

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

        }


        String URLgmap = "https://maps.google.com/maps/api/staticmap?center=" + elat + "," + elong + "&markers=color:red%7Clabel:%7C"+ elat +","+ elong +"&zoom=15&size=600x400&sensor=false&key=AIzaSyCZacxowaOXMkI9Ryx8nSRescj8e60AL44";
        Picasso.get().load(URLgmap).into(imgGmap);
        Picasso.get().load(eimgSampah).into(imgSampah);
        namaSampah.setText(enamaSampah);
        deskripsiSampah.setText(edeskripsiSampah);
        hargaSampah.setText("Rp "+ehargaSampah);
        if (ehargaSampah.equalsIgnoreCase("0")){
            hargaSampah.setBackgroundResource(R.drawable.bg_gratis_round_detail);
            hargaSampah.setText("Gratis");
            float widthDP = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
            hargaSampah.setWidth((int)widthDP);
            hargaSampah.setGravity(Gravity.CENTER);
            hargaSampah.setTextColor(Color.parseColor("#ffffff"));
            hargaSampah.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        }
        alamatUser.setText(ealamatUser);

        btnWhatsapp.setOnClickListener(view -> {
            try {
                String text = "Hai sampah ini ready?";// Replace with your message.
                if (nomorTeleponString.charAt(0)==0){
                    ekontakUserWithoutZero = nomorTeleponString.substring(1);
                } else {
                    ekontakUserWithoutZero = nomorTeleponString;
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

        imgGmap.setOnClickListener(view -> {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr="+elat+","+elong+""));
            startActivity(intent);
        });



        btnCancel.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(DetailSampahTerbooking.this, R.style.DialogStyle);
            builder.setTitle("Batalkan Pesanan")
                    .setMessage("Apakah Anda yakin akan membatalkan pesanan ini?")
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new Handler().postDelayed(()->{
                                uploadData();
                            }, 500);
                            Snackbar.make(view, "Pesanan dibatalkan.", Snackbar.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //do nothing
                        }
                    })
                    .show();


        });

    }
    private void uploadData(){
        String statusSampahString = "Available";
        String idPengambil = "idPengambil0";
        mDatabase.child("DBSampah").child(ekey).child("statusSampah").setValue(statusSampahString);
        mDatabase.child("DBSampah").child(ekey).child("idPengambil").setValue(idPengambil).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(DetailSampahTerbooking.this,MainActivity.class);
                intent.putExtra("fragmentToLoad",R.id.nav_cari_sampah);
                startActivity(intent);
            }
        });
    }



}
