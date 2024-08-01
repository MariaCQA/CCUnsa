package com.example.ccunsa.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.example.ccunsa.R;
import com.example.ccunsa.viewmodel.PinturaViewModel;

public class PinturaFragment extends Fragment {
    private TextView title, description;
    private ImageView image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pintura, container, false);
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
        image = view.findViewById(R.id.image);

        // Obtiene el ID de pintura, asegurándose que sea un número válido
        int pinturaId = getArguments() != null ? getArguments().getInt("pinturaId", -1) : -1;

        // Asegúrate de que el ID es válido antes de continuar
        if (pinturaId == -1) {
            title.setText("Pintura no encontrada");
            description.setText("");
            image.setImageResource(R.drawable.placeholder); // Asegúrate de tener una imagen de marcador de posición adecuada
            return view;
        }

        PinturaViewModel.Factory factory = new PinturaViewModel.Factory(requireActivity().getApplication(), pinturaId);
        PinturaViewModel viewModel = new ViewModelProvider(this, factory).get(PinturaViewModel.class);

        viewModel.getPintura().observe(getViewLifecycleOwner(), pintura -> {
            if (pintura != null) {
                title.setText(pintura.getPaintingName());
                description.setText(pintura.getDescription());
                Glide.with(this)
                        .load(pintura.getIconPath())
                        .placeholder(R.drawable.placeholder)
                        .into(image);
            } else {
                title.setText("Pintura no encontrada");
                description.setText("");
                image.setImageResource(R.drawable.placeholder); // Asegúrate de tener una imagen de marcador de posición adecuada
            }
        });

        return view;
    }
}
