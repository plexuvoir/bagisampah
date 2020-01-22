package com.example.bagisampah;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;




public class KacaFragment extends Fragment {

    private List<List_Sampah> list_sampahs = new ArrayList<List_Sampah>();
    private RecyclerView recycler_sampah;
    private RecyclerView.Adapter adapter_sampah;
    private FirebaseDatabase db;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseAuth auth;
    HashMap<String,String> sampah = new HashMap<>();
    private SampahAdapter adapter;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        final View inflate =inflater.inflate(R.layout.fragment_kaca,null);
        recycler_sampah = inflate.findViewById(R.id.recycler_sampah);
        recycler_sampah.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recycler_sampah.setLayoutManager(linearLayoutManager);
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        db.getReference("DBSampah").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_sampahs.clear();
                for (DataSnapshot sn : dataSnapshot.getChildren()){
                    if (sn.child("kategoriSampah").getValue(String.class).equalsIgnoreCase("kaca")){
                        String img = sn.child("img").getValue(String.class);
                        String nama = sn.child("namaSampah").getValue(String.class);
                        String deskripsi = sn.child("deskripsiSampah").getValue(String.class);
                        String kategori = sn.child("kategoriSampah").getValue(String.class);
                        String latloc = sn.child("latlocSampah").getValue(String.class);
                        String longloc = sn.child("longlocSampah").getValue(String.class);
                        String harga = sn.child("hargaSampah").getValue(String.class);
                        String status = sn.child("statusSampah").getValue(String.class);
                        String jarak = sn.child("jarakSampah").getValue(String.class);
                        String alamat = sn.child("alamatSampah").getValue(String.class);
                        String uid = sn.child("user").getValue(String.class);
                        String namaUser = sn.child("namaUser").getValue(String.class);
                        String nomorTelepon = sn.child("nomorTelepon").getValue(String.class);
                        list_sampahs.add(new List_Sampah(img, nama, deskripsi, kategori, latloc, longloc, harga, status, jarak, alamat, uid, namaUser, nomorTelepon));
                        Collections.reverse(list_sampahs);
                    }
                }
                adapter = new SampahAdapter(getContext(), list_sampahs);
                recycler_sampah.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return inflate;
    }


}
