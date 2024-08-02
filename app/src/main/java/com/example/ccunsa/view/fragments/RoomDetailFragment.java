package com.example.ccunsa.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ccunsa.R;
import com.example.ccunsa.model.Pintura;
import com.example.ccunsa.view.PaintingCanvasView;
import com.example.ccunsa.viewmodel.PinturaViewModel;

import java.util.List;

public class RoomDetailFragment extends Fragment {

    private PinturaViewModel pinturaViewModel;
    private PaintingCanvasView paintingCanvasView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_room_detail, container, false);

        paintingCanvasView = root.findViewById(R.id.paintingCanvasView);

        if (getArguments() != null) {
            String roomName = getArguments().getString("roomName");
            Log.d("RoomDetailFragment", "Room Name: " + roomName);

            // Obtén el ID de la pintura (o maneja el ID de otra manera)
            int pinturaId = getArguments().getInt("pinturaId", -1);

            // Usa la fábrica personalizada para crear el ViewModel
            PinturaViewModel.Factory factory = new PinturaViewModel.Factory(requireActivity().getApplication(), pinturaId);
            pinturaViewModel = new ViewModelProvider(this, factory).get(PinturaViewModel.class);

            pinturaViewModel.getPaintingsForGallery(roomName).observe(getViewLifecycleOwner(), new Observer<List<Pintura>>() {
                @Override
                public void onChanged(List<Pintura> pinturas) {
                    Log.d("RoomDetailFragment", "Number of paintings: " + pinturas.size());
                    paintingCanvasView.setPinturas(pinturas.subList(0, Math.min(pinturas.size(), 8))); // Limitar a los primeros 8
                }
            });
        } else {
            throw new IllegalArgumentException("Room Name no proporcionado");
        }

        return root;
    }
}
