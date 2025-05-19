package com.example.booksbazar.ui.editbook;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booksbazar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditBookFragment extends Fragment {

    private EditBookViewModel mViewModel;
    private TextView newTitleInput;
    private TextView newAuthorInput;
    private EditText newPriceInput;
    private TextView newImageInput;
    private EditText numberOfPagesInput;
    private Button updateInformationBtn;
    private RatingBar ratingBar;
    private AutoCompleteTextView genreDropdown;
    private AutoCompleteTextView conditionDropdown;

    public static EditBookFragment newInstance() {
        return new EditBookFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_book, container, false);

        newTitleInput = root.findViewById(R.id.newTitleInput);
        newAuthorInput = root.findViewById(R.id.newAuthorInput);
        newPriceInput = root.findViewById(R.id.newPriceInput);
        newImageInput = root.findViewById(R.id.newImageInput);
        numberOfPagesInput = root.findViewById(R.id.numberOfPagesInput);
        ratingBar = root.findViewById(R.id.ratingBar);
        updateInformationBtn = root.findViewById(R.id.addNewBookBtn);

        genreDropdown = root.findViewById(R.id.genreDropdown);
        conditionDropdown = root.findViewById(R.id.conditionDropdown);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.genres_array, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapterCondition = ArrayAdapter.createFromResource(getContext(), R.array.condition_array, android.R.layout.simple_spinner_dropdown_item);

        genreDropdown.setAdapter(adapter);
        conditionDropdown.setAdapter(adapterCondition);

        Bundle args = getArguments();
        assert args != null;
        String bookId = args.getString("bookId");

        fillInputFields(bookId);

        updateInformationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = newTitleInput.getText().toString().trim();
                String author = newAuthorInput.getText().toString().trim();
                String imageUrl = newImageInput.getText().toString().trim();
                String condition = conditionDropdown.getText().toString().trim();
                String genre = genreDropdown.getText().toString().trim();
                String priceStr = newPriceInput.getText().toString().trim();
                String pagesStr = numberOfPagesInput.getText().toString().trim();
                Float rating = ratingBar.getRating();


                if (title.isEmpty() || author.isEmpty() || priceStr.isEmpty() || imageUrl.isEmpty()
                        || pagesStr.isEmpty() || genre.isEmpty() || condition.isEmpty()) {
                    Toast.makeText(getContext(), getString(R.string.fillFields), Toast.LENGTH_SHORT).show();
                    return;
                }

                Integer price = Integer.parseInt(priceStr);
                Integer numOfPages = Integer.parseInt(pagesStr);

                updateBookDetails(bookId, title, author, price, imageUrl, numOfPages, rating, genre, condition);
            }
        });

        return root;
    }

    private void fillInputFields(String bookId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        assert bookId != null;
        DocumentReference bookRef = db.collection("books").document(bookId);
        bookRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String title = documentSnapshot.getString("title");
                String author = documentSnapshot.getString("author");
                Long price = documentSnapshot.getLong("price");
                String priceString = String.valueOf(price);

                String imageUrl = documentSnapshot.getString("imageUrl");
                Long numOfPages = documentSnapshot.getLong("numberOfPages");
                String numOfPagesString = String.valueOf(numOfPages);

                Double rating = documentSnapshot.getDouble("rating");

                String genre = documentSnapshot.getString("genre");
                String condition = documentSnapshot.getString("condition");

                newTitleInput.setText(title);
                newAuthorInput.setText(author);
                newPriceInput.setText(priceString);
                newImageInput.setText(imageUrl);
                numberOfPagesInput.setText(numOfPagesString);
                assert rating != null;
                ratingBar.setRating(Float.parseFloat(String.valueOf(rating)));
                genreDropdown.setText(genre, false);
                conditionDropdown.setText(condition, false);
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
        });
    }

    private void updateBookDetails(String bookId, String title, String author, Integer price, String imageUrl, Integer numOfPages, Float rating, String genre, String condition){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference bookRef = db.collection("books").document(bookId);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        Bundle bundle = new Bundle();
        bundle.putString("bookId", bookId);
        bundle.putString("userId", currentUserId);

        bookRef.update(
                "firstName", title,
                "lastName", author,
                "price", price,
                "imageUrl", imageUrl,
                "numberOfPages", numOfPages,
                "rating", rating,
                "genre", genre,
                "condition", condition
        ).addOnSuccessListener(aVoid -> {
            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
            NavController navController = NavHostFragment.findNavController(EditBookFragment.this);
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.editBookFragment, true)
                    .build();
            navController.navigate(R.id.bookDetailFragment, bundle, navOptions);

        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
        });
    }
}