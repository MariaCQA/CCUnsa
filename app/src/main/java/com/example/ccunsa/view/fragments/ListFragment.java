package com.example.ccunsa.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ccunsa.R;
import com.example.ccunsa.adapter.PinturaAdapter;
import com.example.ccunsa.model.Pintura;
import com.example.ccunsa.viewmodel.PinturaViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    private static final String TAG = "ListFragment";
    private PinturaViewModel pinturaViewModel;
    private PinturaAdapter pinturaAdapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        SearchView searchView = view.findViewById(R.id.searchView);
        Spinner gallerySpinner = view.findViewById(R.id.gallerySpinner);
        Spinner authorSpinner = view.findViewById(R.id.authorSpinner);
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
        setupSpinners(gallerySpinner, authorSpinner);

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

    private void setupSpinners(Spinner gallerySpinner, Spinner authorSpinner) {
        // Setup gallery spinner
        ArrayAdapter<CharSequence> galleryAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.gallery_array, android.R.layout.simple_spinner_item);
        galleryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gallerySpinner.setAdapter(galleryAdapter);

        // Setup author spinner
        ArrayAdapter<CharSequence> authorAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.author_array, android.R.layout.simple_spinner_item);
        authorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        authorSpinner.setAdapter(authorAdapter);

        gallerySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGallery = parent.getItemAtPosition(position).toString();
                pinturaViewModel.setSelectedGallery(selectedGallery);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                pinturaViewModel.setSelectedGallery(null);
            }
        });

        authorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedAuthor = parent.getItemAtPosition(position).toString();
                pinturaViewModel.setSelectedAuthor(selectedAuthor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                pinturaViewModel.setSelectedAuthor(null);
            }
        });
    }

    private void onPinturaClick(Pintura pintura) {
        // Navegar al fragmento PinturaFragment con el ID de la pintura
        FragmentActivity activity = requireActivity();
        NavHostFragment navHostFragment = (NavHostFragment) activity.getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            Bundle args = new Bundle();
            args.putInt("pinturaId", pintura.getId());
            navController.navigate(R.id.action_listFragment_to_pinturaFragment, args);
        }
    }

}
