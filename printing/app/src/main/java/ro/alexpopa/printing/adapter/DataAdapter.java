package ro.alexpopa.printing.adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ro.alexpopa.printing.R;
import ro.alexpopa.printing.model.Model;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private List<Model> items;
    private List<String> fields;
    public DataAdapter() {
        items = new ArrayList<>();
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public void setItems(List<Model> books) {
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
            if(field.equals("model")){
                contentText+=items.get(position).getModel()+ " ";
            }
            else if(field.equals("status")){
                contentText+=items.get(position).getStatus()+ " ";
            }
            else if(field.equals("client")){
                contentText+=items.get(position).getClient()+ " ";
            }
            else if(field.equals("time")){
                contentText+=items.get(position).getTime()+ " ";
            }
            else if(field.equals("cost")){
                contentText+=items.get(position).getCost()+ " ";
            }

        }

        holder.mContentView.setText(contentText);

        holder.mView.setOnClickListener(v -> {

        });

        holder.mView.setOnLongClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("Are you sure you want to delete " + holder.mItem.getModel() + "?")
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
        public Model mItem;

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