package com.example.tugasbesar.foodreservationdelivery.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tugasbesar.foodreservationdelivery.MenuActivity;
import com.example.tugasbesar.foodreservationdelivery.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by uber on 12/12/16.
 */

public class MenuItemAdapter extends ArrayAdapter<com.example.tugasbesar.foodreservationdelivery.models.MenuItem> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<com.example.tugasbesar.foodreservationdelivery.models.MenuItem> mGridData;

    public MenuItemAdapter(Context mContext, int layoutResourceId, ArrayList<com.example.tugasbesar.foodreservationdelivery.models.MenuItem> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }

    public void setGridData(ArrayList<com.example.tugasbesar.foodreservationdelivery.models.MenuItem> mGridData){
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if(row == null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleTextNama = (TextView) row.findViewById(R.id.grid_item_title);
            holder.txtHarga=(TextView) row.findViewById(R.id.grid_item_harga);
            holder.imageMenu=(ImageView) row.findViewById(R.id.grid_item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        com.example.tugasbesar.foodreservationdelivery.models.MenuItem item = (com.example.tugasbesar.foodreservationdelivery.models.MenuItem) mGridData.get(position);
        holder.titleTextNama.setText(Html.fromHtml(item.getNama_produk()));
        holder.txtHarga.setText(Html.fromHtml(item.getHarga_produk()));

        Glide.with(mContext).
                load(item.getGambar_produk()).
                placeholder(R.drawable.ic_loading).
                into(holder.imageMenu);
        return row;
    }

    static class ViewHolder{
        TextView titleTextNama, txtHarga;
        ImageView imageMenu;
    }
}
