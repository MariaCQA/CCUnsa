package com.example.ccunsa.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.example.ccunsa.model.Pintura;

import java.util.List;

@Dao
public interface PinturaDao {
    @Query("SELECT * FROM pinturas")
    LiveData<List<Pintura>> getAllPinturas();

    @Query("SELECT * FROM pinturas WHERE id = :id")
    LiveData<Pintura> getPinturaById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Pintura pintura);

    @Update
    void update(Pintura pintura);

    @Delete
    void delete(Pintura pintura);
}
