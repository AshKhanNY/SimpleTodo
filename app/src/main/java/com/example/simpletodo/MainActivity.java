package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // For intent to pass value from this instance to EditActivity
    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POS = "item_pos";
    // Code to distinguish different intents (there is only one activity requested, so it's
    // arbitrary but nevertheless important).
    public static final int EDIT_TEXT_CODE = 51120;

    // List to hold all items for to-do list
    List<String> items;
    // Adding all member variables from the view
    Button addButton;
    EditText inputItemField;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Loads items from previous state, or initializes new list
        loadItems();

        // Initialize view variables
        addButton = findViewById(R.id.buttonAdd);
        inputItemField = findViewById(R.id.editTextItem);
        rvItems = findViewById(R.id.rvItems);

        // Responsible for adding items to list
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currListItem = inputItemField.getText().toString();
                // Add the item to model view
                items.add(currListItem);
                // Notify the adapter that an item has been added
                itemsAdapter.notifyItemInserted(items.size() - 1);
                inputItemField.setText("");
                Toast.makeText(getApplicationContext(), "Successfully added item.", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });

        // Responsible for removing items from list
        ItemsAdapter.OnLongClickListener longClickListener = new ItemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                // Remove item from model view, notify adapter where item was deleted
                items.remove(position);
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item deleted!", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        // Creates item adapter, sets adapter and vertical layout manager to recycler view
        ItemsAdapter.OnClickListener clickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                // Edit item in model view
                Log.d("Main Activity", "You are in edit mode.");
                // Create new activity
                // Creates an intent from the current instance to where we want to go
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                // Pass data which will be altered
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POS, position);
                // Display activity on screen
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };
        itemsAdapter = new ItemsAdapter(items, longClickListener, clickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
    }

    // Handle result of edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            // Retrieve updated text value
            String text = data.getStringExtra(KEY_ITEM_TEXT);
            // Extract original position of edited item from position key
            int pos = data.getExtras().getInt(KEY_ITEM_POS);
            // Update model at right position with new item text
            items.set(pos, text);
            // Notify adapter so that recycler view notices a change
            itemsAdapter.notifyItemChanged(pos);
            // Save changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated!", Toast.LENGTH_SHORT).show();
        } else Log.w("Main Activity", "Unknown call to onActivityResult");
    }

    // Returns file where data of to-do list is kept
    protected File getDataFile() {
        return new File(getFilesDir(), "todoListData.txt");
    }
    // Loads items by reading every line in data file
    protected void loadItems(){
        // Reads file and populations items into a new array list with a desired char set
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading the items.", e);
            items = new ArrayList<>();
        }
    }
    // Writes items into data file
    protected void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error saving/writing the items.", e);
        }

    }
}