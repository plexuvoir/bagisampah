package com.example.bagisampah;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchFragment extends Fragment {
    public List<List_Sampah> list_sampahs = new ArrayList<List_Sampah>();
    public RecyclerView recycler_sampah;
    public RecyclerView.Adapter adapter_sampah;
    private FirebaseDatabase db;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseAuth auth;
    HashMap<String,String> sampah = new HashMap<>();
    private SampahAdapter adapter;
    private DatabaseReference mDatabase;
    TabLayout tabLayout;
    ViewPager viewPager;
    PageAdapter pageAdapter;
    TabItem tabSemua, tabPlastik, tabKertas, tabTekstil, tabKaleng, tabKaca;


    @Nullable
    @Override


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View inflate =inflater.inflate(R.layout.fragment_search,null);
        recycler_sampah = inflate.findViewById(R.id.recycler_sampah);

        tabLayout = inflate.findViewById(R.id.tabLayout);
        tabSemua = inflate.findViewById(R.id.tabSemua);
        tabPlastik = inflate.findViewById(R.id.tabPlastik);
        tabKertas = inflate.findViewById(R.id.tabKertas);
        tabTekstil = inflate.findViewById(R.id.tabTekstil);
        tabKaleng = inflate.findViewById(R.id.tabKaleng);
        tabKaca = inflate.findViewById(R.id.tabKaca);
        viewPager = inflate.findViewById(R.id.viewPager);

        pageAdapter = new PageAdapter(getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));




//        recycler_sampah.setHasFixedSize(true);
//        linearLayoutManager = new LinearLayoutManager(getContext());
//        recycler_sampah.setLayoutManager(linearLayoutManager);
//        db = FirebaseDatabase.getInstance();
//        auth = FirebaseAuth.getInstance();
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//
//
//        db.getReference("DBSampah").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                list_sampahs.clear();
//                for (DataSnapshot sn : dataSnapshot.getChildren()){
//                    String img = sn.child("img").getValue(String.class);
//                    String nama = sn.child("namaSampah").getValue(String.class);
//                    String deskripsi = sn.child("deskripsiSampah").getValue(String.class);
//                    String kategori = sn.child("kategoriSampah").getValue(String.class);
//                    String latloc = sn.child("latlocSampah").getValue(String.class);
//                    String longloc = sn.child("longlocSampah").getValue(String.class);
//                    String harga = sn.child("hargaSampah").getValue(String.class);
//                    String status = sn.child("statusSampah").getValue(String.class);
//                    String jarak = sn.child("jarakSampah").getValue(String.class);
//                    String alamat = sn.child("alamatSampah").getValue(String.class);
//                    String uid = sn.child("user").getValue(String.class);
//                    String namaUser = sn.child("namaUser").getValue(String.class);
//                    String nomorTelepon = sn.child("nomorTelepon").getValue(String.class);
//                    list_sampahs.add(new List_Sampah(img, nama, deskripsi, kategori, latloc, longloc, harga, status, jarak, alamat, uid, namaUser, nomorTelepon));
//                }
//                adapter = new SampahAdapter(getContext(), list_sampahs);
//                recycler_sampah.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        return inflate;


    }


}
