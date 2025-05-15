package com.example.booksbazar.ui.addNew;

import static java.lang.Integer.parseInt;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import com.example.booksbazar.Book;
import com.example.booksbazar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddNewFragment extends Fragment {

    private AddNewViewModel mViewModel;
    private TextView newTitleInput;
    private TextView newAuthorInput;
    private EditText newPriceInput;
    private TextView newImageInput;
    private EditText numberOfPagesInput;
    private Button addNewBook;
    private RatingBar ratingBar;
    private  AutoCompleteTextView genreDropdown;
    private AutoCompleteTextView conditionDropdown;


    public static AddNewFragment newInstance() {
        return new AddNewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_new, container, false);

        newTitleInput = root.findViewById(R.id.newTitleInput);
        newAuthorInput = root.findViewById(R.id.newAuthorInput);
        newPriceInput = root.findViewById(R.id.newPriceInput);
        newImageInput = root.findViewById(R.id.newImageInput);
        numberOfPagesInput = root.findViewById(R.id.numberOfPagesInput);
        ratingBar = root.findViewById(R.id.ratingBar);
        addNewBook = root.findViewById(R.id.addNewBookBtn);


        genreDropdown = root.findViewById(R.id.genreDropdown);
        conditionDropdown = root.findViewById(R.id.conditionDropdown);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.genres_array, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapterCondition = ArrayAdapter.createFromResource(getContext(), R.array.condition_array, android.R.layout.simple_spinner_dropdown_item);

        genreDropdown.setAdapter(adapter);
        conditionDropdown.setAdapter(adapterCondition);

        addNewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = newTitleInput.getText().toString().trim();
                String author = newAuthorInput.getText().toString().trim();
                String priceStr = newPriceInput.getText().toString().trim();
                String imageURL = newImageInput.getText().toString().trim();
                String pagesStr = numberOfPagesInput.getText().toString().trim();
                String genre = genreDropdown.getText().toString().trim();
                String condition = conditionDropdown.getText().toString().trim();
                Float rating = ratingBar.getRating();

                if (title.isEmpty() || author.isEmpty() || priceStr.isEmpty() || imageURL.isEmpty()
                        || pagesStr.isEmpty() || genre.isEmpty() || condition.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Integer price = Integer.parseInt(priceStr);
                Integer numOfPages = Integer.parseInt(pagesStr);

                addNewBook(title, author, genre, price, imageURL, numOfPages, condition, rating);
            }
        });

        return root;
    }

    private void addNewBook(String title, String author, String genre, Integer price, String imageURL, Integer numberOfPages, String condition, Float rating){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getUid();

        Book book = new Book(title, author, price, imageURL, userId, genre, condition, numberOfPages, rating);

        db.collection("books").add(book).addOnSuccessListener(documentReference -> {
                    newTitleInput.setText("");
                    newAuthorInput.setText("");
                    newPriceInput.setText("");
                    newImageInput.setText("");
                    ratingBar.setRating(0);
                    numberOfPagesInput.setText("");
                    genreDropdown.setText("");
                    conditionDropdown.setText("");

                    Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }
}