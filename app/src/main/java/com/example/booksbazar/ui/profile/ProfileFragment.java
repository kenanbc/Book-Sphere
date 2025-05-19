package com.example.booksbazar.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booksbazar.model.Book;
import com.example.booksbazar.adapter.BookAdapter;
import com.example.booksbazar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    RecyclerView recyclerView;
    BookAdapter bookAdapter;
    List<Book> userBooks;
    FirebaseAuth mAuth;
    String currentUserId;
    private TextView noContentID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView nameSurnameTextView = root.findViewById(R.id.nameSurnameProfileID);
        TextView locationTextView = root.findViewById(R.id.locationProfileID);
        TextView statusTextView = root.findViewById(R.id.statusProfileID);
        noContentID = root.findViewById(R.id.noContentID);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        profileViewModel.getFullName().observe(getViewLifecycleOwner(), name -> {
            nameSurnameTextView.setText(name);
        });

        profileViewModel.getLocation().observe(getViewLifecycleOwner(), loc -> {
            locationTextView.setText(loc);
        });

        profileViewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            statusTextView.setText(status);
        });

        profileViewModel.loadUserData();

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        recyclerView = root.findViewById(R.id.booksRecView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        userBooks = new ArrayList<>();
        bookAdapter = new BookAdapter(getContext(), userBooks, R.id.action_nav_profile_to_bookDetailFragment);
        recyclerView.setAdapter(bookAdapter);

        loadUserBooks();

        return root;
    }

    private void loadUserBooks() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("books")
                .whereEqualTo("userId", currentUserId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    userBooks.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Book book = document.toObject(Book.class);
                        book.setBookId(document.getId());
                        userBooks.add(book);
                    }
                    bookAdapter.notifyDataSetChanged();
                    if (userBooks.isEmpty()) {
                        noContentID.setVisibility(View.VISIBLE);
                    } else {
                        noContentID.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                });
    }

}
