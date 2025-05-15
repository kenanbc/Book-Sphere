package com.example.booksbazar.ui.editProfile;

import androidx.lifecycle.ViewModelProvider;

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
import android.widget.Toast;

import com.example.booksbazar.R;
import com.example.booksbazar.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfileFragment extends Fragment {

    private EditProfileViewModel mViewModel;
    private EditText firstNameInput;
    private EditText lastNameInput;
    private AutoCompleteTextView locationDropdown;
    private Button saveBtn;

    public static EditProfileFragment newInstance() {
        return new EditProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        firstNameInput = root.findViewById(R.id.newFirstNameInput);
        lastNameInput = root.findViewById(R.id.newLastNameInput);
        locationDropdown = root.findViewById(R.id.locationDropdown);
        saveBtn = root.findViewById(R.id.saveProfileBtn);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.location_array, android.R.layout.simple_spinner_dropdown_item);

        locationDropdown.setAdapter(adapter);

        fillInputFields();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = firstNameInput.getText().toString().trim();
                String lastName = lastNameInput.getText().toString().trim();
                String location = locationDropdown.getText().toString().trim();

                if(firstName.isEmpty() || lastName.isEmpty() || location.isEmpty()){
                    Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                updateInformations(firstName, lastName, location);
            }
        });


        return root;
    }

    private void fillInputFields(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getUid();

        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String name = documentSnapshot.getString("firstName");
                String lastName = documentSnapshot.getString("lastName");
                String location = documentSnapshot.getString("location");

                firstNameInput.setText(name);
                lastNameInput.setText(lastName);
                locationDropdown.setText(location, false);
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
        });


    }

    private void updateInformations(String firstName, String lastName, String location){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getUid();
        DocumentReference userRef = db.collection("users").document(userId);

        userRef.update(
                "firstName", firstName,
                "lastName", lastName,
                "location", location
        ).addOnSuccessListener(aVoid -> {
            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }
}