package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText editItem;
    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editItem = findViewById(R.id.editUpdateText);
        btnUpdate = findViewById(R.id.editItemButton);

        getSupportActionBar().setTitle("Edit Item");
        editItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));
        // Determines what to do when button for editing is clicked
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent which will contain results
                Intent i = new Intent();
                // Pass results of editing
                i.putExtra(MainActivity.KEY_ITEM_TEXT, editItem.getText().toString());
                i.putExtra(MainActivity.KEY_ITEM_POS,
                        getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POS));
                // Set result of the intent
                setResult(RESULT_OK, i);
                // Finish the activity (close screen and return to prev page)
                finish();
            }
        });
    }
}