////////////////
package com.example.ccunsa.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.ccunsa.model.User;

@Dao
public interface UserDao {
    @Insert
    void registerUser(User user);
    @Query("SELECT * FROM user WHERE username = :username AND password = :password LIMIT 1")
    User cancelar(String username, String password);


}
