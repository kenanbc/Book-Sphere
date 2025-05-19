package com.example.booksbazar.ui.messages;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booksbazar.model.ChatPreview;
import com.example.booksbazar.adapter.ChatPreviewAdapter;
import com.example.booksbazar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<ChatPreview> chatList;
    private ChatPreviewAdapter adapter;
    private FirebaseFirestore db;
    private String currentUserId;

    public MessagesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerViewChats);
        chatList = new ArrayList<>();
        adapter = new ChatPreviewAdapter(getContext(), chatList, chat -> openChat(chat));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadChats();
    }

    private void loadChats() {
        db.collection("chats")
                .whereArrayContains("participants", currentUserId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return;
                    }
                    if (value == null) {
                        return;
                    }

                    chatList.clear();

                    for (DocumentSnapshot doc : value.getDocuments()) {
                        String chatId = doc.getId();

                        List<String> participants = (List<String>) doc.get("participants");
                        if (participants == null || participants.size() < 2) {
                            continue;
                        }

                        String otherUserId = null;
                        for (String participant : participants) {
                            if (!participant.equals(currentUserId)) {
                                otherUserId = participant;
                                break;
                            }
                        }
                        String lastMessage = doc.getString("lastMessage");
                        Long timestampObj = doc.getLong("timestamp");
                        long timestamp = timestampObj != null ? timestampObj : 0L;

                        ChatPreview preview = new ChatPreview(chatId, otherUserId, lastMessage, timestamp);
                        chatList.add(preview);
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void openChat(ChatPreview chat) {
        Bundle bundle = new Bundle();
        bundle.putString("userId", chat.getOtherUserId());

        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.fragment_chat, bundle);
    }
}

