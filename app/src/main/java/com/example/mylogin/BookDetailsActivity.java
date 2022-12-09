package com.example.mylogin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylogin.adapter.bookAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import io.reactivex.rxjava3.annotations.NonNull;

public class BookDetailsActivity extends AppCompatActivity {
    private String extraIDLibro;
    static TextView  IDLibro;
    static TextView  Titulo;
    static TextView  Autor ;
    static TextView  ISBN;
    static TextView  Editorial;
    static TextView  NumeroPaginas;
    static TextView  Calificacion;
    static TextView  VinculoLibro;
    static TextView  Sinopsis;
    static String linkbook;
    static ImageView imagen;

    private TextView btnRegresa;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        extraIDLibro=getIntent().getStringExtra("IDLibro");
        Titulo = (TextView) findViewById(R.id.textView8);
        Autor = (TextView) findViewById(R.id.textView9);
        Editorial = (TextView) findViewById(R.id.textView11);
        imagen = (ImageView) findViewById(R.id.imageView2);
        btnRegresa = (TextView) findViewById(R.id.textViewBackBook);
        NumeroPaginas = (TextView) findViewById(R.id.txtPaginas);
        Calificacion = (TextView) findViewById(R.id.textView19);
        Sinopsis = (TextView) findViewById(R.id.textView22);
        ISBN = (TextView) findViewById(R.id.textView15);

      // consulta

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
                                Calificacion.setText(document.get("Calificacion").toString());
                                Sinopsis.setText(document.get("Sinopsis").toString());
                                ISBN.setText(document.get("ISBN").toString());

                                linkbook = document.get("VinculoImagen").toString();
                                Picasso.get().load(linkbook).into(imagen);

                            }
                        } else {

                        }
                    }
                });

        // consulta


        btnRegresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(BookDetailsActivity.this, HomeActivity.class);
                BookDetailsActivity.this.startActivity(detailIntent);
            }
        });



    }
}