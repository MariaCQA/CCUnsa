package com.example.ccunsa.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.ccunsa.R;
import com.example.ccunsa.view.InteractiveMapView;

public class MapFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        InteractiveMapView interactiveMapView = view.findViewById(R.id.interactiveMapView);

        // Aquí puedes configurar la vista interactiva si es necesario

        return view;
    }
}
