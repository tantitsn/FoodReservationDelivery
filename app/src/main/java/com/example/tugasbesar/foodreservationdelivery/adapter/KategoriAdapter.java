package com.example.tugasbesar.foodreservationdelivery.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tugasbesar.foodreservationdelivery.R;
import com.example.tugasbesar.foodreservationdelivery.models.KategoriItem;

import java.util.ArrayList;

/**
 * Created by tanti on 12/12/16.
 */
public class KategoriAdapter extends ArrayAdapter<KategoriItem>{
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<KategoriItem>mGridData;


    public KategoriAdapter(Context mContext, int layoutResourceId, ArrayList<KategoriItem> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId= layoutResourceId;
        this.mContext= mContext;
        this.mGridData = mGridData;
    }
    public void setGridData(ArrayList<KategoriItem> mGridData)
    {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if(row == null)
        {
            LayoutInflater inflater=((Activity)mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId,parent,false);
            holder = new ViewHolder();
            holder.titleTextView= (TextView) row.findViewById(R.id.grid_item_title);
            holder.imageView= (ImageView) row.findViewById(R.id.grid_item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        KategoriItem item = mGridData.get(position);
        holder.titleTextView.setText(item.getNama_kategori());

        Glide.with(mContext).
                load(item.getGambar_kategori()).
                placeholder(R.drawable.ic_loading).
                error(R.drawable.ic_loading).
                into(holder.imageView);

        return row;
    }



    static class ViewHolder {
        TextView titleTextView , txtNama;
        ImageView imageView;

    }
}
