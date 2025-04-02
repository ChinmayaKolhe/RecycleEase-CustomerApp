package com.example.recycleease_customerapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.PickupRequestViewHolder> {
    private Context context;
    private ArrayList<PickupRequest> pickupRequests;

    public RequestAdapter(Context context, ArrayList<PickupRequest> pickupRequests) {
        this.context = context;
        this.pickupRequests = pickupRequests;
    }

    @NonNull
    @Override
    public PickupRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_request, parent, false);
        return new PickupRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PickupRequestViewHolder holder, int position) {
        PickupRequest request = pickupRequests.get(position);
        holder.tvName.setText(request.getName());
        holder.tvWasteType.setText(request.getWasteType());
        holder.tvPickupDate.setText(request.getPickupDate());
        holder.tvStatus.setText(request.getStatus());
        holder.tvPhone.setText(request.getPhone());
        holder.tvaddress.setText(request.getAddress());


        // Display in ImageView

    }

    @Override
    public int getItemCount() {
        return pickupRequests.size();
    }

    public static class PickupRequestViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvWasteType, tvPickupDate, tvStatus,tvPhone,tvaddress;

        public PickupRequestViewHolder(View itemView) {
            super(itemView);
            tvaddress=itemView.findViewById(R.id.tvAddress);
            tvName = itemView.findViewById(R.id.tvName);
            tvWasteType = itemView.findViewById(R.id.tvWasteType);
            tvPickupDate = itemView.findViewById(R.id.tvPickupDate);
            tvPhone=itemView.findViewById(R.id.tvPhoneNumber);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }


}
