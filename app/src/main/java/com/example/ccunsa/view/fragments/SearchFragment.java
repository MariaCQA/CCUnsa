package com.example.ccunsa.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ccunsa.R;
import com.example.ccunsa.adapter.PinturaAdapter;
import com.example.ccunsa.model.Pintura;
import com.example.ccunsa.viewmodel.PinturaViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private PinturaViewModel pinturaViewModel;
    private PinturaAdapter pinturaAdapter;
    private TextView roomNameTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        roomNameTextView = root.findViewById(R.id.room_name);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        pinturaAdapter = new PinturaAdapter(getContext(), new ArrayList<>(), pintura -> {
            Bundle bundle = new Bundle();
            bundle.putInt("pinturaId", pintura.getId());
            // Navegar al fragmento PinturaFragment
            Navigation.findNavController(root).navigate(R.id.action_searchFragment_to_pinturaFragment, bundle);
        });
        recyclerView.setAdapter(pinturaAdapter);

        // Obtener el ID de pintura de los argumentos
        int pinturaId = getArguments() != null ? getArguments().getInt("pinturaId", -1) : -1;

        // Usar la Factory para crear el ViewModel
        PinturaViewModel.Factory factory = new PinturaViewModel.Factory(getActivity().getApplication(), pinturaId);
        pinturaViewModel = new ViewModelProvider(this, factory).get(PinturaViewModel.class);

        if (getArguments() != null) {
            String roomName = getArguments().getString("roomName");
            roomNameTextView.setText(roomName);
            Log.d("SearchFragment", "Room Name: " + roomName);

            pinturaViewModel.getPaintingsForGallery(roomName).observe(getViewLifecycleOwner(), pinturas -> {
                Log.d("SearchFragment", "Number of paintings: " + pinturas.size());
                for (Pintura pintura : pinturas) {
                    Log.d("SearchFragment", "Pintura: " + pintura.getPaintingName() + ", IconPath: " + pintura.getIconPath());
                }
                pinturaAdapter.updatePinturas(pinturas);
            });
        }

        return root;
    }
}
