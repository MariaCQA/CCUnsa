package com.example.ccunsa.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.ccunsa.model.Pintura;
import com.example.ccunsa.repository.PinturaRepository;
import java.util.List;

public class PinturaViewModel extends AndroidViewModel {
    private PinturaRepository repository;
    private LiveData<Pintura> pintura;

    public PinturaViewModel(@NonNull Application application, int pinturaId) {
        super(application);
        repository = new PinturaRepository(application);
        if (pinturaId != -1) {
            pintura = repository.getPinturaById(pinturaId);
        } else {
            pintura = new MutableLiveData<>(null); // En caso de que no haya ID válido
        }
    }

    public LiveData<Pintura> getPintura() {
        return pintura;
    }

    public LiveData<List<Pintura>> getAllPinturas() {
        return repository.getAllPinturas();
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

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final Application app;
        private final int pinturaId;

        public Factory(Application app, int pinturaId) {
            this.app = app;
            this.pinturaId = pinturaId;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new PinturaViewModel(app, pinturaId);
        }
    }
}
