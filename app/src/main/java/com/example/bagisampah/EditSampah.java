package com.example.bagisampah;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditSampah extends AppCompatActivity {
    private GoogleMap mMap;
    private EditText namaSampah, deskripsiSampah, kategoriSampah, alamatSampah, hargaSampah;
    private Button btn_post;
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference mDatabase;
    private String namaUserString, nomorTeleponString;
    private Spinner spinnerKategori;
    private static String imgLink = "belum masuk";
    private ImageView imgMaps;
    private String  eAddress;
    private double eLatitude, eLongitude;

    FirebaseStorage imgStorage;
    StorageReference imgStorageReference;

    private ImageView imgUploadSampah;
    private Uri filepath;
    private final int PICK_IMAGE_REQUEST = 71;

    private String enamaSampah, edeskripsiSampah, ehargaSampah, enamaUser, ekontakUser, ealamatUser, ekategori, eimgSampah;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sampah);
        namaSampah = findViewById(R.id.namaSampah);
        deskripsiSampah = findViewById(R.id.deskripsiSampah);
        spinnerKategori = findViewById(R.id.spinnerKategori);
        alamatSampah = findViewById(R.id.alamatSampah);
        hargaSampah = findViewById(R.id.hargaSampah);
        btn_post = findViewById(R.id.btn_post);
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        imgUploadSampah = findViewById(R.id.imgUploadSampah);
        imgMaps = findViewById(R.id.imgMaps);
        imgStorage = FirebaseStorage.getInstance();
        imgStorageReference = imgStorage.getReference();



        Bundle extras = getIntent().getExtras();

        if (extras != null){
            eimgSampah = extras.getString("imgSampah");
            enamaSampah = extras.getString("namaSampah");
            edeskripsiSampah = extras.getString("deskripsiSampah");
            ehargaSampah = extras.getString("hargaSampah");
            ealamatUser = extras.getString("alamatUser");
            ekategori = extras.getString("kategoriSampah");

            namaSampah.setText(enamaSampah);
            deskripsiSampah.setText(edeskripsiSampah);
            hargaSampah.setText(ehargaSampah);
            alamatSampah.setText(ealamatUser);
            Picasso.get().load(eimgSampah).into(imgUploadSampah);
//            switch (ekategori){
//                case "Plastik"
//                    :kategoriSampah.setSelection(0);
//                case "Kertas"
//                    :kategoriSampah.setSelection(1);
//                case "Tekstil"
//                    :kategoriSampah.setSelection(2);
//                case "Kaleng"
//                    :kategoriSampah.setSelection(3);
//                case "Kaca"
//                    :kategoriSampah.setSelection(4);
//                default:kategoriSampah.setSelection(0);
//            }
        }


    }



}
