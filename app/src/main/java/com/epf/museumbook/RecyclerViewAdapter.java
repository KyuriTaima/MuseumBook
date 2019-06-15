package com.epf.museumbook;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.epf.museumbook.Modeles.DatabaseSQLiteHelper;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private ArrayList<String> mTitles = new ArrayList<>();
    private ArrayList<String> mDescriptions = new ArrayList<>();
    private ArrayList<String> mAddresses = new ArrayList<>();
    //private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mRessources = new ArrayList<>();
    private Context mcontext;

    public RecyclerViewAdapter(ArrayList<String> mTitles, ArrayList<String> mDescriptions, ArrayList<String> mAddresses, Context mcontext, ArrayList<String> mRessources) {
        this.mTitles = mTitles;
        this.mDescriptions = mDescriptions;
        this.mAddresses = mAddresses;
        //this.mImages = mImages;
        this.mRessources = mRessources;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.layout_lisitem,viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,final int i) {
         //Bind the images

        //Glide.with(mcontext).asBitmap().load(mImages.get(i)).into(viewHolder.image);
        try {
            Glide.with(mcontext)
                    .load(mRessources.get(i-1))
                    .into(viewHolder.image);
        }catch (Exception e){
            System.out.println(e.getMessage() + "Recycler View Holder onBindViewHolder");
            Glide.with(mcontext)
                    .load("https://www.flaticon.com/free-icon/museum_236981")
                    .into(viewHolder.image);
        }
        viewHolder.title.setText(mTitles.get(i));
        viewHolder.description.setText(mDescriptions.get(i));
        viewHolder.address.setText(mAddresses.get(i));

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseSQLiteHelper db = new DatabaseSQLiteHelper(mcontext);
                Intent intent = new Intent(mcontext, MuseumActivity.class);
                intent.putExtra("MUSEUM_ID", db.getLastMuseumRank()-i);
                mcontext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTitles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView title;
        TextView address;
        TextView description;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.icon_recycler_view);
            title = itemView.findViewById(R.id.title_recycler_view);
            address = itemView.findViewById(R.id.address_recycler_view);
            description = itemView.findViewById(R.id.description_recycler_view);
            layout = itemView.findViewById(R.id.layout_recycler_view);
        }
    }
}
