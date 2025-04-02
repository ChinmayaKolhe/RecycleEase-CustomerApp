package com.example.recycleease_customerapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Random;

public class GuidelinesAdapter extends RecyclerView.Adapter<GuidelinesAdapter.ViewHolder> {

    private List<EWasteGuideline> guidelineList;
    private int[] colors = {  // Random colors array
            Color.parseColor("#FFCDD2"), // Light Red
            Color.parseColor("#C8E6C9"), // Light Green
            Color.parseColor("#BBDEFB"), // Light Blue
            Color.parseColor("#FFE0B2"), // Light Orange
            Color.parseColor("#D1C4E9")  // Light Purple
    };
    private Random random = new Random();

    public GuidelinesAdapter(List<EWasteGuideline> guidelineList) {
        this.guidelineList = guidelineList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guideline, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EWasteGuideline guideline = guidelineList.get(position);
        holder.title.setText(guideline.getTitle());
        holder.description.setText(guideline.getDescription());

        // Set random background color
        int randomColor = colors[random.nextInt(colors.length)];
        holder.cardView.setCardBackgroundColor(randomColor);
    }

    @Override
    public int getItemCount() {
        return guidelineList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.guidelineTitle);
            description = itemView.findViewById(R.id.guidelineDescription);
            cardView = itemView.findViewById(R.id.cardViewGuideline); // CardView ID in XML
        }
    }
}
