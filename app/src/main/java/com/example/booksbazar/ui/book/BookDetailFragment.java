package com.example.booksbazar;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class BookDetailFragment extends Fragment {

    private BookDetailViewModel mViewModel;

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

        Bundle bundle = getArguments();
        if (bundle != null) {
            titleTextView.setText(bundle.getString("bookTitle"));
            authorTextView.setText(bundle.getString("bookAuthor"));
            priceTextView.setText(bundle.getString("bookPrice") + " KM");

            Glide.with(this)
                    .load(bundle.getString("bookImage"))
                    .into(bookImageView);
        }

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BookDetailViewModel.class);
        // TODO: Use the ViewModel
    }

}