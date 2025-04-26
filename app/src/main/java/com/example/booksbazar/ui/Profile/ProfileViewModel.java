package com.example.booksbazar.ui.Profile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.booksbazar.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<String> fullName = new MutableLiveData<>();
    private final MutableLiveData<String> location = new MutableLiveData<>();

    public LiveData<String> getFullName() {
        return fullName;
    }

    public LiveData<String> getLocation() {
        return location;
    }

    public void loadUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            User userInfo = task.getResult().toObject(User.class);
                            if (userInfo != null) {

                                fullName.setValue(userInfo.firstName + " " + userInfo.lastName);
                                location.setValue(userInfo.location);
                            }
                        } else {
                            Log.e("Firestore", "Error fetching user data: ", task.getException());
                        }
                    });
        }
    }

}
