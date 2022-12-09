package com.example.mylogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mylogin.adapter.bookAdapter;
import com.example.mylogin.model.Book;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import io.reactivex.rxjava3.annotations.NonNull;


public class SearchActivity extends AppCompatActivity {
    private RecyclerView recycler;
    private bookAdapter mAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText TextSearch;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        TextSearch = (EditText) findViewById(R.id.TxtSearch);
        recycler = (RecyclerView) findViewById(R.id.recyclerSearch);
        recycler.setLayoutManager(new GridLayoutManager(this,3));


        //Consulta de libros
        Query query = db.collection("Books");
        FirestoreRecyclerOptions<Book> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Book>().setQuery(query,Book.class).build();
        mAdapter = new bookAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        recycler.setAdapter(mAdapter);


        //Obtener libro seleccionado
        mAdapter.setOnClicListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent detailIntent = new Intent(SearchActivity.this, BookDetailsActivity.class);

                detailIntent.putExtra("IDLibro",  mAdapter.getItem(recycler.getChildAdapterPosition(view)).getIDLibro());
                SearchActivity.this.startActivity(detailIntent);
            }
        });

        TextSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i==KeyEvent.KEYCODE_ENTER){
                    CargarPorFiltro(TextSearch.getText().toString());
                }
                return false;
            }
        });

    };


    private void CargarPorFiltro(String nombreLibro) {
        Query q1 = db.collection("Books").whereEqualTo("Titulo", nombreLibro);
        FirestoreRecyclerOptions<Book> firestoreRecyclerOptionsfiltro = new FirestoreRecyclerOptions.Builder<Book>().setQuery(q1,Book.class).build();
        mAdapter = new bookAdapter(firestoreRecyclerOptionsfiltro);
        mAdapter.notifyDataSetChanged();
        recycler.setAdapter(mAdapter);
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
}

