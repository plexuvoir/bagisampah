package com.example.bagisampah.View.Search;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bagisampah.Model.DataFilter;
import com.example.bagisampah.Model.DataSampah;
import com.example.bagisampah.R;
import com.example.bagisampah.Adapter.SampahAdapter;
import com.example.bagisampah.View.MainMenu.SearchFragment;
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


public class KalengFragment extends Fragment {

    private List<DataSampah> data_sampahs = new ArrayList<DataSampah>();
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
        final View inflate =inflater.inflate(R.layout.fragment_kaleng,null);
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
                data_sampahs.clear();
                Location locSaya = new Location("");
                locSaya.setLatitude(SearchFragment.getCurrentLatitude());
                locSaya.setLongitude(SearchFragment.getCurrentLongitude());
                for (DataSnapshot sn : dataSnapshot.getChildren()){

                    //hitung jarak


                    Location location = new Location("");
                    location.setLatitude(Double.parseDouble(sn.child("latlocSampah").getValue(String.class)));
                    location.setLongitude(Double.parseDouble(sn.child("longlocSampah").getValue(String.class)));

                    float jarakMeter = locSaya.distanceTo(location);
                    String jarakKM = String.valueOf(Math.round((jarakMeter/1000)*100.0)/100.0);

                    if (DataFilter.getFiltered()){
                        if(sn.child("kategoriSampah").getValue(String.class).equalsIgnoreCase("kaleng") && sn.child("statusSampah").getValue(String.class).equalsIgnoreCase("Available")&& Integer.parseInt(sn.child("hargaSampah").getValue(String.class))<=Integer.parseInt(DataFilter.getMaxHarga())&& (jarakMeter/1000)<=Float.parseFloat(DataFilter.getMaxJarak())){
                            String img = sn.child("img").getValue(String.class);
                            String nama = sn.child("namaSampah").getValue(String.class);
                            String deskripsi = sn.child("deskripsiSampah").getValue(String.class);
                            String kategori = sn.child("kategoriSampah").getValue(String.class);
                            String latloc = sn.child("latlocSampah").getValue(String.class);
                            String longloc = sn.child("longlocSampah").getValue(String.class);
                            String harga = sn.child("hargaSampah").getValue(String.class);
                            String status = sn.child("statusSampah").getValue(String.class);
                            String jarak = jarakKM;
                            String alamat = sn.child("alamatSampah").getValue(String.class);
                            String uid = sn.child("user").getValue(String.class);
                            String key = sn.getKey();
                            String idPengambil = sn.child("idPengambil").getValue(String.class);
                            data_sampahs.add(new DataSampah(img, nama, deskripsi, kategori, latloc, longloc, harga, status, jarak, alamat, uid, key, idPengambil));
                            Collections.reverse(data_sampahs);
                        }
                    } else {
                        if (sn.child("kategoriSampah").getValue(String.class).equalsIgnoreCase("kaleng") && sn.child("statusSampah").getValue(String.class).equalsIgnoreCase("Available")){
                            String img = sn.child("img").getValue(String.class);
                            String nama = sn.child("namaSampah").getValue(String.class);
                            String deskripsi = sn.child("deskripsiSampah").getValue(String.class);
                            String kategori = sn.child("kategoriSampah").getValue(String.class);
                            String latloc = sn.child("latlocSampah").getValue(String.class);
                            String longloc = sn.child("longlocSampah").getValue(String.class);
                            String harga = sn.child("hargaSampah").getValue(String.class);
                            String status = sn.child("statusSampah").getValue(String.class);
                            String jarak = jarakKM;
                            String alamat = sn.child("alamatSampah").getValue(String.class);
                            String uid = sn.child("user").getValue(String.class);
                            String key = sn.getKey();
                            String idPengambil = sn.child("idPengambil").getValue(String.class);
                            data_sampahs.add(new DataSampah(img, nama, deskripsi, kategori, latloc, longloc, harga, status, jarak, alamat, uid, key, idPengambil));
                            Collections.reverse(data_sampahs);
                        }
                    }
                }
                adapter = new SampahAdapter(getContext(), data_sampahs);
                recycler_sampah.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return inflate;
    }


}
