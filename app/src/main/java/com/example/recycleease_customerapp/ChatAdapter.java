package com.example.recycleease_customerapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<ChatMessage> chatMessages;

    public ChatAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessage message = chatMessages.get(position);

        if (message.isUser()) {
            // Show user message, hide bot message
            holder.userMessageLayout.setVisibility(View.VISIBLE);
            holder.botMessageLayout.setVisibility(View.GONE);
            holder.userMessageText.setText(message.getMessage());
        } else {
            // Show bot message, hide user message
            holder.botMessageLayout.setVisibility(View.VISIBLE);
            holder.userMessageLayout.setVisibility(View.GONE);
            holder.botMessageText.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userMessageText, botMessageText;
        View userMessageLayout, botMessageLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userMessageLayout = itemView.findViewById(R.id.userMessageLayout);
            botMessageLayout = itemView.findViewById(R.id.botMessageLayout);
            userMessageText = itemView.findViewById(R.id.userMessageText);
            botMessageText = itemView.findViewById(R.id.botMessageText);
        }
    }
}
