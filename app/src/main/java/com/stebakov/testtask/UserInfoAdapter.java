package com.stebakov.testtask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.MyViewHolder> {
    ArrayList<User> alPlaces;
    Context context;
    public UserInfoAdapter(Context ct, ArrayList<User> alp){
        this.context = ct;
        this.alPlaces = alp;
    }
    @NonNull
    @Override
    public UserInfoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.raw_users, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserInfoAdapter.MyViewHolder holder, int position) {
        holder.name.setText(alPlaces.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return alPlaces.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView email;
        TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.tv_email);
            name = itemView.findViewById(R.id.tv_name);
        }
    }
}
