package com.example.booksbazar.ui.chat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.booksbazar.model.ChatMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatViewModel extends ViewModel {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String chatId;
    private String otherUserId;

    private final MutableLiveData<List<ChatMessage>> messagesLiveData = new MutableLiveData<>();

    public LiveData<List<ChatMessage>> getMessages() {
        return messagesLiveData;
    }

    private final MutableLiveData<String> chatUserNameLiveData = new MutableLiveData<>();

    public LiveData<String> getChatUserName() {
        return chatUserNameLiveData;
    }

    public void setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
        if (currentUserId != null && otherUserId != null) {
            chatId = generateChatId(currentUserId, otherUserId);
            listenForMessages();
            loadChatUserName();
        }
    }

    public void sendMessage(String messageText) {
        if (messageText.isEmpty() || chatId == null) return;

        ChatMessage message = new ChatMessage(currentUserId, messageText, System.currentTimeMillis());
        DocumentReference chatRef = db.collection("chats").document(chatId);

        chatRef.get().addOnSuccessListener(doc -> {
            if (!doc.exists()) {
                Map<String, Object> chatData = new HashMap<>();
                chatData.put("participants", Arrays.asList(currentUserId, otherUserId));
                chatData.put("lastMessage", messageText);
                chatData.put("timestamp", message.getTimestamp());

                chatRef.set(chatData);
            } else {
                Map<String, Object> updates = new HashMap<>();
                updates.put("lastMessage", messageText);
                updates.put("timestamp", message.getTimestamp());
                chatRef.update(updates);
            }

            chatRef.collection("messages").add(message);
        });
    }
    private void listenForMessages() {
        db.collection("chats")
                .document(chatId)
                .collection("messages")
                .orderBy("timestamp")
                .addSnapshotListener((snap, err) -> {
                    if (snap != null) {
                        List<ChatMessage> messages = new ArrayList<>();
                        for (DocumentSnapshot doc : snap.getDocuments()) {
                            ChatMessage msg = doc.toObject(ChatMessage.class);
                            if (msg != null) messages.add(msg);
                        }
                        messagesLiveData.setValue(messages);
                    }
                });
    }

    private String generateChatId(String user1, String user2) {
        return user1.compareTo(user2) < 0 ? user1 + "_" + user2 : user2 + "_" + user1;
    }

    public void loadChatUserName() {
        if (otherUserId == null) return;

        db.collection("users")
                .document(otherUserId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String firstName = documentSnapshot.getString("firstName");
                        String lastName = documentSnapshot.getString("lastName");
                        String fullName = "";

                        if (firstName != null) fullName += firstName;
                        if (lastName != null) fullName += (fullName.isEmpty() ? "" : " ") + lastName;

                        chatUserNameLiveData.setValue(fullName);
                    }
                });
    }

}

