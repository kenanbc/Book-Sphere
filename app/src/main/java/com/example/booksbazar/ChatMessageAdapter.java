package com.example.booksbazar;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MessageViewHolder> {

    private final Context context;
    private final List<ChatMessage> messageList;
    private final String currentUserId;

    public ChatMessageAdapter(Context context, List<ChatMessage> messageList) {
        this.context = context;
        this.messageList = messageList;
        this.currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);

        holder.textViewMessage.setText(message.getMessage());
        holder.textViewTimestamp.setText(formatTimestamp(message.getTimestamp()));

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.messageContainer.getLayoutParams();

        if (message.getSenderId().equals(currentUserId)) {
            holder.messageContainer.setBackgroundResource(R.drawable.bg_message_mine);
            params.gravity = Gravity.END;
        } else {
            holder.messageContainer.setBackgroundResource(R.drawable.bg_message_other);
            params.gravity = Gravity.START;
        }

        holder.messageContainer.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage, textViewTimestamp;
        View messageContainer;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageContainer = itemView.findViewById(R.id.messageContainer);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
        }
    }

    private String formatTimestamp(long timestamp) {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(timestamp));
    }
}
