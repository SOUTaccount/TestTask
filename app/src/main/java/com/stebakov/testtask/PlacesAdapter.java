package com.stebakov.testtask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.MyViewHolder> {
    ArrayList<Places> alPlaces;
    Context context;
    public PlacesAdapter(Context ct, ArrayList<Places> alp){
        this.context = ct;
        this.alPlaces = alp;
    }
    @NonNull
    @Override
    public PlacesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.raw_places, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesAdapter.MyViewHolder holder, int position) {
        holder.title.setText(alPlaces.get(position).getName());
        Picasso.with(context)
                .load(alPlaces.get(position).getImage())
                .fit()
                .placeholder(R.mipmap.ic_launcher) // Отображается в случае ошибки
                .error(R.mipmap.ic_launcher)// Отображается в случае ошибки
                .into(holder.img);// Расположение нашего ImageView
    }

    @Override
    public int getItemCount() {
        return alPlaces.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_view);
            title = itemView.findViewById(R.id.tv_title);
        }
    }
}
