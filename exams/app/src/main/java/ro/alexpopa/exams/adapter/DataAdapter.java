package ro.alexpopa.exams.adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ro.alexpopa.exams.NetworkingManager;
import ro.alexpopa.exams.R;
import ro.alexpopa.exams.model.Exam;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private List<Exam> items;
    private List<String> fields;
    private NetworkingManager manager;

    public void setManager(NetworkingManager manager) {
        this.manager = manager;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public DataAdapter() {
        items = new ArrayList<>();
    }

    public void setItems(List<Exam> books) {
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
        holder.mContentView.setText(items.get(position).getName() + " " + items.get(position).getGroup() + " " + items.get(position).getStatus() + " " + items.get(position).getStudents() + " " + items.get(position).getType());

        holder.mView.setOnClickListener(v -> {
            manager.getDetailsForExam(new ProgressBar(v.getContext()),null, holder.mItem.getId());
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
        public Exam mItem;

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
