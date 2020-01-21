package com.example.bagisampah;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailSampah extends AppCompatActivity {

    private ImageView imgSampah;
    private TextView namaSampah, deskripsiSampah, hargaSampah, namaUser, kontakUser, alamatUser;
    private Button btnWhatsapp;

    private String eimgSampah;
    private String enamaSampah, edeskripsiSampah, ehargaSampah, enamaUser, ekontakUser, ealamatUser, ekontakUserWithoutZero;

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



        Bundle extras = getIntent().getExtras();

        if (extras != null){
            eimgSampah = extras.getString("imgSampah");
            enamaSampah = extras.getString("namaSampah");
            edeskripsiSampah = extras.getString("deskripsiSampah");
            ehargaSampah = extras.getString("hargaSampah");
            enamaUser = extras.getString("namaUser");
            ekontakUser = extras.getString("kontakUser");
            ealamatUser = extras.getString("alamatUser");
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


    }

}
