package com.example.ccunsa.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.ccunsa.database.AppDatabase;
import com.example.ccunsa.model.Pintura;
import com.example.ccunsa.database.PinturaDao;

import java.util.List;

public class PinturaRepository {
    private PinturaDao pinturaDao;
    private LiveData<List<Pintura>> allPaintings;

    public PinturaRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        pinturaDao = db.pinturaDao();
        allPaintings = pinturaDao.getAllPaintings();
    }

    public LiveData<List<Pintura>> getAllPaintings() {
        return allPaintings;
    }

    public LiveData<List<Pintura>> getPaintingsForGallery(String galleryName) {
        Log.d("PinturaRepository", "getPaintingsForGallery: " + galleryName);
        return pinturaDao.getPaintingsForGallery(galleryName);
    }

    public LiveData<Pintura> getPinturaById(int pinturaId) {
        return pinturaDao.getPinturaById(pinturaId);
    }

    public LiveData<List<Pintura>> getFilteredPaintings(String query, String galleryName, String authorName) {
        return pinturaDao.getFilteredPaintings(query, galleryName, authorName);
    }
    public LiveData<List<Pintura>> getPaintingsByQuery(String query) {
        return pinturaDao.getPaintingsByName(query);
    }
    
}
