package com.example.ccunsa.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ccunsa.R;
import com.example.ccunsa.model.Pintura;
import com.example.ccunsa.viewmodel.PinturaViewModel;

import java.util.List;

public class RoomDetailFragment extends Fragment {

    private PinturaViewModel pinturaViewModel;
    private TextView roomNameTextView;
    private GridLayout gridLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_room_detail, container, false);

        roomNameTextView = root.findViewById(R.id.room_name);
        gridLayout = root.findViewById(R.id.gridLayout);

        if (getArguments() != null) {
            String roomName = getArguments().getString("roomName");
            roomNameTextView.setText(roomName);
            Log.d("RoomDetailFragment", "Room Name: " + roomName);

            pinturaViewModel = new ViewModelProvider(this).get(PinturaViewModel.class);
            pinturaViewModel.getPaintingsForGallery(roomName).observe(getViewLifecycleOwner(), new Observer<List<Pintura>>() {
                @Override
                public void onChanged(List<Pintura> pinturas) {
                    Log.d("RoomDetailFragment", "Number of paintings: " + pinturas.size());
                    gridLayout.removeAllViews(); // Limpiar las vistas existentes
                    int count = Math.min(pinturas.size(), 8); // Limitar a los primeros 8
                    for (int i = 0; i < count; i++) {
                        Pintura pintura = pinturas.get(i);
                        TextView textView = new TextView(getContext());
                        textView.setText(pintura.getPaintingName());
                        GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                                GridLayout.spec(i / 2),  // fila
                                GridLayout.spec(i % 2)   // columna
                        );
                        params.width = GridLayout.LayoutParams.WRAP_CONTENT;
                        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                        textView.setLayoutParams(params);
                        gridLayout.addView(textView);
                    }
                }
            });
        } else {
            throw new IllegalArgumentException("Room Name no proporcionado");
        }

        return root;
    }
}
