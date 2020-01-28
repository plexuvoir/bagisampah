package com.example.bagisampah;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class SampahAdapter extends RecyclerView.Adapter<SampahAdapter.ViewHolder>{
    private Context context;
    private List<List_Sampah> listSampahs;
    private FirebaseAuth auth;
    private FirebaseDatabase db;


    public SampahAdapter(Context context, List<List_Sampah> my_data) {
        this.context = context;
        this.listSampahs = my_data;
    }

    @NonNull
    @Override
    public SampahAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_sampah,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SampahAdapter.ViewHolder holder, final int position) {
        List_Sampah listSampah = listSampahs.get(position);
        holder.nama.setText(listSampah.getNama());
        holder.deskripsi.setText(listSampah.getDeskripsi());
        holder.harga.setText("Rp."+listSampah.getHarga());
        holder.jarak.setText(listSampah.getJarak());
        Picasso.get().load(listSampah.getImg()).into(holder.imgSampah);

        holder.card_sampah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = FirebaseDatabase.getInstance();
                auth = FirebaseAuth.getInstance();

                if(listSampah.getUser().equalsIgnoreCase(auth.getCurrentUser().getUid())){
                    Log.d(TAG, "onClick: terklik");
                    Log.d(TAG, "onClick: "+ listSampah.getNama());
                    Intent intent = new Intent(v.getContext(), DetailSampahSaya.class);
                    intent.putExtra("imgSampah",listSampah.getImg());
                    intent.putExtra("namaSampah",listSampah.getNama());
                    intent.putExtra("deskripsiSampah",listSampah.getDeskripsi());
                    intent.putExtra("hargaSampah",listSampah.getHarga());
                    intent.putExtra("namaUser",listSampah.getNamaUser());
                    intent.putExtra("kontakUser",listSampah.getNomorTelepon());
                    intent.putExtra("alamatUser",listSampah.getAlamat());
                    intent.putExtra("kategoriSampah",listSampah.getKategori());
                    intent.putExtra("key", listSampah.getKey());
                    Log.d("keyyy", listSampah.getKey());
                    v.getContext().startActivity(intent);
                }else{
                    Log.d(TAG, "onClick: terklik");
                    Log.d(TAG, "onClick: "+ listSampah.getNama());
                    Intent intent = new Intent(v.getContext(), DetailSampah.class);
                    intent.putExtra("imgSampah",listSampah.getImg());
                    intent.putExtra("namaSampah",listSampah.getNama());
                    intent.putExtra("deskripsiSampah",listSampah.getDeskripsi());
                    intent.putExtra("hargaSampah",listSampah.getHarga());
                    intent.putExtra("namaUser",listSampah.getNamaUser());
                    intent.putExtra("kontakUser",listSampah.getNomorTelepon());
                    intent.putExtra("alamatUser",listSampah.getAlamat());
                    v.getContext().startActivity(intent);
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return listSampahs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nama, deskripsi, harga, jarak;
        private ImageView imgSampah;
        public CardView card_sampah;
        public ViewHolder(View itemView){
            super(itemView);
            card_sampah= (CardView) itemView.findViewById(R.id.card_sampah);
            nama = itemView.findViewById(R.id.nama);
            deskripsi = itemView.findViewById(R.id.deskripsi);
            harga = itemView.findViewById(R.id.harga);
            jarak = itemView.findViewById(R.id.jarak);
            imgSampah = itemView.findViewById(R.id.imgSampah);
        }
    }
}
