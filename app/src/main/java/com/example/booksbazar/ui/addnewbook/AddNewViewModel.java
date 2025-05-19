package com.example.booksbazar.ui.addnewbook;



import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.booksbazar.model.Book;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddNewViewModel extends ViewModel {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private final MutableLiveData<Boolean> addSuccess = new MutableLiveData<>();

    public LiveData<Boolean> getAddSuccess() {
        return addSuccess;
    }

    public void addNewBook(String title, String author, String genre, int price, String imageURL,
                           int numberOfPages, String condition, float rating) {

        String userId = auth.getCurrentUser() != null ? auth.getUid() : null;

        if (userId == null) {
            return;
        }

        Book book = new Book(title, author, price, imageURL, userId, genre, condition, numberOfPages, rating);

        db.collection("books").add(book)
                .addOnSuccessListener(docRef -> addSuccess.setValue(true));
    }
}
