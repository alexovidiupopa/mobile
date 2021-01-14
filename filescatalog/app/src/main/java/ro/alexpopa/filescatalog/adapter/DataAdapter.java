package ro.alexpopa.filescatalog.adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ro.alexpopa.filescatalog.R;
import ro.alexpopa.filescatalog.model.File;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private List<File> items;
    private List<String> fields;
    public DataAdapter() {
        items = new ArrayList<>();
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public void setItems(List<File> books) {
        this.items = books;
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

        String contentText = "";
        for(String field:fields){
            if(field.equals("name")){
                contentText+=items.get(position).getName()+ " ";
            }
            else if(field.equals("status")){
                contentText+=items.get(position).getStatus()+ " ";
            }
            else if(field.equals("size")){
                contentText+=items.get(position).getSize()+ " ";
            }
            else if(field.equals("location")){
                contentText+=items.get(position).getLocation()+ " ";
            }
            else if(field.equals("usage")){
                contentText+=items.get(position).getUsage()+ " ";
            }

        }

        holder.mContentView.setText(contentText);

        holder.mView.setOnClickListener(v -> {

        });

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
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public File mItem;

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
