package com.example.mylogin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylogin.database.AppDatabase;
import com.example.mylogin.database.UserDao;
import com.example.mylogin.model.HondanaUser;
import com.example.mylogin.repository.UserRepository;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {
    private EditText editEmail, editUsername2, editPasswordc1, editPasswordc2;
    private Button btnAddUser;
    private AppDatabase db;

    private String p1, p2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        db=AppDatabase.getInstance(this.getApplicationContext());
        UserDao userDao=db.userDao();
        UserRepository userRepository=new UserRepository(userDao);
        HondanaUser hondanaUser = new HondanaUser();

        //Botones
        btnAddUser=(Button) findViewById(R.id.btnAddUser);
        //Edit Text
        editEmail=(EditText)findViewById(R.id.editEmail);
        editUsername2=(EditText)findViewById(R.id.editUsername2);
        editPasswordc1=(EditText)findViewById(R.id.editPasswordc1);
        editPasswordc2=(EditText)findViewById(R.id.editPasswordc2);



        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p1 = editPasswordc1.getText().toString();
                p2 = editPasswordc2.getText().toString();

                if ((editEmail.getText().toString().length() > 0) &
                        (editPasswordc1.getText().toString().length() > 0) &
                        (editUsername2.getText().toString().length() > 0) &
                        (editPasswordc2.getText().toString().length() > 0))
                {

                    hondanaUser.setEmail(editEmail.getText().toString());
                    hondanaUser.setUserName(editUsername2.getText().toString());
                    hondanaUser.setPassword(editPasswordc1.getText().toString());
                    userRepository.insert(hondanaUser);
                    Toast.makeText(RegisterActivity.this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();

                    if (p1.equals(p2))
                    {
                        hondanaUser.setEmail(editEmail.getText().toString());
                        hondanaUser.setUserName(editUsername2.getText().toString());
                        hondanaUser.setPassword(editPasswordc1.getText().toString());
                        userRepository.insert(hondanaUser);
                        Toast.makeText(RegisterActivity.this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }




                }
                else{Toast.makeText(RegisterActivity.this, "Rellene todo los campos!!", Toast.LENGTH_SHORT).show();}
            }
        });
    }
}