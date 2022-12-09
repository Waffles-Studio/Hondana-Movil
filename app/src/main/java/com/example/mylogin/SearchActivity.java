package com.example.mylogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mylogin.adapter.bookAdapter;
import com.example.mylogin.model.Book;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
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

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView recycler;
    private bookAdapter mAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText TextSearch;
    private GoogleSignInClient mGoogleSignClient;
    private FirebaseAnalytics mFirebaseAnalytics;

    private Button btnHome,btnSearch, btnCS;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        TextSearch = (EditText) findViewById(R.id.TxtSearch);
        recycler = (RecyclerView) findViewById(R.id.recyclerSearch);
        recycler.setLayoutManager(new GridLayoutManager(this,3));

        CargarPorFiltro(TextSearch.getText().toString());
        //Consulta de libros
        btnHome = (Button) findViewById(R.id.btnHome);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnCS = (Button) findViewById(R.id.btnCS);

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

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(SearchActivity.this, HomeActivity.class);
                SearchActivity.this.startActivity(detailIntent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(SearchActivity.this, SearchActivity.class);
                SearchActivity.this.startActivity(detailIntent);
            }
        });

        btnCS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleSignClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mFirebaseAnalytics = FirebaseAnalytics.getInstance(SearchActivity.this);
                            Bundle bundle = new Bundle();
                            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "SignOut");
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                            SearchActivity.this.startActivity(intent);

                        }
                    }
                });
            }
        });


        TextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                CargarPorFiltro(TextSearch.getText().toString());
            }
        });

    };


    private void CargarPorFiltro(String nombreLibro) {
        Query q1 ;
        if (nombreLibro.length() != 0){

            q1 = db.collection("Books").whereGreaterThanOrEqualTo("Titulo", nombreLibro);
        }else
        {
            q1 = db.collection("Books");
        }
        FirestoreRecyclerOptions<Book> firestoreRecyclerOptionsfiltro = new FirestoreRecyclerOptions.Builder<Book>().setQuery(q1,Book.class).build();
        mAdapter = new bookAdapter(firestoreRecyclerOptionsfiltro);
        mAdapter.startListening();
        mAdapter.notifyDataSetChanged();
        recycler.setAdapter(mAdapter);
    }


    @Override
    public void onBackPressed() {
        // do nothing.
    }

}