package com.example.ccunsa.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ccunsa.model.Pintura;
import com.example.ccunsa.repository.PinturaRepository;

import java.util.List;

public class PinturaViewModel extends AndroidViewModel {
    private PinturaRepository repository;
    private LiveData<List<Pintura>> allPaintings;

    public PinturaViewModel(Application application) {
        super(application);
        repository = new PinturaRepository(application);
        allPaintings = repository.getAllPaintings();
    }

    public LiveData<List<Pintura>> getAllPaintings() {
        return allPaintings;
    }

    public LiveData<List<Pintura>> getPaintingsForGallery(String galleryName) {
        Log.d("PinturaViewModel", "getPaintingsForGallery: " + galleryName);
        return repository.getPaintingsForGallery(galleryName);
    }
}
