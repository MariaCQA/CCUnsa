/////////////////
package com.example.ccunsa.view;

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
import com.example.ccunsa.database.UserDao;
import com.example.ccunsa.model.User;
import com.example.ccunsa.R;

public class RegisterActivity extends AppCompatActivity {
    EditText name, lastname, email,phone, username, password;
    Button registrar, login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.name);
        lastname = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        registrar = findViewById(R.id.btnRegistrar);
        login = findViewById(R.id.btnLogin);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating user entity
                User user = new User();
                user.setName(name.getText().toString());
                user.setLastname(lastname.getText().toString());
                user.setEmail(email.getText().toString());
                user.setPhone(phone.getText().toString());
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());
                if(validateInput(user)){
                    AppDatabase appDatabase = AppDatabase.getDatabase(getApplicationContext());
                    final UserDao userDao = appDatabase.userDao();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //registrando usuario
                            userDao.registerUser(user);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "¡Usuario Registrado!", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    }).start();

                }else{
                    Toast.makeText(getApplicationContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

    }

    private Boolean validateInput(User user){
        if(user.getName().isEmpty()||
        user.getPassword().isEmpty()) {
            return false;
        }
        return true;
    }

}



















































