package com.example.booksbazar.ui.saved;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.booksbazar.model.Book;
import com.example.booksbazar.adapter.BookAdapter;
import com.example.booksbazar.R;
import com.example.booksbazar.model.Saved;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SavedFragment extends Fragment {
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<Saved> savedList = new ArrayList<>();
    private List<Book> userBooks = new ArrayList<>();
    private FirebaseFirestore db;

    public static SavedFragment newInstance() {
        return new SavedFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_saved, container, false);

        recyclerView = root.findViewById(R.id.recyclerViewBooksSaved);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        bookAdapter = new BookAdapter(getContext(), userBooks, R.id.action_nav_saved_to_bookDetailFragment);
        recyclerView.setAdapter(bookAdapter);

        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            loadSavedBooks(currentUserId);
        }

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            loadSavedBooks(currentUserId);
        }
    }

    private void loadSavedBooks(String currentUserId) {
        db.collection("saved")
                .whereEqualTo("userId", currentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        savedList.clear();
                        userBooks.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Saved savedBook = document.toObject(Saved.class);
                            String bookId = savedBook.getBookId();
                            savedList.add(savedBook);

                            db.collection("books").document(bookId)
                                    .get()
                                    .addOnSuccessListener(bookDoc -> {
                                        if (bookDoc.exists()) {
                                            Book book = bookDoc.toObject(Book.class);
                                            book.setBookId(bookDoc.getId());

                                            if (!userBooks.contains(book)) {
                                                userBooks.add(book);
                                                bookAdapter.notifyDataSetChanged();
                                            }

                                        }
                                    });
                        }
                    }
                });
    }
}

