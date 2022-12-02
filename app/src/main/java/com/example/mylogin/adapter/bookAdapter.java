package com.example.mylogin.adapter;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mylogin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.example.mylogin.model.Book;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import io.reactivex.rxjava3.annotations.NonNull;


public class bookAdapter extends FirestoreRecyclerAdapter<Book,bookAdapter.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public bookAdapter(@androidx.annotation.NonNull FirestoreRecyclerOptions<Book> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder (@NonNull ViewHolder viewHolder, int i , @NonNull Book book)
    {
    ViewHolder.autor.setText(book.getAutor());
    ViewHolder.nombre.setText(book.getNombre());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_book_single,parent,false);
        return new ViewHolder(v);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        static TextView autor;
        static TextView nombre;
        public ViewHolder (@NonNull View itemView){
        super(itemView);
        nombre = itemView.findViewById(R.id.txtNombre);
        autor = itemView.findViewById(R.id.txtAutor);
        }

    }


}
