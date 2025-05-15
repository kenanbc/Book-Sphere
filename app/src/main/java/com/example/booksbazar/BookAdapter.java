package com.example.booksbazar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> implements Filterable {

    private Context context;
    private List<Book> bookList;
    private List<Book> bookListFull;
    private int navActionId;

    public BookAdapter(Context context, List<Book> bookList, int navActionId) {
        this.context = context;
        this.bookList = bookList;
        this.navActionId = navActionId;
        this.bookListFull = new ArrayList<>(bookList);
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);

        holder.bookTitle.setText(book.getTitle());
        holder.bookAuthor.setText(book.getAuthor());
        holder.bookPrice.setText(book.getPrice() + " KM");

        Glide.with(context)
                .load(book.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.bookImage);

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("bookTitle", book.getTitle());
            bundle.putString("bookAuthor", book.getAuthor());
            bundle.putString("bookImage", book.getImageUrl());
            bundle.putInt("bookPrice", book.getPrice());
            bundle.putString("bookGenre", book.getGenre());
            bundle.putString("bookCondition", book.getCondition());
            bundle.putString("bookId", book.getBookId());
            bundle.putString("userId", book.getUserId());
            bundle.putInt("numberOfPages", book.getNumberOfPages());
            if (book.getRating() != null) {
                bundle.putFloat("rating", book.getRating());
            } else {
                bundle.putFloat("rating", 4.5F);
            }

            NavController navController = Navigation.findNavController(v);
            navController.navigate(navActionId, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    @Override
    public Filter getFilter() {
        return bookFilter;
    }

    private final Filter bookFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Book> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(bookListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Book book : bookListFull) {
                    if (book.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(book);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            bookList.clear();
            bookList.addAll((List<Book>) results.values);
            notifyDataSetChanged();
        }
    };

    public void updateBooks(List<Book> newBooks) {
        bookList.clear();
        bookList.addAll(newBooks);

        if (bookListFull == null) {
            bookListFull = new ArrayList<>(newBooks);
        } else {
            bookListFull.clear();
            bookListFull.addAll(newBooks);
        }

        notifyDataSetChanged();
    }



    public static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImage;
        TextView bookTitle, bookAuthor, bookPrice;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.bookImage);
            bookTitle = itemView.findViewById(R.id.bookTitle);
            bookAuthor = itemView.findViewById(R.id.bookAuthor);
            bookPrice = itemView.findViewById(R.id.bookPrice);
        }
    }
}

