package com.example.digimiceconferent.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digimiceconferent.Model.Materi;
import com.example.digimiceconferent.R;

import java.util.ArrayList;

public class RecyclerViewMateriAdapter extends RecyclerView.Adapter<RecyclerViewMateriAdapter.MateriViewHolder> {

    ArrayList<Materi> list = new ArrayList<>();

    public void sendData(ArrayList<Materi> materi) {
        list.clear();
        list.addAll(materi);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MateriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_materi, parent, false);
        return new MateriViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MateriViewHolder holder, int position) {
        Materi materi = list.get(position);
        holder.namaMateri.setText(materi.getNamaMateri());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MateriViewHolder extends RecyclerView.ViewHolder {
        TextView namaMateri;
        public MateriViewHolder(@NonNull View itemView) {
            super(itemView);
            namaMateri = itemView.findViewById(R.id.name_materi);
        }
    }
}
