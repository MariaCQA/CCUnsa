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
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public PinturaRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        pinturaDao = db.pinturaDao();
        allPinturas = pinturaDao.getAllPinturas();
    }

    public LiveData<List<Pintura>> getAllPinturas() {
        return allPinturas;
    }

    public void insert(Pintura pintura) {
        databaseWriteExecutor.execute(() -> {
            pinturaDao.insert(pintura);
        });
    }

    public void update(Pintura pintura) {
        databaseWriteExecutor.execute(() -> {
            pinturaDao.update(pintura);
        });
    }

    public void delete(Pintura pintura) {
        databaseWriteExecutor.execute(() -> {
            pinturaDao.delete(pintura);
        });
    }
}
