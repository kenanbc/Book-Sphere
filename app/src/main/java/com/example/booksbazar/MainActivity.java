package com.example.booksbazar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.booksbazar.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

//        binding.appBarMain.addNewButtonID.setOnClickListener(view -> {
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null)
//                    .setAnchorView(R.id.addNewButtonID)
//                    .show();
//        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_messages, R.id.nav_edit, R.id.nav_saved, R.id.nav_addNew)
                .setOpenableLayout(drawer)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.navNameSurnameID);
        TextView navLocation = headerView.findViewById(R.id.navLocationID);
        ImageButton profileBtn = headerView.findViewById(R.id.drawerProfileImageBtn);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (currentUser != null) {

            db.collection("users").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            User userInfo = task.getResult().toObject(User.class);
                            if (userInfo != null) {
                                navUsername.setText(userInfo.firstName + " " + userInfo.lastName);
                                navLocation.setText(userInfo.location);
                            }
                        } else {
                            Log.e("Firestore", "Error fetching user data: ", task.getException());
                        }
                    });
        }

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);

                MenuItem profileItem = binding.navView.getMenu().findItem(R.id.nav_profile);
                NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                NavigationUI.onNavDestinationSelected(profileItem, navController);
            }
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_logout) {
                db.collection("users").document(userId)
                        .update("status", "Offline");
                FirebaseAuth.getInstance().signOut();
                SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();
                startActivity(new Intent(MainActivity.this, LogInActivity.class));
                finish();
                return true;
            }

            drawer.closeDrawer(GravityCompat.START);

            return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);

        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
