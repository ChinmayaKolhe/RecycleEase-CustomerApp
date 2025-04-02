package com.example.recycleease_customerapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecyclingCenterAdapter extends RecyclerView.Adapter<RecyclingCenterAdapter.ViewHolder> {

    private ArrayList<RecyclingCenter> centerList;

    public RecyclingCenterAdapter(ArrayList<RecyclingCenter> centerList) {
        this.centerList = centerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycling_center, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclingCenter center = centerList.get(position);

        if (center != null) {
            holder.tvName.setText(center.getName() != null ? center.getName() : "No Name");
            holder.tvAddress.setText(center.getLocation() != null ? center.getLocation() : "No Address");
            holder.tvContact.setText("Contact: " + (center.getContact() != null ? center.getContact() : "No Contact"));
        }
    }

    @Override
    public int getItemCount() {
        return centerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress, tvContact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCenterName);
            tvAddress = itemView.findViewById(R.id.tvCenterAddress);
            tvContact = itemView.findViewById(R.id.tvCenterContact);
        }
    }
}
