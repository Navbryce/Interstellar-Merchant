package com.cs2340.interstellarmerchant.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cs2340.interstellarmerchant.R;
import com.cs2340.interstellarmerchant.viewmodels.ItemClickListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * class for handling recycler view in load view
 */
public class LoadRecyclerViewAdapter extends RecyclerView.Adapter<LoadRecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "LoadRecyclerViewAd";

    public ArrayList<String> names = new ArrayList<>();
    public ArrayList<String> times = new ArrayList<>();
    private Context itemContext;


    /**
     * constructor
     * @param names name
     * @param times times
     * @param itemContext contexts
     */
    public LoadRecyclerViewAdapter(ArrayList<String> names, ArrayList<String> times, Context itemContext) {
        this.names = names;
        this.times = times;
        this.itemContext = itemContext;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.load_games, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: called");
        viewHolder.name.setText(names.get(i));
        viewHolder.time.setText(times.get(i));

        viewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int i, boolean isLongClick) {
                ((Loadgame) itemContext).changeInput(names.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        private ItemClickListener itemClickListener;
        public TextView name;
        TextView time;
        TextView name_prompt;
        TextView time_prompt;
        RelativeLayout loadLayout;

        /**
         * constructor
         * @param itemView item view
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.names);
            name_prompt = itemView.findViewById(R.id.name_prompt);
            time = itemView.findViewById(R.id.times);
            time_prompt = itemView.findViewById(R.id.time_prompt);
            loadLayout = itemView.findViewById(R.id.load_parent_layout);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }


}