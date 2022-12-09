package com.example.mylogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mylogin.adapter.bookAdapter;
import com.example.mylogin.model.Book;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


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
                Toast.makeText(SearchActivity.this, "Se escribio algo siaodknas", Toast.LENGTH_SHORT).show();

                return false;
            }
        });

    };

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

