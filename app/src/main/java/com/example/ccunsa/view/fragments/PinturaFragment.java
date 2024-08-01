//package com.example.ccunsa.view.fragments;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//import com.bumptech.glide.Glide;
//import com.example.ccunsa.R;
//import com.example.ccunsa.viewmodel.PinturaViewModel;
//import com.example.ccunsa.model.Pintura;
//
//public class PinturaFragment extends Fragment {
//    private TextView title, description;
//    private ImageView image;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_pintura, container, false);
//        title = view.findViewById(R.id.title);
//        description = view.findViewById(R.id.description);
//        image = view.findViewById(R.id.image);
//
//        // Obtiene el ID de pintura, asegurándose que sea un número válido
//        int pinturaId = Integer.parseInt(getArguments().getString("pinturaId", "-1"));
//        PinturaViewModel.Factory factory = new PinturaViewModel.Factory(getActivity().getApplication(), pinturaId);
//        PinturaViewModel viewModel = new ViewModelProvider(this, factory).get(PinturaViewModel.class);
//
//        viewModel.getPintura().observe(getViewLifecycleOwner(), pintura -> {
//            if (pintura != null) {
//                title.setText(pintura.getTitulo());
//                description.setText(pintura.getDescripcion());
//                Glide.with(this)
//                        .load(pintura.getImagen())
//                        .placeholder(R.drawable.placeholder)
//                        .into(image);
//            } else {
//                title.setText("Pintura no encontrada");
//                description.setText("");
//                image.setImageResource(R.drawable.placeholder); // Asegúrate de tener una imagen de marcador de posición adecuada
//            }
//        });
//
//        return view;
//    }
//}
