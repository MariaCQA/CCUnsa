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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ccunsa.R;
import com.example.ccunsa.adapter.PinturaAdapter;
import com.example.ccunsa.model.Pintura;
import com.example.ccunsa.viewmodel.PinturaViewModel;

import java.util.ArrayList;
import java.util.List;

public class RoomDetailFragment extends Fragment {

    private PinturaViewModel pinturaViewModel;
    private PinturaAdapter pinturaAdapter;
    private TextView roomNameTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_room_detail, container, false);

        roomNameTextView = root.findViewById(R.id.room_name);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        pinturaAdapter = new PinturaAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(pinturaAdapter);

        pinturaViewModel = new ViewModelProvider(this).get(PinturaViewModel.class);
        if (getArguments() != null) {
            String roomName = getArguments().getString("roomName");
            roomNameTextView.setText(roomName);
            Log.d("RoomDetailFragment", "Room Name: " + roomName);

            pinturaViewModel.getPaintingsForGallery(roomName).observe(getViewLifecycleOwner(), new Observer<List<Pintura>>() {
                @Override
                public void onChanged(List<Pintura> pinturas) {
                    Log.d("RoomDetailFragment", "Number of paintings: " + pinturas.size());
                    for (Pintura pintura : pinturas) {
                        Log.d("RoomDetailFragment", "Pintura: " + pintura.getPaintingName() + ", IconPath: " + pintura.getIconPath());
                    }
                    pinturaAdapter.updatePinturas(pinturas);
                }
            });
        }

        return root;
    }
}
