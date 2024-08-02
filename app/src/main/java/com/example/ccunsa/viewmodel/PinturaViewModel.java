package com.example.ccunsa.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ccunsa.model.Pintura;
import com.example.ccunsa.repository.PinturaRepository;

import java.util.List;

public class PinturaViewModel extends AndroidViewModel {
    private PinturaRepository repository;
    private LiveData<List<Pintura>> allPaintings;
    private MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private MutableLiveData<String> selectedGallery = new MutableLiveData<>();
    private MutableLiveData<String> selectedAuthor = new MutableLiveData<>();
    private LiveData<List<Pintura>> paintingsForGallery;
    private LiveData<Pintura> pintura;

    public PinturaViewModel(@NonNull Application application, int pinturaId) {
        super(application);
        repository = new PinturaRepository(application);
        allPaintings = repository.getAllPaintings();
        pintura = repository.getPinturaById(pinturaId);
    }

    public LiveData<Pintura> getPintura() {
        return pintura;
    }

    public LiveData<List<Pintura>> getFilteredPaintings() {
        return Transformations.switchMap(searchQuery, query -> {
            return Transformations.switchMap(selectedGallery, gallery -> {
                return Transformations.switchMap(selectedAuthor, author -> {
                    return repository.getAllPaintings();
                });
            });
        });
    }

    public LiveData<List<Pintura>> getPaintingsForGallery(String galleryName) {
        paintingsForGallery = repository.getPaintingsForGallery(galleryName);
        return paintingsForGallery;
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public void setSelectedGallery(String gallery) {
        selectedGallery.setValue(gallery);
    }

    public void setSelectedAuthor(String author) {
        selectedAuthor.setValue(author);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final Application application;
        private final int pinturaId;

        public Factory(@NonNull Application application, int pinturaId) {
            this.application = application;
            this.pinturaId = pinturaId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new PinturaViewModel(application, pinturaId);
        }
    }
}
