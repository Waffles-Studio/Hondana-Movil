package com.example.mylogin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylogin.adapter.bookAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;

public class BookDetailsActivity extends AppCompatActivity {
    private String extraIDLibro;
    private String IDDOCUMENTO;
    private String linkreadbook;

    private FirebaseAuth mAuth;
    static TextView  IDLibro;
    static TextView  Titulo;
    static TextView  Autor ;
    static TextView  ISBN;
    static TextView  Editorial;
    static TextView  NumeroPaginas;
    static EditText Calificacion;
    static TextView  VinculoLibro;
    static TextView  Sinopsis;
    static String linkbook;
    static ImageView imagen;
    public Button btnSubmit, btnRead, btnHome, btnSearch, btnbtnCS;
    private GoogleSignInClient mGoogleSignClient;
    private TextView btnRegresa;
    private WebView webView;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAnalytics mFirebaseAnalytics;

    private CheckBox checkfavorito;
    private CheckBox checkLeido;
    static Integer Calif = 0;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        extraIDLibro = getIntent().getStringExtra("IDLibro");
        Titulo = (TextView) findViewById(R.id.textView8);
        Autor = (TextView) findViewById(R.id.textView9);
        Editorial = (TextView) findViewById(R.id.textView11);
        imagen = (ImageView) findViewById(R.id.imageView2);
        btnRegresa = (TextView) findViewById(R.id.textViewBackBook);
        NumeroPaginas = (TextView) findViewById(R.id.txtPaginas);
        Calificacion = (EditText) findViewById(R.id.editTextNumber);
        Sinopsis = (TextView) findViewById(R.id.textView23);
        ISBN = (TextView) findViewById(R.id.textView15);
        btnSubmit = (Button) findViewById(R.id.button3);
        btnRead = (Button) findViewById(R.id.btnReadNow);
        btnHome = (Button) findViewById(R.id.btnHome);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnbtnCS = (Button) findViewById(R.id.btnCS);
        checkfavorito = (CheckBox) findViewById(R.id.checkBox2);
        checkLeido = (CheckBox) findViewById(R.id.checkBox);


        // consulta para obtener datos de libro - usuario
        db.collection("Relacion_UsrBook")
                .whereEqualTo("Libro", extraIDLibro)
                .whereEqualTo("Usario", mAuth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.get("Calificacion") != null && document.get("Calificacion").toString().length() >= 1) {
                                    Calificacion.setText(document.get("Calificacion").toString());

                                    // traemos las variables de la base de datos para respaldarlas
                                    Calif = Integer.parseInt(Calificacion.getText().toString());


                                    checkLeido.setChecked(Boolean.parseBoolean( document.get("Leido").toString()));
                                    checkfavorito.setChecked(Boolean.parseBoolean( document.get("Favorito").toString()));

                                }
                            }
                        }
                    }
                });


        // consulta de el libro seleccionado
        db.collection("Books")
                .whereEqualTo("IDLibro", extraIDLibro)
                //.whereEqualTo("","")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Titulo.setText(document.get("Titulo").toString());
                                Autor.setText(document.get("Autor").toString());
                                Editorial.setText(document.get("Editorial").toString());
                                NumeroPaginas.setText(document.get("NumeroPaginas").toString());
                                Sinopsis.setText(document.get("Sinopsis").toString());
                                ISBN.setText(document.get("ISBN").toString());
                                linkreadbook = document.get("VinculoLibro").toString();


                                linkbook = document.get("VinculoImagen").toString();
                                Picasso.get().load(linkbook).into(imagen);
                            }
                        }
                    }
                });

        // consulta


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Calificacion.getText().length() >= 1)
                {
                    if ( Integer.parseInt(Calificacion.getText().toString()) < 0 || Integer.parseInt(Calificacion.getText().toString()) > 5){
                        Calif = 0;
                    }
                    else {
                        Calif = Integer.valueOf(Calificacion.getText().toString());
                    }
                }



                Map<String, Object> relation = new HashMap<>();
                relation.put("Calificacion", Calif);
                relation.put("Favorito", checkfavorito.isChecked()  );
                relation.put("Leido",checkLeido.isChecked() );

                relation.put("Libro",extraIDLibro);
                relation.put("Usario",mAuth.getCurrentUser().getEmail());
                db.collection("Relacion_UsrBook").document(extraIDLibro+""+mAuth.getCurrentUser().getEmail()).set(relation);
            }
        });


        btnRegresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(BookDetailsActivity.this, HomeActivity.class);
                BookDetailsActivity.this.startActivity(detailIntent);
            }
        });

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(BookDetailsActivity.this, activity_webview.class);
                myIntent.putExtra("LinkLibro", linkreadbook);
                startActivity(myIntent);

            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(BookDetailsActivity.this, HomeActivity.class);
                BookDetailsActivity.this.startActivity(detailIntent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(BookDetailsActivity.this, SearchActivity.class);
                BookDetailsActivity.this.startActivity(detailIntent);
            }
        });

        btnbtnCS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleSignClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(BookDetailsActivity.this, MainActivity.class);
                            BookDetailsActivity.this.startActivity(intent);
                        }
                    }
                });
            }
        });


        checkfavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Calificacion.getText().length() >= 1)
                {
                    if ( Integer.parseInt(Calificacion.getText().toString()) < 0 || Integer.parseInt(Calificacion.getText().toString()) > 5){
                        Calif = 0;
                    }
                    else {
                        Calif = Integer.valueOf(Calificacion.getText().toString());
                    }
                }


                Map<String, Object> relation = new HashMap<>();
                relation.put("Calificacion", Calif);
                relation.put("Favorito", checkfavorito.isChecked()  );
                relation.put("Leido",checkLeido.isChecked() );

                relation.put("Libro",extraIDLibro);
                relation.put("Usario",mAuth.getCurrentUser().getEmail());
                db.collection("Relacion_UsrBook").document(extraIDLibro+""+mAuth.getCurrentUser().getEmail()).set(relation);

            }
        });

        checkLeido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Calificacion.getText().length() >= 1)
                {
                    if ( Integer.parseInt(Calificacion.getText().toString()) < 0 || Integer.parseInt(Calificacion.getText().toString()) > 5){
                        Calif = 0;
                    }
                    else {
                        Calif = Integer.valueOf(Calificacion.getText().toString());
                    }
                }


                Map<String, Object> relation = new HashMap<>();
                relation.put("Calificacion", Calif);
                relation.put("Favorito", checkfavorito.isChecked()  );
                relation.put("Leido",checkLeido.isChecked() );

                relation.put("Libro",extraIDLibro);
                relation.put("Usario",mAuth.getCurrentUser().getEmail());
                db.collection("Relacion_UsrBook").document(extraIDLibro+""+mAuth.getCurrentUser().getEmail()).set(relation);

            }
        });
    }

    @Override
    public void onBackPressed() {
        // do nothing.
    }


}