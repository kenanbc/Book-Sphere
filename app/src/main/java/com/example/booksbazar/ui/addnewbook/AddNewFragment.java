package com.example.booksbazar.ui.addnewbook;

import static java.lang.Integer.parseInt;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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

public class AddNewFragment extends Fragment {

    private AddNewViewModel mViewModel;
    private TextView newTitleInput;
    private TextView newAuthorInput;
    private EditText newPriceInput;
    private TextView newImageInput;
    private EditText numberOfPagesInput;
    private Button addNewBook;
    private RatingBar ratingBar;
    private AutoCompleteTextView genreDropdown;
    private AutoCompleteTextView conditionDropdown;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add_new, container, false);

        mViewModel = new ViewModelProvider(this).get(AddNewViewModel.class);

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

        addNewBook.setOnClickListener(view -> {
            String title = newTitleInput.getText().toString().trim();
            String author = newAuthorInput.getText().toString().trim();
            String priceStr = newPriceInput.getText().toString().trim();
            String imageURL = newImageInput.getText().toString().trim();
            String pagesStr = numberOfPagesInput.getText().toString().trim();
            String genre = genreDropdown.getText().toString().trim();
            String condition = conditionDropdown.getText().toString().trim();
            float rating = ratingBar.getRating();

            if (title.isEmpty() || author.isEmpty() || priceStr.isEmpty() || imageURL.isEmpty()
                    || pagesStr.isEmpty() || genre.isEmpty() || condition.isEmpty()) {
                Toast.makeText(getContext(), getString(R.string.fillFields), Toast.LENGTH_SHORT).show();
                return;
            }

            int price = Integer.parseInt(priceStr);
            int numOfPages = Integer.parseInt(pagesStr);

            mViewModel.addNewBook(title, author, genre, price, imageURL, numOfPages, condition, rating);
        });

        mViewModel.getAddSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                clearForm();
                Toast.makeText(getContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }
    private void clearForm() {
        newTitleInput.setText("");
        newAuthorInput.setText("");
        newPriceInput.setText("");
        newImageInput.setText("");
        numberOfPagesInput.setText("");
        ratingBar.setRating(0);
        genreDropdown.setText("");
        conditionDropdown.setText("");
    }
}
