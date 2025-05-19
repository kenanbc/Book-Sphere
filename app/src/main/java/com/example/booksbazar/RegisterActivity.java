package com.example.booksbazar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.booksbazar.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private EditText firstNameInput;
    private EditText lastNameInput;
    private AutoCompleteTextView cityInput;
    private EditText emailInput;
    private EditText passwordInput;
    private CheckBox terms;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firstNameInput = findViewById(R.id.firstNameInputID);
        lastNameInput = findViewById(R.id.lastNameInputID);
        cityInput = findViewById(R.id.locationDropdown);
        emailInput = findViewById(R.id.emailInputID);
        passwordInput = findViewById(R.id.passwordInputRegID);
        terms = findViewById(R.id.termsID);
        registerBtn = findViewById(R.id.registerBtnID);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.location_array, android.R.layout.simple_spinner_dropdown_item);

        cityInput.setAdapter(adapter);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = firstNameInput.getText().toString().trim();
                String lastName = lastNameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String city = cityInput.getText().toString().trim();

                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
                        password.isEmpty() || city.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.fillFields), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!terms.isChecked()) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.pleaseTermsAndConditions), Toast.LENGTH_SHORT).show();
                    return;
                }

                registerUser(firstName, lastName, email, password, city);
            }
        });

    }

    private void registerUser(String firstName, String lastName, String email, String password, String city) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        String userId = mAuth.getCurrentUser().getUid();

                        User user = new User(firstName, lastName, email, city);

                        db.collection("users").document(userId)
                                .set(user)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(getApplicationContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                                    changeToLogin();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void changeToLogin(){
        Intent intent = new Intent(RegisterActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();
    }

}