package com.example.mylogin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        extraIDLibro=getIntent().getStringExtra("IDLibro");
        Titulo = (TextView) findViewById(R.id.textView8);
        Autor = (TextView) findViewById(R.id.textView9);
        Editorial = (TextView) findViewById(R.id.textView11);

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
                            }
                        } else {

                        }
                    }
                });

        // consulta


    }
}