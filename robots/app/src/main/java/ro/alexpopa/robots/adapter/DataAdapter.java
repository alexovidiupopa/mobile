package ro.alexpopa.robots.adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ro.alexpopa.robots.NetworkingManager;
import ro.alexpopa.robots.R;
import ro.alexpopa.robots.model.Robot;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private List<Robot> items;
    private NetworkingManager manager;

    public DataAdapter() {
        items = new ArrayList<>();
    }

    public void setItems(List<Robot> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void clear(){
        this.items.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mItem = items.get(position);
        holder.mIdView.setText(String.valueOf(items.get(position).getId()));
        holder.mContentView.setText(items.get(position).getName() + " " + items.get(position).getSpecs() + " " + items.get(position).getType() + " " + items.get(position).getAge() + " " + items.get(position).getHeight());

        holder.mView.setOnLongClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("Are you sure you want to delete " + holder.mItem.getName() + "?")
                    .setPositiveButton("Yes",((dialog, which) -> {

                    }))
                    .setNegativeButton("No", (dialog, which) -> {

                    });
            builder.create().show();
            return true;
        });

        holder.mView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("New height:");
            final EditText input = new EditText(v.getContext());

            builder.setView(input);

            // Set up the button
            builder.setPositiveButton("OK", (dialog, which) -> {
                manager.updateHeight(new ProgressBar(v.getContext()),new Robot(holder.mItem.getId(),"","", Integer.parseInt(input.getText().toString()),0, ""), null);
                notifyDataSetChanged();
            });
            builder.create().show();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setManager(NetworkingManager manager) {
        this.manager = manager;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Robot mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.id);
            mContentView = view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
