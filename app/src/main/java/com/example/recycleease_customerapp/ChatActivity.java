package com.example.recycleease_customerapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.*;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
public class ChatActivity extends AppCompatActivity {


    private static final String API_URL = "https://api.cohere.com/v1/generate";
    private static final String API_KEY = "mjgnRrkkBHZFKTySWk6qwBJgD2Q3eZ1zGIR8WoRI"; // 🔹 Replace with your actual API Key

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;
    private EditText userInput;
    private ImageButton sendButton;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initializeUI();
        setupHttpClient();
    }

    private void initializeUI() {
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        userInput = findViewById(R.id.userInput);
        sendButton = findViewById(R.id.sendButton);

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        sendButton.setOnClickListener(v -> handleUserInput());
    }

    private void setupHttpClient() {
        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    private void handleUserInput() {
        String message = userInput.getText().toString().trim();
        if (message.isEmpty()) return;

        userInput.setText("");
        addMessageToChat(message, true);
        addLoadingIndicator();
        fetchChatbotResponse(message);
    }

    private void addMessageToChat(String message, boolean isUser) {
        runOnUiThread(() -> {
            chatMessages.add(new ChatMessage(message, isUser));
            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
            chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
        });
    }

    private void addLoadingIndicator() {
        runOnUiThread(() -> {
            chatMessages.add(new ChatMessage("...", false));
            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        });
    }

    private void removeLoadingIndicator() {
        runOnUiThread(() -> {
            if (!chatMessages.isEmpty() &&
                    chatMessages.get(chatMessages.size() - 1).getMessage().equals("...")) {
                int position = chatMessages.size() - 1;
                chatMessages.remove(position);
                chatAdapter.notifyItemRemoved(position);
            }
        });
    }

    private void fetchChatbotResponse(String userMessage) {
        new Thread(() -> {
            try {
                JSONObject payload = new JSONObject();
                payload.put("model", "command-r-plus");  // Cohere's best AI model
                payload.put("prompt", userMessage);
                payload.put("max_tokens", 100);
                payload.put("temperature", 0.7);

                RequestBody body = RequestBody.create(
                        payload.toString(),
                        MediaType.get("application/json; charset=utf-8")
                );

                Request request = new Request.Builder()
                        .url(API_URL)
                        .addHeader("Authorization", "Bearer " + API_KEY)
                        .addHeader("Content-Type", "application/json")
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();

                if (response.isSuccessful()) {
                    JSONObject responseObject = new JSONObject(responseBody);
                    JSONArray generations = responseObject.getJSONArray("generations");
                    String botResponse = generations.getJSONObject(0).getString("text").trim();

                    removeLoadingIndicator();
                    addMessageToChat(botResponse, false);
                } else {
                    handleErrorResponse(responseBody);
                }
            } catch (Exception e) {
                handleException(e);
            }
        }).start();
    }

    private void handleErrorResponse(String errorBody) {
        removeLoadingIndicator();
        addMessageToChat("Error: " + errorBody, false);
    }

    private void handleException(Exception e) {
        removeLoadingIndicator();
        addMessageToChat("Error: " + e.getMessage(), false);
    }
}
