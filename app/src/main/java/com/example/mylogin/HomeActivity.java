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

import com.example.mylogin.adapter.bookAdapter;
import com.example.mylogin.model.Book;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {


    private RecyclerView recycler;
    private TextView txtWelcome;
    private Button btnCerrar, btnSearch;
    private GoogleSignInClient mGoogleSignClient;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private bookAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recycler = (RecyclerView) findViewById(R.id.recyclerID);
        recycler.setLayoutManager(new GridLayoutManager(this,3));

        //Se obtienen referencias de layout
        btnSearch = (Button) findViewById(R.id.btnSearch);
        txtWelcome = (TextView) findViewById(R.id.txtWelcome);
        btnCerrar = (Button) findViewById(R.id.btnCS);

        //Consulta de libros
        Query query = db.collection("Books");
        FirestoreRecyclerOptions<Book> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Book>().setQuery(query,Book.class).build();
        mAdapter = new bookAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        recycler.setAdapter(mAdapter);

        //Obtiene la instancia de Firebase y Logs de Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.METHOD, "HOME");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW,params);

        //Otros
        obtenerusuario();

        //Configurar ventana Google SignIn
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignClient = GoogleSignIn.getClient(this, gso);

        //Boton para cerrar sesion y volver a home
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

        //Boton para ir a layout de busqueda
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                HomeActivity.this.startActivity(intent);
            }
        });

        //Obtener libro seleccionado
        mAdapter.setOnClicListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent detailIntent = new Intent(HomeActivity.this, BookDetailsActivity.class);

                detailIntent.putExtra("IDLibro",  mAdapter.getItem(recycler.getChildAdapterPosition(view)).getIDLibro());

                HomeActivity.this.startActivity(detailIntent);
            }
        });

    }

    //Obtener datos del usuario
    private void obtenerusuario(){
        mAuth = FirebaseAuth.getInstance();
        String correoUsuario = mAuth.getCurrentUser().getEmail();
        DocumentReference docRef = db.collection("HondanaDB").document(correoUsuario);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        txtWelcome.setText("Hello "+document.get("UserName")+"!");
                    }
                }
            }
        });
    }

    //Codigo de Load
  @Override
  protected void onStart() {
      super.onStart();
      mAdapter.startListening();
  }

    //Codigo de close
  @Override
  protected void onStop() {
      super.onStop();
      mAdapter.stopListening();
  }

    @Override
    public void onBackPressed() {
        // do nothing.
    }


}