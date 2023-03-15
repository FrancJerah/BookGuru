package com.example.bookguru;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button CreateNewEmp, ManageRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CreateNewEmp = (Button) findViewById(R.id.CreateNewEmp);
        ManageRecords = (Button) findViewById(R.id.ManageRecords);

        CreateNewEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, CreateNewRecord.class);
                startActivity(in);
            }
        });
        ManageRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, ManageRecords.class);
            }
        });
    }
}