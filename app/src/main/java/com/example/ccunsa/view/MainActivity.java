package com.example.ccunsa.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Database;

import com.example.ccunsa.R;
import com.example.ccunsa.database.AppDatabase;
import com.example.ccunsa.database.PinturaDao;
import com.example.ccunsa.databinding.ActivityMainBinding;
import com.example.ccunsa.model.Pintura;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
        Log.d("asd","test");

        appDatabase = AppDatabase.getDatabase(this);
        appDatabase.insertInitialData();

        LiveData<List<Pintura>> allPaintings = appDatabase.pinturaDao().getAllPaintings();
        allPaintings.observe(this, pinturas -> {
            for (Pintura pintura : pinturas) {
                System.out.println("ID: " + pintura.getId() + ", Name: " + pintura.getPaintingName() + ", Gallery: " + pintura.getGalleryName());
            }
        });


        // Handle FloatingActionButton click
        binding.fab.setOnClickListener(v -> {
            navController.navigate(R.id.QrFragment);
        });
    }

}