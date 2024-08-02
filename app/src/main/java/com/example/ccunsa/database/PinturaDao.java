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

    @Query("SELECT * FROM pinturas WHERE (galleryName LIKE '%' || :galleryName || '%' OR :galleryName IS NULL) AND (authorName LIKE '%' || :authorName || '%' OR :authorName IS NULL) AND (paintingName LIKE '%' || :query || '%' OR :query IS NULL)")
    LiveData<List<Pintura>> getFilteredPaintings(String query, String galleryName, String authorName);

    @Insert
    void insert(Pintura... pinturas);

    @Query("DELETE FROM pinturas")
    void deleteAll();

    @Query("SELECT * FROM pinturas WHERE paintingName LIKE '%' || :query || '%'")
    LiveData<List<Pintura>> getPaintingsByName(String query);

}
