package com.example.ccunsa.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;

import com.example.ccunsa.view.fragments.QrFragment;
import com.example.ccunsa.R;
import com.example.ccunsa.databinding.ActivityMainBinding;
import com.example.ccunsa.view.fragments.FavoriteFragment;
import com.example.ccunsa.view.fragments.HomeFragment;
import com.example.ccunsa.view.fragments.SearchFragment;
import com.example.ccunsa.view.fragments.SettingFragment;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if(itemId== R.id.home){
                replaceFragment(new HomeFragment());
            }else if(itemId== R.id.search) {
                replaceFragment(new SearchFragment());
            }else if(itemId== R.id.qr){
                replaceFragment(new QrFragment());
            }else if(itemId== R.id.favorite){
                replaceFragment(new FavoriteFragment());
            }else if(itemId== R.id.setting) {
                replaceFragment(new SettingFragment());
            }
//HOLA
            return true;
        });
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}