package com.example.bagisampah;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.Toast;

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

public class BagiSampahFragment extends Fragment {
    private EditText namaSampah, deskripsiSampah, kategoriSampah, alamatSampah, hargaSampah;
    private Button btn_post;
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference mDatabase;
    private String namaUserString, nomorTeleponString;
    private Spinner spinnerKategori;
    private static String imgLink = "belum masuk";

    private ImageView imgUploadSampah;
    private Uri filepath;
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
        //kategoriSampah = inflate.findViewById(R.id.kategoriSampah);
        spinnerKategori = inflate.findViewById(R.id.spinnerKategori);
        alamatSampah = inflate.findViewById(R.id.alamatSampah);
        hargaSampah = inflate.findViewById(R.id.hargaSampah);
        btn_post = inflate.findViewById(R.id.btn_post);
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        imgUploadSampah = inflate.findViewById(R.id.imgUploadSampah);

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

        //image click
        imgUploadSampah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
                String imgString = imgLink;
                String namaSampahString = namaSampah.getText().toString();
                Log.d("nss", namaSampahString);
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
                Log.d("nts", nomorTeleponString);
                mDatabase.child("DBSampah").push().setValue(dataMap);
                Log.d("testcuy", "onClick: test");
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
                            String linklink = uri.toString();
                            imgLink = linklink;
                            Log.d("TAG", "onComplete: Url: "+ uri.toString());
                        }

                    });
                }
            });


//            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    Uri imgUrl = task.getResult();
//                    imgLink = imgUrl.toString();
//                }
//            });

        }
    }
}
