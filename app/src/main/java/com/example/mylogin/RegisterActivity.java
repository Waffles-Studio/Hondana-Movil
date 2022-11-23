package com.example.mylogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {
    private EditText editEmail, editUsername2, editPasswordc1, editPasswordc2;
    private Button btnAddUser;
    private FirebaseAnalytics mFirebaseAnalytics;
    //private AppDatabase db;

    private String p1, p2, e1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //RELACIONAR ELEMENTOS Y CREARLOS
        btnAddUser=(Button) findViewById(R.id.btnAddUser);
        editEmail=(EditText)findViewById(R.id.editEmail);
        editUsername2=(EditText)findViewById(R.id.editUsername2);
        editPasswordc1=(EditText)findViewById(R.id.editPasswordc1);
        editPasswordc2=(EditText)findViewById(R.id.editPasswordc2);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.METHOD, "REGISTER");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW,params);

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                e1 = editEmail.getText().toString();
                p1 = editPasswordc1.getText().toString();
                p2 = editPasswordc2.getText().toString();

                if ((editEmail.getText().toString().length() > 0) &
                        (editPasswordc1.getText().toString().length() > 0) &
                        (editUsername2.getText().toString().length() > 0) &
                        (editPasswordc2.getText().toString().length() > 0))
                {

                    if (p1.equals(p2))
                    {

                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(e1,p1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    mFirebaseAnalytics = FirebaseAnalytics.getInstance(RegisterActivity.this);
                                    Bundle bundle = new Bundle();
                                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "RegisterEmail");
                                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle);

                                    Intent RegisterIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                    RegisterIntent.putExtra("msg", "User Successfully Registered.");
                                    RegisterActivity.this.startActivity(RegisterIntent);
                                }
                                else {
                                    String mensaje = task.getException().getMessage().toString();
                                    Toast.makeText(RegisterActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this, "Passwords do not Match.", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(RegisterActivity.this, "Please Fill All the Fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}