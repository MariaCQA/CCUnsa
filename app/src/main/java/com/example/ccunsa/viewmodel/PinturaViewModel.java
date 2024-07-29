package com.example.ccunsa.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.ccunsa.model.Pintura;
import com.example.ccunsa.repository.PinturaRepository;
import java.util.List;

public class PinturaViewModel extends AndroidViewModel {

    private PinturaRepository repository;
    private LiveData<List<Pintura>> allPinturas;

    public PinturaViewModel(@NonNull Application application) {
        super(application);
        repository = new PinturaRepository(application);
        allPinturas = repository.getAllPinturas();
    }

    public LiveData<List<Pintura>> getAllPinturas() {
        return allPinturas;
    }

    public void insert(Pintura pintura) {
        repository.insert(pintura);
    }

    public void update(Pintura pintura) {
        repository.update(pintura);
    }

    public void delete(Pintura pintura) {
        repository.delete(pintura);
    }
}
