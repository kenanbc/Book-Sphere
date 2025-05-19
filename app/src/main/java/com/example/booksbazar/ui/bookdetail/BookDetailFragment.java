package com.example.booksbazar.ui.bookdetail;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.booksbazar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class BookDetailFragment extends Fragment {

    private BookDetailViewModel mViewModel;
    private TextView nameTextView;
    private TextView locationTextView;
    private String userId;
    private String bookId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_detail, container, false);

        mViewModel = new ViewModelProvider(this).get(BookDetailViewModel.class);

        TextView titleTextView = view.findViewById(R.id.titleDetail);
        TextView authorTextView = view.findViewById(R.id.authorDetail);
        ImageView bookImageView = view.findViewById(R.id.imageDetail);
        TextView priceTextView = view.findViewById(R.id.priceDetail);
        TextView genreTextView = view.findViewById(R.id.genreBookDetail);
        TextView conditionTextView = view.findViewById(R.id.conditionBookDetail);
        TextView pageNumber = view.findViewById(R.id.numOfPagesDetail);
        TextView ratingDetail = view.findViewById(R.id.ratingDetail);
        nameTextView = view.findViewById(R.id.nameBookDetail);
        locationTextView = view.findViewById(R.id.locationBookDetail);
        Button sendMessageBtn = view.findViewById(R.id.messageBookDetailBtn);
        Button saveBtn = view.findViewById(R.id.saveBookDetail);
        ImageButton openProfileBtn = view.findViewById(R.id.openProfileBtn);

        Bundle bundle = getArguments();
        if (bundle != null) {
            bookId = bundle.getString("bookId");
            userId = bundle.getString("userId");

            mViewModel.fetchBook(bookId);
            mViewModel.fetchUser(userId);
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = currentUser != null ? currentUser.getUid() : null;
        boolean isOwner = currentUserId != null && currentUserId.equals(userId);

        if (isOwner) {
            sendMessageBtn.setText(getString(R.string.edit));
            saveBtn.setText(getString(R.string.delete));

            sendMessageBtn.setOnClickListener(v -> {
                Bundle editBundle = new Bundle();
                editBundle.putString("bookId", bookId);

                NavController navController = NavHostFragment.findNavController(BookDetailFragment.this);
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.bookDetailFragment, true)
                        .build();
                navController.navigate(R.id.editBookFragment, bundle, navOptions);
            });

            saveBtn.setOnClickListener(v -> {
                new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.bookDelete))
                        .setMessage(getString(R.string.bookDeleteValidate))
                        .setPositiveButton(getString(R.string.yesValidation), (dialog, which) -> {
                            mViewModel.deleteBook(bookId, () -> {
                                Toast.makeText(getContext(), getString(R.string.deletedSuccess), Toast.LENGTH_SHORT).show();
                                NavHostFragment.findNavController(this)
                                        .navigate(R.id.action_bookDetailFragment_to_nav_home);
                            });
                        })
                        .setNegativeButton(getString(R.string.noValidation), null)
                        .show();
            });

        } else {
            mViewModel.checkIfBookIsSaved(bookId, currentUserId);

            saveBtn.setOnClickListener(v -> {
                mViewModel.toggleSaveBook(bookId, currentUserId);
            });

            openProfileBtn.setOnClickListener(v -> {
                Bundle profileBundle = new Bundle();
                profileBundle.putString("userId", userId);
                NavController navController = NavHostFragment.findNavController(this);
                navController.navigate(R.id.nav_userProfile, profileBundle);
            });

            sendMessageBtn.setOnClickListener(v -> {
                Bundle chatBundle = new Bundle();
                chatBundle.putString("userId", userId);
                NavController navController = NavHostFragment.findNavController(this);
                navController.navigate(R.id.fragment_chat, chatBundle);
            });
        }

        mViewModel.getBook().observe(getViewLifecycleOwner(), book -> {
            titleTextView.setText(book.getTitle());
            authorTextView.setText(book.getAuthor());
            priceTextView.setText(book.getPrice() + " KM");
            genreTextView.setText(book.getGenre());
            conditionTextView.setText(book.getCondition());
            pageNumber.setText(String.valueOf(book.getNumberOfPages()));
            ratingDetail.setText(book.getRating() + "/5.0");

            Glide.with(this)
                    .load(book.getImageUrl())
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(24, 0)))
                    .into(bookImageView);
        });

        mViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            nameTextView.setText(user.getFirstName() + " " + (user.getLastName() != null ? user.getLastName() : ""));
            locationTextView.setText(user.getLocation());
        });

        mViewModel.getIsSaved().observe(getViewLifecycleOwner(), saved -> {
            saveBtn.setText(saved ? getString(R.string.unsaveBook) : getString(R.string.saveBook));
        });

        return view;
    }
}
