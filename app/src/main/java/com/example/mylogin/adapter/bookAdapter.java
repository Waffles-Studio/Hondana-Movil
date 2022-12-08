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


public class bookAdapter extends FirestoreRecyclerAdapter<Book,bookAdapter.ViewHolder>
implements View.OnClickListener {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private View.OnClickListener listener;
    public bookAdapter(@androidx.annotation.NonNull FirestoreRecyclerOptions<Book> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder (@NonNull ViewHolder viewHolder, int i , @NonNull Book book)
    {
    ViewHolder.Autor.setText(book.getAutor());
    ViewHolder.Titulo.setText(book.getTitulo());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_book_content,parent,false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    public void setOnClicListener(View.OnClickListener listener)
{
        this.listener=listener;
    }
    @Override
    public void onClick(View view) {
    if (listener!=null)
    {
        listener.onClick(view);
    }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        static TextView Autor;
        static TextView Titulo;
        public ViewHolder (@NonNull View itemView){
        super(itemView);
            Titulo = itemView.findViewById(R.id.txtData);
            Autor = itemView.findViewById(R.id.textView10);
        }

    }


}
