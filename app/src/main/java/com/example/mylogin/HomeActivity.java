package com.example.mylogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private TextView txtWelcome;
    private Button btnCerrar;
    private GoogleSignInClient mGoogleSignClient;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.METHOD, "HOME");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW,params);


        txtWelcome = (TextView) findViewById(R.id.txtWelcome);
        btnCerrar = (Button) findViewById(R.id.btnCS);

        txtWelcome.setText("Hello "+getIntent().getStringExtra("Username")+"!");


        //Configurar Google SignIn
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //.requestIdToken(getString(R.string.default_web_client_id))

        mGoogleSignClient = GoogleSignIn.getClient(this, gso);


        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mGoogleSignClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mFirebaseAnalytics = FirebaseAnalytics.getInstance(HomeActivity.this);
                            Bundle bundle = new Bundle();
                            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "SignOut");
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                            HomeActivity.this.startActivity(intent);
                        } else {
                            Toast.makeText(HomeActivity.this, "No se pudo cerrar sesión", Toast.LENGTH_SHORT).show();
                        }
                    }
                });






            }
        });
    }
}