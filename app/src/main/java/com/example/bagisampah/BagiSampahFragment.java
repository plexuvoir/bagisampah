package com.example.bagisampah;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;

public class BagiSampahFragment extends Fragment {
    private GoogleMap mMap;
    private EditText namaSampah, deskripsiSampah, kategoriSampah, alamatSampah, hargaSampah;
    private Button btn_post;
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference mDatabase;
    private String namaUserString, nomorTeleponString,hargaSampahString="null";
    private Spinner spinnerKategori;
    private static String imgLink = "belum masuk";
    private ImageView imgMaps;
    private String  eAddress;
    private double eLatitude, eLongitude;
    private String nama="",deskripsi="",alamat="",harga="";
    private CheckBox checkBoxGratis;
    private TextView textRP;
    private String hargaTemp="";

    private ImageView imgUploadSampah;
    private Uri filepath, filepathGlobal;
    private final int PICK_IMAGE_REQUEST = 71;
    public static final int KITKAT_VALUE = 1002;

    //Firebase storage
    FirebaseStorage imgStorage;
    StorageReference imgStorageReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View inflate =inflater.inflate(R.layout.fragment_bagi_sampah,null);
        namaSampah = inflate.findViewById(R.id.namaSampah);
        deskripsiSampah = inflate.findViewById(R.id.deskripsiSampah);
        spinnerKategori = inflate.findViewById(R.id.spinnerKategori);
        alamatSampah = inflate.findViewById(R.id.alamatSampah);
        hargaSampah = inflate.findViewById(R.id.hargaSampah);
        btn_post = inflate.findViewById(R.id.btn_post);
        checkBoxGratis = inflate.findViewById(R.id.checkbox_gratis);
        textRP = inflate.findViewById(R.id.text_rp);
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        imgUploadSampah = inflate.findViewById(R.id.imgUploadSampah);
        imgMaps = inflate.findViewById(R.id.imgMaps);
        imgStorage = FirebaseStorage.getInstance();
        imgStorageReference = imgStorage.getReference();




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

