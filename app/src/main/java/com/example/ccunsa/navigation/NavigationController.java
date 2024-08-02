package com.example.ccunsa.navigation;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.example.ccunsa.R;

public class NavigationController {

    private NavController navController;

    public NavigationController(Fragment fragment) {
        navController = NavHostFragment.findNavController(fragment);
    }

    public void navigateToPinturaFragment(int pinturaId) {
        Bundle bundle = new Bundle();
        bundle.putInt("pinturaId", pinturaId);
        navController.navigate(R.id.action_qrFragment_to_pinturaFragment, bundle);
    }

    // Otros métodos de navegación pueden agregarse aquí
}
