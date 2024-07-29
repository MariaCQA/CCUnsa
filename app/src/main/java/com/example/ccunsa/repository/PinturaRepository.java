package com.example.ccunsa.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.ccunsa.database.AppDatabase;
import com.example.ccunsa.database.PinturaDao;
import com.example.ccunsa.model.Pintura;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PinturaRepository {

    private PinturaDao pinturaDao;
    private LiveData<List<Pintura>> allPinturas;
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public PinturaRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        pinturaDao = db.pinturaDao();
        allPinturas = pinturaDao.getAllPinturas();
    }

    public LiveData<List<Pintura>> getAllPinturas() {
        return allPinturas;
    }

    public LiveData<Pintura> getPinturaById(int id) {
        return pinturaDao.getPinturaById(id);
    }

    public void insert(Pintura pintura) {
        executorService.execute(() -> {
            pinturaDao.insert(pintura);
        });
    }

    public void update(Pintura pintura) {
        executorService.execute(() -> {
            pinturaDao.update(pintura);
        });
    }

    public void delete(Pintura pintura) {
        executorService.execute(() -> {
            pinturaDao.delete(pintura);
        });
    }
}
