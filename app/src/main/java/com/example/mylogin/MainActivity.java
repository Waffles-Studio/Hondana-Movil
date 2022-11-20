package com.example.mylogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnNext;
    private TextView btnRegister, mensaje;
    public static final String EXTRA_MESSAGE = "com.example.mylogin.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Se declaran y relacionan elementos
        btnNext = (Button) findViewById(R.id.btnNext);
        btnRegister = (TextView) findViewById(R.id.textView6);
        mensaje = (TextView) findViewById(R.id.txtMsg);

        //Comprobar Si se tiene coneccion a internet
        if(isNetDisponible()){
            if(isOnlineNet()){
                mensaje.setText("");
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
    public Boolean isOnlineNet() {
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
}