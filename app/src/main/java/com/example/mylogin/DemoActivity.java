package com.example.mylogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mylogin.adapter.bookAdapter;
import com.example.mylogin.model.Book;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import android.os.Bundle;

public class DemoActivity extends AppCompatActivity {
    RecyclerView mRecycler;
    bookAdapter mAdapter;
    FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        mFirestore=FirebaseFirestore.getInstance();
        mRecycler=findViewById(R.id.recyclerviewbooks);
        //mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setLayoutManager(new GridLayoutManager(this,2));
        Query query = mFirestore.collection("Books");

        FirestoreRecyclerOptions<Book> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Book>().setQuery(query,Book.class).build();
        mAdapter = new bookAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);



    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}