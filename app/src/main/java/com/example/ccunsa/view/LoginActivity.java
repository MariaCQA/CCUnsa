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
import com.example.ccunsa.database.UserDao;
import com.example.ccunsa.databinding.ActivityLoginBinding;
import com.example.ccunsa.databinding.ActivityMainBinding;
import com.example.ccunsa.model.User;
import com.example.ccunsa.view.fragments.HomeFragment;
import com.google.gson.Gson;


public class LoginActivity extends AppCompatActivity {
    private static String TAG = "LoginActivity";

    EditText username1, password1;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username1 = findViewById(R.id.username);
        password1 = findViewById(R.id.password);
        login = findViewById(R.id.btnLogin);

        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = username1.getText().toString();
                String password = password1.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fill all fields!", Toast.LENGTH_SHORT).show();
                } else {
                    AppDatabase appDatabase = AppDatabase.getDatabase(getApplicationContext());
                    UserDao userDao = appDatabase.userDao();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            User user = userDao.cancelar(username, password);
                            if (user == null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Credenciales invalidas", Toast.LENGTH_SHORT).show();
                                    }

                                });
                            } else {
                                String name = user.getName();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class)
                                        .putExtra("name", name));
                            }
                        }
                    }).start();
                }


            };
        });
    }
}