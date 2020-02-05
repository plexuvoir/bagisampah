package com.example.bagisampah;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
    private String namaUserString, nomorTeleponString;
    private Spinner spinnerKategori;
    private static String imgLink = "belum masuk";
    private ImageView imgMaps;
    private String  eAddress;
    private double eLatitude, eLongitude;
    private String nama="",deskripsi="",alamat="",harga="";

    private ImageView imgUploadSampah;
    private Uri filepath, filepathGlobal;
    private final int PICK_IMAGE_REQUEST = 71;

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
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        imgUploadSampah = inflate.findViewById(R.id.imgUploadSampah);
        imgMaps = inflate.findViewById(R.id.imgMaps);
        imgStorage = FirebaseStorage.getInstance();
        imgStorageReference = imgStorage.getReference();






        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());


//        if (prefs != null){
//            eLatitude = Double.longBitsToDouble(prefs.getLong("lat",0));
//            eLongitude = Double.longBitsToDouble(prefs.getLong("long",0));
//            eAddress = prefs.getString("address",alamatSampah.getText().toString());
//            System.out.println(eAddress);
//            LatLng latLng = new LatLng(eLatitude, eLongitude);
////// Add Marker
////            mMap.addMarker(new MarkerOptions().position(latLng));
////// Center map on the marker
////            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng, 4.0f);
////            mMap.animateCamera(yourLocation);
////
////            final ImageView mapPreview = inflate.findViewById(R.id.imgMaps);
////            mapPreview.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    // Hide the preview, to reveal the map
////                    mapPreview.setImageBitmap(null);
////                    mapPreview.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
////
////                    // Or start Google Maps app
//////      String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 50.0, 0.1);
//////      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//////      startActivity(intent);
////                }
////            });
////
////            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
////                @Override
////                public void onMapLoaded() {
////                    // Make a snapshot when map's done loading
////                    mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
////                        @Override
////                        public void onSnapshotReady(Bitmap bitmap) {
////                            mapPreview.setLayoutParams(new RelativeLayout.LayoutParams(
////                                    ViewGroup.LayoutParams.MATCH_PARENT,
////                                    ViewGroup.LayoutParams.MATCH_PARENT));
////                            mapPreview.setImageBitmap(bitmap);
////
////                            // If map won't be used afterwards, remove it's views
//////              ((FrameLayout)findViewById(R.id.map)).removeAllViews();
////                        }
////                    });
////                }
////            });
//
//            alamatSampah.setText(eAddress);
//        }

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
            getActivity().finish();
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
                uploadImage();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();

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
        String latlocSampahString = "Latloc Sampah 0";
        String longlocSampahString = "Longloc Sampah 0";
        String hargaSampahString = hargaSampah.getText().toString();
        String statusSampahString = "Available";
        String jarakSampahString = "Jarak Sampah 0";
        String userString = auth.getCurrentUser().getUid();
        Log.d("user", userString);
        String alamatSampahString = alamatSampah.getText().toString();
        String idPengambil = "idpengambil0";
        String namaPengambil = "namapengambil0";
        String nomorPengambil = "nomorpengambil0";
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
        dataMap.put("namaUser", namaUserString);
        dataMap.put("nomorTelepon", nomorTeleponString);
        dataMap.put("idPengambil", idPengambil);
        dataMap.put("namaPengambil", namaPengambil);
        dataMap.put("nomorPengambil", nomorPengambil);
        mDatabase.child("DBSampah").push().setValue(dataMap);
        DataBagiSampah.setNamaSampah(null);
        DataBagiSampah.setAlamatSampah(null);
        DataBagiSampah.setHargaSampah(null);
        DataBagiSampah.setDeskripsiSampah(null);
        DataBagiSampah.setImgSampah(null);

    }
}
