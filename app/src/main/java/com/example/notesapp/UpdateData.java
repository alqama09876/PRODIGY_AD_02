package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateData extends AppCompatActivity {

    EditText edt_title, edt_content;
    ImageView btn_prev;
    AppCompatButton btn_update;
    FirebaseFirestore db;
    String id, title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_data);

        edt_title = findViewById(R.id.edt_title);
        edt_content = findViewById(R.id.edt_content);
        btn_update = findViewById(R.id.btn_update);
        btn_prev = findViewById(R.id.btn_prev);
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");

        edt_title.setText(title);
        edt_content.setText(content);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateData() {
        String newTitle = edt_title.getText().toString();
        String newContent = edt_content.getText().toString();

        if (newTitle.isEmpty() || newContent.isEmpty()) {
            Toast.makeText(this, "Both fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        DocumentReference docRef = db.collection("users").document(id);
        docRef.update("title", newTitle, "content", newContent)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UpdateData.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("id", id);
                            resultIntent.putExtra("title", newTitle);
                            resultIntent.putExtra("content", newContent);
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        } else {
                            Toast.makeText(UpdateData.this, "Update Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}