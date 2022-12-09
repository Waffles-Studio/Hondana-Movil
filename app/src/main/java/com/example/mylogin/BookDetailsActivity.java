package com.example.mylogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylogin.adapter.bookAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    public Button btnSubmit;

    private TextView btnRegresa;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        extraIDLibro=getIntent().getStringExtra("IDLibro");
        Titulo = (TextView) findViewById(R.id.textView8);
        Autor = (TextView) findViewById(R.id.textView9);
        Editorial = (TextView) findViewById(R.id.textView11);
        imagen = (ImageView) findViewById(R.id.imageView2);
        btnRegresa = (TextView) findViewById(R.id.textViewBackBook);
        btnSubmit =   (Button) findViewById(R.id.button3);
        Calificacion = (EditText) findViewById(R.id.editTextNumber);

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
                                linkbook = document.get("VinculoImagen").toString();
                                Picasso.get().load(linkbook).into(imagen);


                            }
                        } else {

                        }
                    }
                });

        // consulta



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Calificacion.length() == 0){
                    Toast.makeText(BookDetailsActivity.this,""+ Calificacion.getText().toString()+" "+extraIDLibro+" "+mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                }else
                {


                Map<String, Object> relation = new HashMap<>();
                relation.put("Calificacion",Calificacion.getText().toString() );
                relation.put("Favorito", false  );
                relation.put("Leido",false );
                relation.put("Libro",extraIDLibro);
                relation.put("Usario",mAuth.getCurrentUser().getEmail());



                db.collection("Relacion_UsrBook").document(extraIDLibro+""+mAuth.getCurrentUser().getEmail()).set(relation)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(),"Creado Correctamente",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@androidx.annotation.NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"No ha sido Creado ",Toast.LENGTH_SHORT).show();
                            }
                        });

                }
            }
        });


        btnRegresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(BookDetailsActivity.this, HomeActivity.class);
                BookDetailsActivity.this.startActivity(detailIntent);
            }
        });



    }
}