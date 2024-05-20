package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.Adapter.MyAdapter;
import com.example.notesapp.Model.model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Display_Data extends AppCompatActivity {

    ImageView btn_add;
    RecyclerView rv_view;
    FirebaseFirestore db;
    MyAdapter adapter;
    ArrayList<model> arrayList;
    private static final int REQUEST_CODE_UPDATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        btn_add = findViewById(R.id.btn_add);
        db = FirebaseFirestore.getInstance();
        rv_view = findViewById(R.id.rv_view);
        rv_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        loadData();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Display_Data.this, Notes.class));
            }
        });
    }

    private void loadData() {
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    arrayList = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshots : task.getResult()) {
                        model userModel = documentSnapshots.toObject(model.class);
                        userModel.setId(documentSnapshots.getId());
                        arrayList.add(userModel);
                    }
                    adapter = new MyAdapter(Display_Data.this, arrayList);
                    rv_view.setAdapter(adapter);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UPDATE && resultCode == RESULT_OK && data != null) {
            String id = data.getStringExtra("id");
            String newTitle = data.getStringExtra("title");
            String newContent = data.getStringExtra("content");

            for (model item : arrayList) {
                if (item.getId().equals(id)) {
                    item.setTitle(newTitle);
                    item.setContent(newContent);
                    break;
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    public void refreshData() {
        loadData();
    }
}