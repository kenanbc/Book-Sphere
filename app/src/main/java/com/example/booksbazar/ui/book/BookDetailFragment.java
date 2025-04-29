package com.example.booksbazar.ui.book;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.booksbazar.R;
import com.example.booksbazar.Saved;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class BookDetailFragment extends Fragment {

    private BookDetailViewModel mViewModel;
    private TextView nameTextView;
    private TextView locationTextView;

    public static BookDetailFragment newInstance() {
        return new BookDetailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_detail, container, false);

        TextView titleTextView = view.findViewById(R.id.titleDetail);
        TextView authorTextView = view.findViewById(R.id.authorDetail);
        ImageView bookImageView = view.findViewById(R.id.imageDetail);
        TextView priceTextView = view.findViewById(R.id.priceDetail);
        TextView genreTextView = view.findViewById(R.id.genreBookDetail);

        nameTextView = view.findViewById(R.id.nameBookDetail);
        locationTextView = view.findViewById(R.id.locationBookDetail);
        Button sendMessageBtn = view.findViewById(R.id.messageBookDetailBtn);
        Button saveBtn = view.findViewById(R.id.saveBookDetail);
        ImageButton openProfileBtn = view.findViewById(R.id.openProfileBtn);

        Bundle bundle = getArguments();
        String bookId;
        String userId;
        if (bundle != null) {
            titleTextView.setText(bundle.getString("bookTitle"));
            authorTextView.setText(bundle.getString("bookAuthor"));
            priceTextView.setText(bundle.getString("bookPrice") + " KM");
            genreTextView.setText(bundle.getString("bookGenre"));

            Glide.with(this)
                    .load(bundle.getString("bookImage"))
                    .into(bookImageView);
            fetchUserData(bundle.getString("userId"));
            userId = bundle.getString("userId");
            bookId = bundle.getString("bookId");
        } else {
            userId = null;
            bookId = null;
        }


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        checkIfBookIsSaved(db, bookId, currentUser.getUid(), saveBtn);

        saveBtn.setOnClickListener(v -> {
            saveOrUnsaveBook(db, bookId, currentUser.getUid(), saveBtn);
        });

        return view;
    }

    private void checkIfBookIsSaved(FirebaseFirestore db, String bookId, String userId, Button saveBtn) {
        Log.d("Test", bookId);
        Log.d("Test2", userId);
        db.collection("saved")
                .whereEqualTo("userId", userId)
                .whereEqualTo("bookId", bookId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        saveBtn.setText("Unsave");
                    } else {
                        saveBtn.setText("Save");
                    }
                });
    }

    private void saveOrUnsaveBook(FirebaseFirestore db, String bookId, String userId, Button saveBtn) {
        db.collection("saved")
                .whereEqualTo("userId", userId)
                .whereEqualTo("bookId", bookId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.isEmpty()) {
                        for (DocumentSnapshot doc : snapshot.getDocuments()) {
                            db.collection("saved").document(doc.getId()).delete();
                        }
                        saveBtn.setText("Save");
                        Toast.makeText(getContext(), "Book removed from saved.", Toast.LENGTH_SHORT).show();
                    } else {
                        Saved saved = new Saved(userId, bookId);
                        db.collection("saved").add(saved)
                                .addOnSuccessListener(ref -> {
                                    saveBtn.setText("Unsave");
                                    Toast.makeText(getContext(), "Book saved!", Toast.LENGTH_SHORT).show();
                                });
                    }
                });
    }

    private void fetchUserData(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("firstName");
                        String lastName = documentSnapshot.getString("lastName");
                        String location = documentSnapshot.getString("location");
                        if (lastName != null)
                            nameTextView.setText(name + " " + lastName);
                        else
                            nameTextView.setText(name);
                        locationTextView.setText(location);
                    }
                });
    }
}
