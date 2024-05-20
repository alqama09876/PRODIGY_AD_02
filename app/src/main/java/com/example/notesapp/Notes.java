package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Notes extends AppCompatActivity {
    EditText edt_title, edt_content;
    ImageView btn_submit, btn_display;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        edt_title = findViewById(R.id.edt_title);
        edt_content = findViewById(R.id.edt_content);
        btn_submit = findViewById(R.id.btn_submit);
        btn_display = findViewById(R.id.btn_display);
        db = FirebaseFirestore.getInstance();

        btn_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Notes.this, Display_Data.class));
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edt_title.getText().toString();
                String content = edt_content.getText().toString();

                if (title.isEmpty() || content.isEmpty()) {
                    edt_title.setError("Enter title");
                    edt_content.setError("Enter content");
                } else {
                    saveData();
                }
            }
        });
    }

    private void saveData() {
        String id = UUID.randomUUID().toString();
        String title = edt_title.getText().toString();
        String content = edt_content.getText().toString();

        Map<String, Object> user = new HashMap<>();

        user.put("id", id);
        user.put("title", title);
        user.put("content", content);

        DocumentReference documentReference = db.collection("users").document();

        documentReference.set(user).addOnCompleteListener(Notes.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Notes.this, "Data Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                    edt_title.setText("");
                    edt_content.setText("");
                } else {
                    Toast.makeText(Notes.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}