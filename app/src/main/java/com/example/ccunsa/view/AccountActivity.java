package com.example.ccunsa.view;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ccunsa.R;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ccunsa.database.AppDatabase;
import com.example.ccunsa.model.User;
import com.google.gson.Gson;

public class AccountActivity extends AppCompatActivity {
    public final static String ACCOUNT_RECORD = "ACCOUNT_RECORD";
    public final static Integer ACCOUNT_ACEPTAR = 100;
    public final static Integer ACCOUNT_CANCELAR = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);

        Button btnAceptar = findViewById(R.id.btnAceptar);
        Button btnCancelar = findViewById(R.id.btnCancelar);

        EditText edtFirstname = findViewById(R.id.edtFirstname);
        EditText edtLastname = findViewById(R.id.edtLastname);
        EditText edtEmail = findViewById(R.id.edtEmail);
        EditText edtPhone = findViewById(R.id.edtPhone);
        EditText edtUsername2 = findViewById(R.id.edtUsername2);
        EditText edtPassword2 = findViewById(R.id.edtPassword2);

        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());

        btnAceptar.setOnClickListener(v -> {
            User newUser = new User(edtUsername2.getText().toString(), edtPassword2.getText().toString());

            AppDatabase.databaseWriteExecutor.execute(() -> {
                db.userDao().insert(newUser);
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show();
                    setResult(ACCOUNT_ACEPTAR);
                    finish();
                });
            });
        });

        btnCancelar.setOnClickListener(v -> {
            setResult(ACCOUNT_CANCELAR);
            finish();
        });
    }
}