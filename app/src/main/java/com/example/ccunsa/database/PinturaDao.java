package com.example.ccunsa.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.ccunsa.model.Pintura;

import java.util.List;

@Dao
public interface PinturaDao {
    @Insert
    void insert(Pintura pintura);

    @Query("DELETE FROM pinturas")
    void deleteAll();

    @Query("SELECT * FROM pinturas ORDER BY paintingName ASC")
    LiveData<List<Pintura>> getAllPaintings();

    @Query("SELECT * FROM pinturas WHERE galleryName = :galleryName ORDER BY paintingName ASC")
    LiveData<List<Pintura>> getPaintingsForGallery(String galleryName);
}
