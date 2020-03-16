package com.example.bagisampah.View.MainMenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bagisampah.GPSTracker;
import com.example.bagisampah.Model.DataSampah;
import com.example.bagisampah.R;
import com.example.bagisampah.Adapter.SampahAdapter;
import com.example.bagisampah.View.FilterActivity;
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
    public List<DataSampah> data_sampahs = new ArrayList<DataSampah>();
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
    int harga, hargaMax;
    boolean read = false;

     static double currentLatitude;
     static double currentLongitude;


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
        hargaMax = 0;

        setHasOptionsMenu(true);

        db = FirebaseDatabase.getInstance();
        db.getReference("DBSampah").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot sn : dataSnapshot.getChildren()){
                    if(sn.child("statusSampah").getValue(String.class).equalsIgnoreCase("Available")){
                        harga = Integer.parseInt(sn.child("hargaSampah").getValue(String.class));
                        if (harga>=hargaMax){
                            hargaMax=harga;
                        }
                    }
                }
                read = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


            GPSTracker gps = new GPSTracker(getContext());
            currentLatitude = gps.getLatitude();
            currentLongitude = gps.getLongitude();



        pageAdapter = new PageAdapter(getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return inflate;


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                if (read){
                    Intent intent = new Intent(getContext(), FilterActivity.class);
                    intent.putExtra("hargaMax", hargaMax);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), "Memuat data, harap tunggu", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static double getCurrentLatitude() {
        return currentLatitude;
    }

    public static double getCurrentLongitude() {
        return currentLongitude;
    }
}
