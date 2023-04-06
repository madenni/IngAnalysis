package com.example.inganapp;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private ArrayList name_id, id_ids, wrns;
    private final RecyclerViewInterface recyclerViewInterface;

    public MyAdapter(Context context, ArrayList name_id,ArrayList id_ids,ArrayList wrns, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.name_id = name_id;
        this.id_ids = id_ids;
        this.wrns = wrns;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.userentry,parent, false);
        return new MyViewHolder(v, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name_id.setText(String.valueOf(name_id.get(position)));
        holder.id_id.setText(String.valueOf(id_ids.get(position)));
        //holder.u.setText(String.valueOf(wrns.get(position)));

        if (String.valueOf(wrns.get(position)).equals("1")){
            holder.wrn.setVisibility(VISIBLE);
        }
        else holder.wrn.setVisibility(INVISIBLE);
        //switch (a){
          //  case 1:

            //case 0:

          //  }
        //holder.wrn.setVisibility(Integer.valueOf((String) wrns.get(position)));

    }

    @Override
    public int getItemCount() {
        return name_id.size();
    }

    public ArrayList getId_id(){ return id_ids;}

    @Override
    public void onClick(View v) {

    }
    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<String> filterdNames, ArrayList<String> filterdIDs, ArrayList<String> filterdWRN) {
        this.name_id = filterdNames;
        this.id_ids = filterdIDs;
        this.wrns = filterdWRN;
        notifyDataSetChanged();
    }
    /*public void filter(String text) {
        items.clear();
        if(text.isEmpty()){
            items.addAll(itemsCopy);
        } else{
            text = text.toLowerCase();
            for(PhoneBookItem item: itemsCopy){
                if(item.name.toLowerCase().contains(text) || item.phone.toLowerCase().contains(text)){
                    items.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }*/

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name_id, id_id, u;
        ImageView wrn;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            name_id = itemView.findViewById(R.id.textname);
            id_id = itemView.findViewById(R.id.textid);
            wrn = itemView.findViewById(R.id.imageView);
            u = itemView.findViewById(R.id.u);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.OnItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
