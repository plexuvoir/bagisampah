package com.example.bagisampah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EditSampah extends AppCompatActivity {
    private GoogleMap mMap;
    private EditText namaSampah, deskripsiSampah, kategoriSampah, alamatSampah, hargaSampah;
    private Button btn_simpan;
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference mDatabase;
    private String namaUserString, nomorTeleponString;
    private Spinner spinnerKategori;
    private static String imgLink = "belum masuk";
    private ImageView imgMaps;
    private String  eAddress, ekey;
    private double eLatitude, eLongitude;

    FirebaseStorage imgStorage;
    StorageReference imgStorageReference;

    private ImageView imgUploadSampah;
    private Uri filepath;
    private final int PICK_IMAGE_REQUEST = 71;

    private String enamaSampah, edeskripsiSampah, ehargaSampah, ealamatUser, ekategori, eimgSampah;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sampah);
        namaSampah = findViewById(R.id.namaSampah);
        deskripsiSampah = findViewById(R.id.deskripsiSampah);
        spinnerKategori = findViewById(R.id.spinnerKategori);
        alamatSampah = findViewById(R.id.alamatSampah);
        hargaSampah = findViewById(R.id.hargaSampah);
        btn_simpan = findViewById(R.id.btn_simpan);
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
            ekey = extras.getString("key");
            Log.d("ekeyy", ekey);
            imgLink = eimgSampah;

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

        //image click
        imgUploadSampah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgUploadSampah.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                },1000);

                chooseImage();

            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();


//                Intent intent = new Intent(getContext(), MainActivity.class);
//                startActivity(intent);
//                getActivity().finish();

            }
        });

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                imgUploadSampah.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    private void uploadImage(){
        if(filepath !=null){
            final ProgressDialog progressDialog = new ProgressDialog(this);


            final StorageReference ref = imgStorageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            imgLink = uri.toString();
                            uploadData();
                        }

                    });
                }
            });

        } else {
            uploadData();
        }

    }

    private void uploadData(){
        String imgString = imgLink;
        String namaSampahString = namaSampah.getText().toString();
        String deskripsiSampahString = deskripsiSampah.getText().toString();
        String kategoriSampahString = spinnerKategori.getSelectedItem().toString();
        String latlocSampahString = "Latloc Sampah 0";
        String longlocSampahString = "Longloc Sampah 0";
        String hargaSampahString = hargaSampah.getText().toString();
        String statusSampahString = "Available";
        String jarakSampahString = "Jarak Sampah 0";
        String idPengambil = "idPengambil0";
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
        dataMap.put("idPengambil", idPengambil);
        System.out.println(ekey);
        mDatabase.child("DBSampah").child(ekey).setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(EditSampah.this,MainActivity.class);
                intent.putExtra("fragmentToLoad",R.id.nav_sampah_saya);
                startActivity(intent);
                //startActivity(new Intent(EditSampah.this, DetailSampahSaya.class));

            }
        });

    }



}
