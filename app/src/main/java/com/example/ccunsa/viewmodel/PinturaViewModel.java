package com.example.ccunsa.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ccunsa.model.Pintura;
import com.example.ccunsa.repository.PinturaRepository;

import java.util.List;

public class PinturaViewModel extends AndroidViewModel {
    private PinturaRepository repository;
    private LiveData<Pintura> pintura;
    private LiveData<List<Pintura>> paintingsForGallery;

    public PinturaViewModel(@NonNull Application application, int pinturaId) {
        super(application);
        repository = new PinturaRepository(application);
        pintura = repository.getPinturaById(pinturaId);
    }

    public PinturaViewModel(@NonNull Application application) {
        super(application);
        repository = new PinturaRepository(application);
    }

    public LiveData<Pintura> getPintura() {
        return pintura;
    }

    public LiveData<List<Pintura>> getPaintingsForGallery(String galleryName) {
        paintingsForGallery = repository.getPaintingsForGallery(galleryName);
        return paintingsForGallery;
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
