package com.example.notesapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.Display_Data;
import com.example.notesapp.R;
import com.example.notesapp.UpdateData;
import com.example.notesapp.Model.model;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    ArrayList<model> arrayList = new ArrayList<>();
    Context context;
    FirebaseFirestore db;

    public MyAdapter(Context context, ArrayList<model> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(arrayList.get(position).getTitle());
        holder.content.setText(arrayList.get(position).getContent());

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String documentId = arrayList.get(position).getId();
                db.collection("users").document(documentId)
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            arrayList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, arrayList.size());
                            ((Display_Data) context).refreshData();
                        })
                        .addOnFailureListener(e -> {
                            // Handle failure
                        });
            }
        });

        holder.rl_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateData.class);
                intent.putExtra("id", arrayList.get(position).getId());
                intent.putExtra("title", arrayList.get(position).getTitle());
                intent.putExtra("content", arrayList.get(position).getContent());
//                context.startActivity(intent);
                ((Display_Data) context).startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, content;
        ImageView btn_delete;
        RelativeLayout rl_items;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            rl_items = itemView.findViewById(R.id.rl_items);
        }
    }
}