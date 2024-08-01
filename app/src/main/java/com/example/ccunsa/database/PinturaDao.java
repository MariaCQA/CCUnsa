package com.example.ccunsa.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.ccunsa.model.Pintura;
import java.util.List;

@Dao
public interface PinturaDao {

    @Query("SELECT * FROM pinturas")
    LiveData<List<Pintura>> getAllPaintings();

    @Query("SELECT * FROM pinturas WHERE galleryName = :galleryName")
    LiveData<List<Pintura>> getPaintingsForGallery(String galleryName);

    @Query("SELECT * FROM pinturas WHERE id = :pinturaId")
    LiveData<Pintura> getPinturaById(int pinturaId);

    @Insert
    void insert(Pintura... pinturas);

    @Query("DELETE FROM pinturas")
    void deleteAll();
}