        imgMaps.setOnClickListener(view -> {

//            if (filepath!=null){
//                DataBagiSampah.setImgSampah(filepath);
//            }
            startActivity(new Intent(getContext(), MapsActivityBagi.class));
//            getActivity().finish();
        });

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






        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filepath==null){
                    filepath=filepathGlobal;
                    Log.d(TAG, "filepathnull: "+filepath);

                }
                Log.d(TAG, "GETLATLOC "+DataBagiSampah.getLatLoc());
                if (namaSampah.getText().toString().equalsIgnoreCase("")|| deskripsiSampah.getText().toString().equalsIgnoreCase("")  || alamatSampah.getText().toString().equalsIgnoreCase("")|| hargaSampahString.equals("null") || DataBagiSampah.getLatLoc()==null){
                    Toast.makeText(getContext(), "Mohon isi semua form yang tersedia.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onClick: masuk if");
                } else {
                    uploadImage();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        hargaSampah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                hargaSampahString = hargaSampah.getText().toString();
            }
        });


        checkBoxGratis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxGratis.isChecked()){
                    hargaTemp = hargaSampah.getText().toString();
                    hargaSampah.setEnabled(false);
                    hargaSampah.setCursorVisible(false);
                    hargaSampah.setText("0");
                    hargaSampah.setBackgroundResource(R.drawable.rectangular_edit_grey);
                    textRP.setTextColor(Color.parseColor("#9e9e9e"));
                    hargaSampahString = "0";
                    Toast.makeText(getContext(),"Checked", Toast.LENGTH_LONG).show();
                }
                else {
                   // hargaSampah.setFocusable(true);
                    hargaSampah.setEnabled(true);
                    hargaSampah.setCursorVisible(true);
                    hargaSampah.setBackgroundResource(R.drawable.rectangular_edit);
                    hargaSampah.setText(hargaTemp);
                    textRP.setTextColor(Color.parseColor("#212121"));
                    hargaSampahString = hargaSampah.getText().toString();
                    Toast.makeText(getContext(),"Unchecked", Toast.LENGTH_LONG).show();
                }
            }
        });






        return inflate;
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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filepath);
                imgUploadSampah.setImageBitmap(bitmap);
                BitmapDrawable drawable = (BitmapDrawable) imgUploadSampah.getDrawable();
                Bitmap bitmap2 = drawable.getBitmap();
                Log.d(TAG, "setData: "+drawable);
                DataBagiSampah.setImgSampah(bitmap2);
                DataBagiSampah.setImgSampahUri(filepath);
                Log.d(TAG, "filepath "+filepath);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }





    private void uploadImage(){
        if(filepath !=null){
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
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

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setData();
    }

    @Override
    public void onPause() {
        super.onPause();
        setData();
    }

    private void setData (){
        DataBagiSampah.setNamaSampah(namaSampah.getText().toString());
        DataBagiSampah.setDeskripsiSampah(deskripsiSampah.getText().toString());
        DataBagiSampah.setKategoriSampah(spinnerKategori.getSelectedItem().toString());
        DataBagiSampah.setAlamatSampah(alamatSampah.getText().toString());
        DataBagiSampah.setHargaSampah(hargaSampah.getText().toString());

    }

    @Override
    public void onResume() {
        super.onResume();

        namaSampah.setText(DataBagiSampah.getNamaSampah());
        deskripsiSampah.setText(DataBagiSampah.getDeskripsiSampah());
        alamatSampah.setText(DataBagiSampah.getAlamatSampah());
        hargaSampah.setText(DataBagiSampah.getHargaSampah());
        if (DataBagiSampah.getImgSampah() !=null){
            imgUploadSampah.setImageBitmap(DataBagiSampah.getImgSampah());
            imgUploadSampah.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        filepathGlobal = DataBagiSampah.getImgSampahUri();
        Log.d(TAG, "filepath global: "+filepathGlobal);

//        if (DataBagiSampah.getImgSampah()!=null){
////            try {
////                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), DataBagiSampah.getImgSampah());
////                imgUploadSampah.setImageBitmap(bitmap);
////            } catch (IOException e){
////                e.printStackTrace();
////            }
//            imgUploadSampah.setImageURI(DataBagiSampah.getImgSampah());
//        }

    }

    private void uploadData(){
        String imgString = imgLink;
        String namaSampahString = namaSampah.getText().toString();
        String deskripsiSampahString = deskripsiSampah.getText().toString();
        String kategoriSampahString = spinnerKategori.getSelectedItem().toString();
        String latlocSampahString = DataBagiSampah.getLatLoc();
        String longlocSampahString = DataBagiSampah.getLongLoc();
        String statusSampahString = "Available";
        String userString = auth.getCurrentUser().getUid();
        Log.d("user", userString);
        String alamatSampahString = alamatSampah.getText().toString();
        String idPengambil = "idpengambil0";
        HashMap<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("img", imgString);
        dataMap.put("namaSampah", namaSampahString);
        dataMap.put("deskripsiSampah", deskripsiSampahString);
        dataMap.put("kategoriSampah", kategoriSampahString);
        dataMap.put("latlocSampah", latlocSampahString);
        dataMap.put("longlocSampah", longlocSampahString);
        dataMap.put("hargaSampah", hargaSampahString);
        dataMap.put("statusSampah", statusSampahString);
        dataMap.put("user", userString);
        dataMap.put("alamatSampah", alamatSampahString);
        dataMap.put("namaUser", namaUserString);
        dataMap.put("nomorTelepon", nomorTeleponString);
        dataMap.put("idPengambil", idPengambil);
        dataMap.put("notifyBook", "0");

        mDatabase.child("DBSampah").push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                DataBagiSampah.setNullAll();
                DataEditSampah.setNullAll();

            }
        });


    }



}
