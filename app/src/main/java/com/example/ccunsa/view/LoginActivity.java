package com.example.ccunsa.view;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ccunsa.R;
import com.example.ccunsa.database.AppDatabase;
import com.example.ccunsa.databinding.ActivityLoginBinding;
import com.example.ccunsa.databinding.ActivityMainBinding;
import com.example.ccunsa.model.User;
import com.example.ccunsa.view.fragments.HomeFragment;
import com.google.gson.Gson;


public class LoginActivity extends AppCompatActivity {
    private static String TAG = "LoginActivity";
    private ActivityLoginBinding binding;
    private AccountEntity accountEntity;
    private String accountEntityString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        EditText edtNombre = binding.edtNombre;

        EditText edtContraseniaa = binding.edtContraseniaa;
        Button btnLogin = binding.btnLogin;
        Button btnAddAccount = binding.btnAddAccount;

        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtNombre.getText().toString();
                String password = edtContraseniaa.getText().toString();

                // Ejecutar la consulta en un hilo separado
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    User user = db.userDao().authenticate(username, password);
                    runOnUiThread(() -> {
                        if (user != null) {
                            Toast.makeText(getApplicationContext(), "Bienvenido a mi App", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error en la autenticación", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }
        });

        btnAddAccount.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
            activityResultLauncher.launch(intent);
        });
    }

    // Registrar el resultado de la actividad AccountActivity
    ActivityResultLauncher<Intent>activityResultLauncher=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult activityResult) {
                    // Obtener el código de resultado
                    Integer resulCode = activityResult.getResultCode();

                    // Verificar si se aceptó la cuenta
                    if(resulCode ==AccountActivity.ACCOUNT_ACEPTAR){

                        // Obtener los datos del intent
                        Intent data = activityResult.getData();
                        accountEntityString=data.getStringExtra(AccountActivity.ACCOUNT_RECORD);

                        // Convertir la cadena JSON a un objeto AccountEntity
                        Gson gson=new Gson();
                        accountEntity= gson.fromJson(accountEntityString,AccountEntity.class);

                        // Obtener el nombre del usuario y mostrarlo en un Toast
                        String firstname= accountEntity.getFirstname();
                        Toast.makeText (getApplicationContext(),"Nombre:"+firstname,Toast.LENGTH_SHORT).show();
                        Log.d("LoginActivity","Nombre:"+firstname);

                        // Mostrar un mensaje de cancelación
                    } else if (resulCode == AccountActivity.ACCOUNT_CANCELAR) {
                        Toast.makeText (getApplicationContext(),"Cancelado",Toast.LENGTH_SHORT).show();
                        Log.d("LoginActivity","Cancelado");
                    }

                }
            }


    );
}
