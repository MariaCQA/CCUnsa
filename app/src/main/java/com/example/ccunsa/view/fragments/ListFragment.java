package com.example.ccunsa.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ccunsa.R;
import com.example.ccunsa.adapter.PinturaAdapter;
import com.example.ccunsa.model.Pintura;
import com.example.ccunsa.viewmodel.PinturaViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    private PinturaViewModel pinturaViewModel;
    private PinturaAdapter pinturaAdapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        SearchView searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pinturaAdapter = new PinturaAdapter(getContext(), new ArrayList<>(), this::onPinturaClick);
        recyclerView.setAdapter(pinturaAdapter);

        pinturaViewModel = new ViewModelProvider(this).get(PinturaViewModel.class);
        pinturaViewModel.getFilteredPaintings().observe(getViewLifecycleOwner(), new Observer<List<Pintura>>() {
            @Override
            public void onChanged(List<Pintura> pinturas) {
                if (pinturas != null) {
                    pinturaAdapter.updatePinturas(pinturas);
                }
            }
        });

        setupSearchView(searchView);

        return view;
    }

    private void setupSearchView(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                pinturaViewModel.setSearchQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pinturaViewModel.setSearchQuery(newText);
                return false;
            }
        });
    }

    private void onPinturaClick(Pintura pintura) {
        // Navegar al fragmento PinturaFragment con el ID de la pintura
        NavController navController = NavHostFragment.findNavController(this);
        Bundle args = new Bundle();
        args.putInt("pinturaId", pintura.getId());
        navController.navigate(R.id.action_listFragment_to_pinturaFragment, args);
    }
}
