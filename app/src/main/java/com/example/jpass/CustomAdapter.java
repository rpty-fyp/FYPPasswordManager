package com.example.jpass;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private String username;
    private ArrayList logID, logName, logPW, logDesc;

    CustomAdapter(Activity activity, Context context, ArrayList logID, ArrayList logName, ArrayList logPW, ArrayList logDesc, String username){
        this.activity = activity;
        this.context = context;
        this.logID = logID;
        this.logName = logName;
        this.logPW = logPW;
        this.logDesc = logDesc;
        this.username = username;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.entryrow, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_entryDesc.setText(String.valueOf(logDesc.get(position)));
        holder.tv_entryUser.setText(String.valueOf(logName.get(position)));
        holder.tv_entryPW.setText(String.valueOf(logPW.get(position)));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updateEntry = new Intent(context, EntryUpdate.class);
                updateEntry.putExtra("id", String.valueOf(logID.get(position)));
                updateEntry.putExtra("name", String.valueOf(logName.get(position)));
                updateEntry.putExtra("PW", String.valueOf(logPW.get(position)));
                updateEntry.putExtra("desc", String.valueOf(logDesc.get(position)));
                updateEntry.putExtra("profileUser", username);
                activity.startActivityForResult(updateEntry, 1);

            }
        });
    }



    @Override
    public int getItemCount() {
        return logID.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_entryDesc, tv_entryUser, tv_entryPW;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_entryDesc = (TextView) itemView.findViewById(R.id.tv_entryDesc);
            tv_entryUser = (TextView) itemView.findViewById(R.id.tv_entryUser);
            tv_entryPW = (TextView) itemView.findViewById(R.id.tv_entryPW);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }

}
