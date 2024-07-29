package com.example.ccunsa.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ccunsa.model.Pintura;

import java.util.List;

@Dao
public interface PinturaDao {
    @Query("SELECT * FROM pinturas")
    LiveData<List<Pintura>> getAllPinturas();

    @Insert
    void insert(Pintura pintura);

    @Update
    void update(Pintura pintura);

    @Delete
    void delete(Pintura pintura);
}
