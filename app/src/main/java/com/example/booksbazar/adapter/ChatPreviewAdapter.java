package com.example.booksbazar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booksbazar.R;
import com.example.booksbazar.model.ChatPreview;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatPreviewAdapter extends RecyclerView.Adapter<ChatPreviewAdapter.ChatViewHolder> {

    public interface OnChatClickListener {
        void onChatClick(ChatPreview chat);
    }

    private final Context context;
    private final List<ChatPreview> chatList;
    private final OnChatClickListener listener;

    public ChatPreviewAdapter(Context context, List<ChatPreview> chatList, OnChatClickListener listener) {
        this.context = context;
        this.chatList = chatList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatPreview chat = chatList.get(position);

        holder.textViewLastMessage.setText(chat.getLastMessage());
        // holder.textViewTime.setText(formatTimestamp(chat.getTimestamp()));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(chat.getOtherUserId())
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        String firstname = doc.getString("firstName");
                        String lastname = doc.getString("lastName");
                        holder.textViewUsername.setText(firstname + " " +  lastname);
                    } else {
                        holder.textViewUsername.setText("Unknown");
                    }
                })
                .addOnFailureListener(e -> {
                    holder.textViewUsername.setText("Error");
                });

        holder.itemView.setOnClickListener(v -> listener.onChatClick(chat));
    }


    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUsername, textViewLastMessage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            textViewLastMessage = itemView.findViewById(R.id.textViewLastMessage);

        }
    }

    private String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}

