package ro.alexpopa.onlineshop.adapter;



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

import ro.alexpopa.onlineshop.NetworkingManager;
import ro.alexpopa.onlineshop.R;
import ro.alexpopa.onlineshop.model.Order;
import ro.alexpopa.onlineshop.model.User;


public class UserDataAdapter extends RecyclerView.Adapter<UserDataAdapter.ViewHolder> {

    private List<User> items;
    private NetworkingManager manager;

    public UserDataAdapter() {
        items = new ArrayList<>();
    }

    public void setItems(List<User> items) {
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
        holder.mIdView.setText("User: " + String.valueOf(items.get(position).getId()));
        holder.mContentView.setText(" Orders: " + String.valueOf(items.get(position).getNumberOfOrders()));

        holder.mView.setOnLongClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("Are you sure you want to delete " + holder.mItem.getId() + "?")
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

    public void setManager(NetworkingManager manager) {
        this.manager = manager;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public User mItem;

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
