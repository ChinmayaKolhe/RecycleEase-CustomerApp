package com.example.recycleease_customerapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecyclingCenterAdapter extends RecyclerView.Adapter<RecyclingCenterAdapter.ViewHolder> {
    private List<RecyclingCenter> recyclingCenterList;

    public RecyclingCenterAdapter(List<RecyclingCenter> recyclingCenterList) {
        this.recyclingCenterList = recyclingCenterList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycling_center, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclingCenter center = recyclingCenterList.get(position);
        holder.tvCenterName.setText(center.getName() != null ? center.getName() : "Unknown");
        holder.tvCenterAddress.setText(center.getAddress() != null ? "Address: " + center.getAddress() : "Address: Not available");
        holder.tvCenterContact.setText(center.getContact() != null ? "Contact: " + center.getContact() : "Contact: Not available");
        holder.tvWasteTypes.setText(center.getWasteTypes() != null ? "Waste Types: " + center.getWasteTypes() : "Waste Types: Not available");
        holder.tvCenterDescription.setText(center.getDescription() != null ? "Description: " + center.getDescription() : "Description: Not available");
        holder.tvCenterWorkingHours.setText(center.getWorkingHours() != null ? "Working Hours: " + center.getWorkingHours() : "Working Hours: Not available");

    }

    @Override
    public int getItemCount() {
        return recyclingCenterList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCenterName, tvCenterAddress, tvCenterContact, tvWasteTypes, tvCenterDescription, tvCenterWorkingHours;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCenterName = itemView.findViewById(R.id.tvCenterName);
            tvCenterAddress = itemView.findViewById(R.id.tvCenterAddress);
            tvCenterContact = itemView.findViewById(R.id.tvCenterContact);
            tvWasteTypes = itemView.findViewById(R.id.tvWasteTypes);
            tvCenterDescription = itemView.findViewById(R.id.tvCenterDescription);
            tvCenterWorkingHours = itemView.findViewById(R.id.tvCenterWorkingHours);
        }
    }
}
