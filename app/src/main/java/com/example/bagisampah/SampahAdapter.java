package com.example.bagisampah;
import android.content.Context;
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
import com.squareup.picasso.Picasso;

import java.util.List;

public class SampahAdapter extends RecyclerView.Adapter<SampahAdapter.ViewHolder>{
    private Context context;
    private List<List_Sampah> listSampahs;

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
        holder.harga.setText(listSampah.getHarga());
        holder.jarak.setText(listSampah.getJarak());
        Picasso.get().load(listSampah.getImg()).into(holder.imgSampah);
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
