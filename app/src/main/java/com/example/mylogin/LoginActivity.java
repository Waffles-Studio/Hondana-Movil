package com.example.mylogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mylogin.database.AppDatabase;
import com.example.mylogin.database.UserDao;
import com.example.mylogin.model.HondanaUser;
import com.example.mylogin.repository.UserRepository;
import com.example.mylogin.viewmodel.ListaUsersViewModel;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText txtUser, txtPass;
    private Button btnLogin, btnRegister;
    public static final String EXTRA_MESSAGE = "com.example.mylogin.MESSAGE";
    private AppDatabase db;

    private String d1, d2;
    private HondanaUser hondanaUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUser = (EditText) findViewById(R.id.editUsername);
        txtPass = (EditText) findViewById(R.id.editPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnGoRegister);

        db=AppDatabase.getInstance(this.getApplicationContext());
        UserDao userDao=db.userDao();
        UserRepository userRepository=new UserRepository(userDao);



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtUser.length() == 0 || txtPass.length() == 0){

                    Toast.makeText(LoginActivity.this, "Please Fill All the Fields.", Toast.LENGTH_SHORT).show();
                } else {

                    d1 = txtUser.getText().toString();
                    d2 = txtPass.getText().toString();
                    hondanaUser = (userRepository.findByPass(d1, d2));

                    if (hondanaUser != null)
                    {
                        String username = txtUser.getText().toString();
                        Intent loginIntent = new Intent(LoginActivity.this, HomeActivity.class);
                        loginIntent.putExtra("Username", username);
                        LoginActivity.this.startActivity(loginIntent);
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });
    }
}