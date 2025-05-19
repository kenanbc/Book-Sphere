package com.example.booksbazar.ui.bookdetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.booksbazar.model.Book;
import com.example.booksbazar.model.Saved;
import com.example.booksbazar.model.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BookDetailViewModel extends ViewModel {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final MutableLiveData<Book> bookLiveData = new MutableLiveData<>();
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSaved = new MutableLiveData<>();

    public LiveData<Book> getBook() {
        return bookLiveData;
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public LiveData<Boolean> getIsSaved() {
        return isSaved;
    }

    public void fetchBook(String bookId) {
        db.collection("books").document(bookId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        Book book = snapshot.toObject(Book.class);
                        bookLiveData.setValue(book);
                    }
                });
    }

    public void fetchUser(String userId) {
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        User user = snapshot.toObject(User.class);
                        userLiveData.setValue(user);
                    }
                });
    }

    public void checkIfBookIsSaved(String bookId, String userId) {
        db.collection("saved")
                .whereEqualTo("userId", userId)
                .whereEqualTo("bookId", bookId)
                .get()
                .addOnSuccessListener(snapshot -> isSaved.setValue(!snapshot.isEmpty()));
    }

    public void toggleSaveBook(String bookId, String userId) {
        db.collection("saved")
                .whereEqualTo("userId", userId)
                .whereEqualTo("bookId", bookId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.isEmpty()) {
                        for (DocumentSnapshot doc : snapshot.getDocuments()) {
                            db.collection("saved").document(doc.getId()).delete();
                        }
                        isSaved.setValue(false);
                    } else {
                        db.collection("saved").add(new Saved(userId, bookId))
                                .addOnSuccessListener(ref -> isSaved.setValue(true));
                    }
                });
    }

    public void deleteBook(String bookId, Runnable onSuccess) {
        db.collection("books").document(bookId)
                .delete()
                .addOnSuccessListener(aVoid -> onSuccess.run());
    }
}
