package com.example.booksbazar.ui.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booksbazar.model.ChatMessage;
import com.example.booksbazar.adapter.ChatMessageAdapter;
import com.example.booksbazar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatFragment extends Fragment {

    private EditText editTextMessage;
    private RecyclerView recyclerView;
    private ChatMessageAdapter adapter;
    private List<ChatMessage> messageList;
    private ChatViewModel chatViewModel;
    private TextView chatUsernameTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextMessage = view.findViewById(R.id.editTextMessage);
        recyclerView = view.findViewById(R.id.recyclerViewMessages);
        ImageButton buttonSend = view.findViewById(R.id.buttonSend);
        chatUsernameTextView = view.findViewById(R.id.chatUsernameTextView);

        messageList = new ArrayList<>();
        adapter = new ChatMessageAdapter(getContext(), messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        if (getArguments() != null) {
            String otherUserId = getArguments().getString("userId");
            chatViewModel.setOtherUserId(otherUserId);
        }

        buttonSend.setOnClickListener(v -> {
            String msg = editTextMessage.getText().toString().trim();
            if (!msg.isEmpty()) {
                chatViewModel.sendMessage(msg);
                editTextMessage.setText("");
            }
        });

        observeViewModel();
    }

    private void observeViewModel() {
        chatViewModel.getMessages().observe(getViewLifecycleOwner(), messages -> {
            messageList.clear();
            messageList.addAll(messages);
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(messageList.size() - 1);
        });

        chatViewModel.getChatUserName().observe(getViewLifecycleOwner(), name -> {
            chatUsernameTextView.setText(name);
        });

    }
}


