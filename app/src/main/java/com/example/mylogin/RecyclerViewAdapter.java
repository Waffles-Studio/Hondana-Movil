package com.example.mylogin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolderData> {

    ArrayList<String> listData;

    public RecyclerViewAdapter(ArrayList<String> listData) {
        this.listData = listData;
    }

    @NonNull
    @Override
    public ViewHolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_book_content,null, false);
        return new ViewHolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderData holder, int position) {
        holder.asignData(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolderData extends RecyclerView.ViewHolder {

        TextView Data;

        public ViewHolderData(@NonNull View itemView) {
            super(itemView);
            Data = itemView.findViewById(R.id.txtData);

        }

        public void asignData(String sdata) {
            Data.setText(sdata);
        }
    }
}
