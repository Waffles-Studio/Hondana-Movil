package com.example.mylogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    // RecyclerView References 2022-12-02
    ArrayList<String> listData;
    RecyclerView recycler;
    // RecyclerView References 2022-12-02

    private TextView txtWelcome;
    private Button btnCerrar;
    private GoogleSignInClient mGoogleSignClient;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // RecyclerView References 2022-12-02
        recycler = (RecyclerView) findViewById(R.id.recyclerID);
        //recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recycler.setLayoutManager(new GridLayoutManager(this,3));
        listData = new ArrayList<String>();
        for (int i=0; i<=50; i++){
            listData.add("Book #" + i + " ");
        }
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(listData);
        recycler.setAdapter(adapter);
        // RecyclerView References 2022-12-02

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.METHOD, "HOME");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW,params);


        txtWelcome = (TextView) findViewById(R.id.txtWelcome);
        btnCerrar = (Button) findViewById(R.id.btnCS);

        obtenerusuario(getIntent().getStringExtra("Username"));


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

    private void obtenerusuario(String correoUsuario){
        DocumentReference docRef = db.collection("HondanaDB").document(correoUsuario);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        txtWelcome.setText("Hello "+document.get("UserName")+"!");
                    } else {
                        Toast.makeText(HomeActivity.this, "No tiene nombre", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "Error al traer nombre", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}