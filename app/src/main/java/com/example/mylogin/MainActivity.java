package com.example.mylogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btnNext;
    private TextView btnRegister, mensaje;
    public static final String EXTRA_MESSAGE = "com.example.mylogin.MESSAGE";
    private FirebaseAuth mAuth;
    private FirebaseAnalytics mFirebaseAnalytics;
    String usr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.METHOD, "Main");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW,params);



        btnNext = (Button) findViewById(R.id.btnNext);
        btnRegister = (TextView) findViewById(R.id.textView6);
        mensaje = (TextView) findViewById(R.id.txtMsg);

        if(isNetDisponible()){
            if(isOnlineNet()){
                mensaje.setText("");
                if(mAuth.getCurrentUser() != null){
                    usr = mAuth.getCurrentUser().getEmail();

                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if(currentUser != null){
                        Intent loginIntent = new Intent(MainActivity.this, HomeActivity.class);
                        loginIntent.putExtra("Username", usr);
                        MainActivity.this.startActivity(loginIntent);
                    }
                }

            }
            else{
                Toast.makeText(MainActivity.this, "Sin conexión", Toast.LENGTH_SHORT).show();
                mensaje.setText("¡No se tiene conexión a la red, asegúrese de que cuente con internet!");
            }
        }
        else {
            Toast.makeText(MainActivity.this, "Wifi o Red Móvil apagada", Toast.LENGTH_SHORT).show();
            mensaje.setText("¡La red está deshabilitada, actívela para poder usar esta aplicación!");
        }

        String strmsg = getIntent().getStringExtra("msg");
        if (strmsg != null){
            Toast.makeText(MainActivity.this, strmsg, Toast.LENGTH_SHORT).show();
        }



        //Clic Boton de Iniciar Sesion
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        //Clic Boton de registrarse
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    //Funcion para comprobar si tiene encendido los datos o wifi
    private boolean isNetDisponible() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();
        return (actNetInfo != null && actNetInfo.isConnected());
    }

    //Funcion para comprobar si tiene coneccion a internet
    public boolean isOnlineNet() {
        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onStart(){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){

        }
        super.onStart();
    }

}