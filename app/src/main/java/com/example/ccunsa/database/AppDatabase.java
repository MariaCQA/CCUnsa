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

@Database(entities = {Pintura.class, User.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PinturaDao pinturaDao();
    public abstract UserDao userDao();
    //
    private static final String dbName= "user";
    private static volatile AppDatabase INSTANCE;


    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    //si
    public static synchronized AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {

            synchronized (AppDatabase.class) {
                //si
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")

                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public void insertInitialData() {
        Executors.newSingleThreadExecutor().execute(() -> {
            PinturaDao dao = pinturaDao();
            UserDao udao = userDao();

            dao.deleteAll(); // Opcional: Limpiar la tabla antes de insertar
// Definir las pinturas con descripciones ajustadas
            Pintura monaLisa = new Pintura(
                    "Mona Lisa",
                    "Leonardo da Vinci",
                    "La Mona Lisa es una pintura de Leonardo da Vinci, creada entre 1503 y 1506. Conocida por su enigmática sonrisa y su mirada cautivadora, esta obra maestra del Renacimiento destaca por su técnica de sfumato, que permite una transición suave entre colores y sombras. La Mona Lisa se exhibe en el Museo del Louvre, en París, y su historia incluye robos y vandalismos que han aumentado su leyenda. La pintura es un ícono perdurable del arte y un ejemplo sobresaliente del genio creativo de da Vinci.",
                    "GALERIA I",
                    "mona_lisa",
                    "mona_lisa"
            );

            Pintura lasMeninas = new Pintura(
                    "Las Meninas",
                    "Diego Velázquez",
                    "Las Meninas es una pintura de Diego Velázquez, realizada en 1656. Representa una escena en el palacio real de Madrid, con Velázquez pintándose a sí mismo trabajando en su taller. La complejidad de la composición, la interacción entre los personajes y el uso de la perspectiva han sido objeto de numerosos análisis críticos. La obra desafía las convenciones del arte de su época, proporcionando una reflexión sobre la percepción y la realidad en la pintura.",
                    "GALERIA II",
                    "las_meninas",
                    "las_meninas"
            );

            Pintura ultimaCena = new Pintura(
                    "La Última Cena",
                    "Leonardo da Vinci",
                    "La Última Cena es un fresco de Leonardo da Vinci, pintado entre 1495 y 1498. Representa el momento en que Jesucristo revela a sus discípulos que uno de ellos lo traicionará. La pintura destaca por su innovador uso de la perspectiva y su capacidad para transmitir emociones profundas. Aunque ha sido restaurada y dañada a lo largo de los siglos, sigue siendo un ejemplo icónico del genio artístico de da Vinci y un testimonio de su habilidad para captar momentos humanos significativos.",
                    "GALERIA III",
                    "la_ultima_cena",
                    "la_ultima_cena"
            );

            Pintura caminante = new Pintura(
                    "El Caminante",
                    "Caspar David Friedrich",
                    "El Caminante es una pintura del Romanticismo por Caspar David Friedrich. Muestra a un hombre de espaldas contemplando un vasto paisaje montañoso. La obra refleja la inmensidad del paisaje y la capacidad de Friedrich para evocar emociones profundas mediante la representación de la naturaleza. Aunque no es tan conocida como otras obras del Romanticismo, destaca por su capacidad para capturar la grandeza y el misterio del paisaje.",
                    "GALERIA IV",
                    "el_caminante",
                    "el_caminante"
            );


// Insertar pinturas en las galerías I a VI
            dao.insert(new Pintura(
                    monaLisa.getPaintingName(),    // Cambiado de getTitulo() a getPaintingName()
                    monaLisa.getAuthorName(),      // Cambiado de getAutor() a getAuthorName()
                    monaLisa.getDescription(),     // Correcto, coincide con getDescription()
                    "GALERIA I",
                    monaLisa.getIconPath(),        // Cambiado de getImagen() a getIconPath()
                    monaLisa.getAudioPath()        // Cambiado de getArchivo() a getAudioPath()
            ));
            dao.insert(new Pintura(
                    monaLisa.getPaintingName(),
                    monaLisa.getAuthorName(),
                    monaLisa.getDescription(),
                    "GALERIA II",
                    monaLisa.getIconPath(),
                    monaLisa.getAudioPath()
            ));
            dao.insert(new Pintura(
                    monaLisa.getPaintingName(),
                    monaLisa.getAuthorName(),
                    monaLisa.getDescription(),
                    "GALERIA III",
                    monaLisa.getIconPath(),
                    monaLisa.getAudioPath()
            ));
            dao.insert(new Pintura(
                    monaLisa.getPaintingName(),
                    monaLisa.getAuthorName(),
                    monaLisa.getDescription(),
                    "GALERIA IV",
                    monaLisa.getIconPath(),
                    monaLisa.getAudioPath()
            ));
            dao.insert(new Pintura(
                    monaLisa.getPaintingName(),
                    monaLisa.getAuthorName(),
                    monaLisa.getDescription(),
                    "GALERIA V",
                    monaLisa.getIconPath(),
                    monaLisa.getAudioPath()
            ));
            dao.insert(new Pintura(
                    monaLisa.getPaintingName(),
                    monaLisa.getAuthorName(),
                    monaLisa.getDescription(),
                    "GALERIA VI",
                    monaLisa.getIconPath(),
                    monaLisa.getAudioPath()
            ));

            dao.insert(new Pintura(
                    lasMeninas.getPaintingName(),
                    lasMeninas.getAuthorName(),
                    lasMeninas.getDescription(),
                    "GALERIA I",
                    lasMeninas.getIconPath(),
                    lasMeninas.getAudioPath()
            ));
            dao.insert(new Pintura(
                    lasMeninas.getPaintingName(),
                    lasMeninas.getAuthorName(),
                    lasMeninas.getDescription(),
                    "GALERIA II",
                    lasMeninas.getIconPath(),
                    lasMeninas.getAudioPath()
            ));
            dao.insert(new Pintura(
                    lasMeninas.getPaintingName(),
                    lasMeninas.getAuthorName(),
                    lasMeninas.getDescription(),
                    "GALERIA III",
                    lasMeninas.getIconPath(),
                    lasMeninas.getAudioPath()
            ));
            dao.insert(new Pintura(
                    lasMeninas.getPaintingName(),
                    lasMeninas.getAuthorName(),
                    lasMeninas.getDescription(),
                    "GALERIA IV",
                    lasMeninas.getIconPath(),
                    lasMeninas.getAudioPath()
            ));
            dao.insert(new Pintura(
                    lasMeninas.getPaintingName(),
                    lasMeninas.getAuthorName(),
                    lasMeninas.getDescription(),
                    "GALERIA V",
                    lasMeninas.getIconPath(),
                    lasMeninas.getAudioPath()
            ));
            dao.insert(new Pintura(
                    lasMeninas.getPaintingName(),
                    lasMeninas.getAuthorName(),
                    lasMeninas.getDescription(),
                    "GALERIA VI",
                    lasMeninas.getIconPath(),
                    lasMeninas.getAudioPath()
            ));

            dao.insert(new Pintura(
                    ultimaCena.getPaintingName(),
                    ultimaCena.getAuthorName(),
                    ultimaCena.getDescription(),
                    "GALERIA I",
                    ultimaCena.getIconPath(),
                    ultimaCena.getAudioPath()
            ));
            dao.insert(new Pintura(
                    ultimaCena.getPaintingName(),
                    ultimaCena.getAuthorName(),
                    ultimaCena.getDescription(),
                    "GALERIA II",
                    ultimaCena.getIconPath(),
                    ultimaCena.getAudioPath()
            ));
            dao.insert(new Pintura(
                    ultimaCena.getPaintingName(),
                    ultimaCena.getAuthorName(),
                    ultimaCena.getDescription(),
                    "GALERIA III",
                    ultimaCena.getIconPath(),
                    ultimaCena.getAudioPath()
            ));
            dao.insert(new Pintura(
                    ultimaCena.getPaintingName(),
                    ultimaCena.getAuthorName(),
                    ultimaCena.getDescription(),
                    "GALERIA IV",
                    ultimaCena.getIconPath(),
                    ultimaCena.getAudioPath()
            ));
            dao.insert(new Pintura(
                    ultimaCena.getPaintingName(),
                    ultimaCena.getAuthorName(),
                    ultimaCena.getDescription(),
                    "GALERIA V",
                    ultimaCena.getIconPath(),
                    ultimaCena.getAudioPath()
            ));
            dao.insert(new Pintura(
                    ultimaCena.getPaintingName(),
                    ultimaCena.getAuthorName(),
                    ultimaCena.getDescription(),
                    "GALERIA VI",
                    ultimaCena.getIconPath(),
                    ultimaCena.getAudioPath()
            ));

            dao.insert(new Pintura(
                    caminante.getPaintingName(),
                    caminante.getAuthorName(),
                    caminante.getDescription(),
                    "GALERIA I",
                    caminante.getIconPath(),
                    caminante.getAudioPath()
            ));
            dao.insert(new Pintura(
                    caminante.getPaintingName(),
                    caminante.getAuthorName(),
                    caminante.getDescription(),
                    "GALERIA II",
                    caminante.getIconPath(),
                    caminante.getAudioPath()
            ));
            dao.insert(new Pintura(
                    caminante.getPaintingName(),
                    caminante.getAuthorName(),
                    caminante.getDescription(),
                    "GALERIA III",
                    caminante.getIconPath(),
                    caminante.getAudioPath()
            ));
            dao.insert(new Pintura(
                    caminante.getPaintingName(),
                    caminante.getAuthorName(),
                    caminante.getDescription(),
                    "GALERIA IV",
                    caminante.getIconPath(),
                    caminante.getAudioPath()
            ));
            dao.insert(new Pintura(
                    caminante.getPaintingName(),
                    caminante.getAuthorName(),
                    caminante.getDescription(),
                    "GALERIA V",
                    caminante.getIconPath(),
                    caminante.getAudioPath()
            ));
            dao.insert(new Pintura(
                    caminante.getPaintingName(),
                    caminante.getAuthorName(),
                    caminante.getDescription(),
                    "GALERIA VI",
                    caminante.getIconPath(),
                    caminante.getAudioPath()
            ));


// Fin de la carga de datos



        });
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                ///////
                PinturaDao dao = INSTANCE.pinturaDao();
                dao.deleteAll();

                // Añadir pinturas de ejemplo
                Pintura pintura1 = new Pintura("Mona Lisa", "Leonardo da Vinci", "Retrato de una mujer", "GALERIA I", "mona_lisa", "mona_lisa_audio");
                Pintura pintura2 = new Pintura("La Noche Estrellada", "Vincent van Gogh", "Paisaje nocturno", "GALERIA I", "starry_night", "starry_night_audio");
                Pintura pintura3 = new Pintura("El Grito", "Edvard Munch", "Figura gritando", "GALERIA II", "the_scream", "the_scream_audio");
                Pintura pintura5 = new Pintura(12345,"El Grito", "Edvard Munch", "Figura gritando", "GALERIA II", "logo", "las_meninas");
                Log.d("AppDatabase", "Inserting: " + pintura1);
                Log.d("AppDatabase", "Inserting: " + pintura2);
                Log.d("AppDatabase", "Inserting: " + pintura5);

                dao.insert(pintura1);
                dao.insert(pintura2);
                dao.insert(pintura3);


            });
        }
    };

    private static class Callback extends RoomDatabase.Callback {
    }
}