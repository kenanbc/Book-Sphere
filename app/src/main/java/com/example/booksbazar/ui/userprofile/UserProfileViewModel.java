package com.example.booksbazar.ui.userprofile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.booksbazar.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfileViewModel extends ViewModel {
    private final MutableLiveData<String> fullName = new MutableLiveData<>();
    private final MutableLiveData<String> location = new MutableLiveData<>();
    private final MutableLiveData<String> status = new MutableLiveData<>();

    public LiveData<String> getFullName() {
        return fullName;
    }

    public LiveData<String> getLocation() {
        return location;
    }

    public LiveData<String> getStatus() {
        return status;
    }

    public void loadUserData(String userId) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            User userInfo = task.getResult().toObject(User.class);
                            if (userInfo != null) {

                                fullName.setValue(userInfo.firstName + " " + userInfo.lastName);
                                location.setValue(userInfo.location);
                                status.setValue(userInfo.status);
                            }
                        }
                    });
        }
    }
}