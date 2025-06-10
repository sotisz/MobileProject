package com.example.mobileproject2;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChecklistActivity extends AppCompatActivity {
    ImageButton btnBefore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailymission);

        btnBefore = findViewById(R.id.btnBefore);
        btnBefore.setOnClickListener(v -> finish());

    }
}
