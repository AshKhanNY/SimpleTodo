package com.example.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Responsible for taking data in a particular position and putting it into a view holder
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }
    public interface OnClickListener {
        void onItemClicked(int position);
    }

    List<String> items;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;

    public ItemsAdapter(List<String> items, OnLongClickListener longClickListener,
                        OnClickListener clickListener) {
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    // Holds view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Use layout inflater to inflate a view
        View todoView = LayoutInflater.from(parent.getContext()).inflate
                (android.R.layout.simple_list_item_1, parent, false);
        // Wrap it inside a view holder and return it
        return new ViewHolder(todoView);
    }

    // Binds data to a specific view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Retrieve the item from position
        String tempItem = items.get(position);
        // Bind item to the specific view holder
        holder.bind(tempItem);
    }

    // Tells recycler view how many items are in list
    @Override
    public int getItemCount() {
        return items.size();
    }

    // Container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItem = itemView.findViewById(android.R.id.text1);
        }

        // Update view in ViewHolder with new data
        public void bind(String tempItem) {
            textViewItem.setText(tempItem);
            textViewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Let listener know what position was clicked
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
            textViewItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    // Let listener know what position was long pressed
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
