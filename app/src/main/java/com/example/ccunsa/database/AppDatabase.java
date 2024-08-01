package com.example.ccunsa.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.ccunsa.model.Pintura;
import com.example.ccunsa.database.PinturaDao;
import com.example.ccunsa.model.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Pintura.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PinturaDao pinturaDao();
    public abstract UserDao userDao();
    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public void insertInitialData() {
        Executors.newSingleThreadExecutor().execute(() -> {
            PinturaDao dao = pinturaDao();
            dao.deleteAll(); // Opcional: Limpiar la tabla antes de insertar
            dao.insert(
                    new Pintura("Mona Lisa I", "Leonardo da Vinci", "Retrato de una mujer", "GALERIA I", "mona_lisa", "mona_lisa_audio")
            );
            dao.insert(
                    new Pintura("Mona Lisa II", "Leonardo da Vinci", "Retrato de una mujer", "GALERIA I", "mona_lisa", "mona_lisa_audio")
            );
            dao.insert(
                    new Pintura("Mona Lisa III", "Leonardo da Vinci", "Retrato de una mujer", "GALERIA I", "mona_lisa", "mona_lisa_audio")
            );
            dao.insert(
                    new Pintura("Mona Lisa IV", "Leonardo da Vinci", "Retrato de una mujer", "GALERIA I", "mona_lisa", "mona_lisa_audio")
            );
            dao.insert(
                    new Pintura("Mona Lisa V", "Leonardo da Vinci", "Retrato de una mujer", "GALERIA I", "mona_lisa", "mona_lisa_audio")
            );
            dao.insert(
                    new Pintura("Mona Lisa VI", "Leonardo da Vinci", "Retrato de una mujer", "GALERIA I", "mona_lisa", "mona_lisa_audio")
            );
            dao.insert(
                    new Pintura("Mona Lisa VI", "Leonardo da Vinci", "Retrato de una mujer", "GALERIA I", "the_scream", "mona_lisa_audio")
            );
            dao.insert(
                    new Pintura("Mona Lisa VI", "Leonardo da Vinci", "Retrato de una mujer", "GALERIA I", "mona_lisa", "mona_lisa_audio")
            );
            dao.insert(
                    new Pintura("Mona Lisa VI", "Leonardo da Vinci", "Retrato de una mujer", "GALERIA II", "mona_lisa", "mona_lisa_audio")
            );
        });
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                PinturaDao dao = INSTANCE.pinturaDao();
                dao.deleteAll();

                // Añadir pinturas de ejemplo
                Pintura pintura1 = new Pintura("Mona Lisa", "Leonardo da Vinci", "Retrato de una mujer", "GALERIA I", "mona_lisa", "mona_lisa_audio");
                Pintura pintura2 = new Pintura("La Noche Estrellada", "Vincent van Gogh", "Paisaje nocturno", "GALERIA I", "starry_night", "starry_night_audio");
                Pintura pintura3 = new Pintura("El Grito", "Edvard Munch", "Figura gritando", "GALERIA II", "the_scream", "the_scream_audio");

                Log.d("AppDatabase", "Inserting: " + pintura1);
                Log.d("AppDatabase", "Inserting: " + pintura2);
                Log.d("AppDatabase", "Inserting: " + pintura3);


                dao.insert(pintura1);
                dao.insert(pintura2);
                dao.insert(pintura3);

                // Añadir usuarios
                UserDao userDao = INSTANCE.userDao();
                userDao.insert(new User("user1", "password1"));
                userDao.insert(new User("user2", "password2"));
            });
        }
    };


}