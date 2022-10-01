package com.jss.smartdustbin.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jss.smartdustbin.R;
import com.jss.smartdustbin.activity.DustbinDetailsActivity;
import com.jss.smartdustbin.model.Dustbin;
import com.jss.smartdustbin.utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class DustbinsAdapter extends RecyclerView.Adapter<DustbinsAdapter.MyViewHolder> {
    private Context context;
    private List<Dustbin> dustbinList;
    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView garbageLevelTv;
        TextView lastUpdatedTv;
        ImageView alertIcon;
        View dustbinDetailsCard;
        TextView binTv;

        public MyViewHolder(View view) {
            super(view);
            garbageLevelTv = view.findViewById(R.id.dustbin_level_tv);
            lastUpdatedTv = view.findViewById(R.id.last_updated_tv);
            binTv = view.findViewById(R.id.bin_tv);
            alertIcon = view.findViewById(R.id.info_icon);
            dustbinDetailsCard = view.findViewById(R.id.dustbin_details_card);
            context = view.getContext();

        }
    }
    public DustbinsAdapter(Context context, List<Dustbin> dustbinList) {
        this.context = context;
        this.dustbinList = new ArrayList<>(dustbinList);
    }

    public void setItems(List<Dustbin> dustbins){
        this.dustbinList = dustbins;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dustbin_details_card_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Dustbin dustbin =  dustbinList.get(position);
        int garbageStatus = Helper.getGarbageStatusFromLevel(dustbin.getGarbageLevel());
        if(garbageStatus == 1)
            holder.garbageLevelTv.setTextColor(Color.parseColor("#44A849"));
        else if(garbageStatus == 2)
            holder.garbageLevelTv.setTextColor(Color.parseColor("#FF8922"));
        else if(garbageStatus == 3)
            holder.garbageLevelTv.setTextColor(Color.parseColor("#E2574C"));


        holder.garbageLevelTv.setText(dustbin.getGarbageLevel()+ "%" + " full");
        holder.lastUpdatedTv.setText("Last updated " + dustbin.getLastUpdated());
        holder.binTv.setText("BIN: " + dustbin.getBin());
        holder.alertIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Message");
                builder.setMessage(dustbin.getComment());
                builder.setPositiveButton("OK", null);
                builder.setIcon(R.drawable.ic_message);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        holder.dustbinDetailsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DustbinDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("dustbin", dustbin);
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return dustbinList.size();
    }

}
