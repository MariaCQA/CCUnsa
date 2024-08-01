//package com.example.ccunsa.view.fragments;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView; // Importar correctamente desde androidx.recyclerview.widget
//import com.example.ccunsa.R;
//import com.example.ccunsa.adapter.PinturaAdapter;
//import com.example.ccunsa.model.Pintura;
//import com.example.ccunsa.viewmodel.PinturaViewModel;
//import java.util.List;
//
//public class ListFragment extends Fragment {
//
//    private PinturaViewModel pinturaViewModel;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_list, container, false);
//
//        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        final PinturaAdapter adapter = new PinturaAdapter();
//        recyclerView.setAdapter(adapter);
//
//        pinturaViewModel = new ViewModelProvider(this).get(PinturaViewModel.class);
//        pinturaViewModel.getAllPinturas().observe(getViewLifecycleOwner(), new Observer<List<Pintura>>() {
//            @Override
//            public void onChanged(@Nullable List<Pintura> pinturas) {
//                adapter.setPinturas(pinturas);
//            }
//        });
//
//        return view;
//    }
//}
