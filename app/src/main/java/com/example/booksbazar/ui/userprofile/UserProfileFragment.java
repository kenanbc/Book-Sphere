package com.example.booksbazar.ui.userprofile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booksbazar.model.Book;
import com.example.booksbazar.adapter.BookAdapter;
import com.example.booksbazar.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UserProfileFragment extends Fragment {

    private com.example.booksbazar.ui.userprofile.UserProfileViewModel UserProfileViewModel;
    RecyclerView recyclerView;
    BookAdapter bookAdapter;
    List<Book> userBooks;

    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       View root = inflater.inflate(R.layout.fragment_user_profile, container, false);

        TextView nameSurnameTextView = root.findViewById(R.id.nameSurnameProfileID);
        TextView locationTextView = root.findViewById(R.id.locationProfileID);
        TextView statusTextView = root.findViewById(R.id.statusProfileID);

        UserProfileViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);

        UserProfileViewModel.getFullName().observe(getViewLifecycleOwner(), name -> {
            nameSurnameTextView.setText(name);
        });

        UserProfileViewModel.getLocation().observe(getViewLifecycleOwner(), loc -> {
            locationTextView.setText(loc);
        });

        UserProfileViewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            statusTextView.setText(status);
        });

        Bundle args = getArguments();
        String userId = args.getString("userId");

        UserProfileViewModel.loadUserData(userId);

        recyclerView = root.findViewById(R.id.booksRecView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        userBooks = new ArrayList<>();
        bookAdapter = new BookAdapter(getContext(), userBooks, R.id.action_nav_userProfile_to_bookDetailFragment);
        recyclerView.setAdapter(bookAdapter);

        loadUserBooks(userId);

       return root;
    }

    private void loadUserBooks(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("books")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    userBooks.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Book book = document.toObject(Book.class);
                        book.setBookId(document.getId());
                        userBooks.add(book);
                    }
                    bookAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                });
    }

}