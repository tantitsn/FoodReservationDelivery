package com.example.tugasbesar.foodreservationdelivery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tugasbesar.foodreservationdelivery.BillActivity;
import com.example.tugasbesar.foodreservationdelivery.R;
import com.example.tugasbesar.foodreservationdelivery.configs.Konfigurasi;
import com.example.tugasbesar.foodreservationdelivery.configs.SessionManagement;
import com.example.tugasbesar.foodreservationdelivery.models.CartItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by amarullah87 on 16/05/16.
 */
public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.CustomViewHolder> {

    private List<CartItem> feedItemList;
    private Context mContext;
    private SessionManagement session;


    public CartRecyclerAdapter(Context context, List<CartItem> feedItemList){
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_pemesanan, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        session = new SessionManagement(mContext);
        HashMap<String, String> user = session.getUserDetails();
        final String meja = user.get(SessionManagement.KEY_MEJA);
        final CartItem cartItem = feedItemList.get(i);

        Glide.with(mContext).load(cartItem.getGambar_produk())
                .error(R.drawable.img_loading)
                .placeholder(R.drawable.img_loading)
                .into(customViewHolder.thumbCart);

        customViewHolder.txtTitle.setText(Html.fromHtml(cartItem.getNama_produk()));
        customViewHolder.txtHarga.setText("Rp. " + Html.fromHtml(cartItem.getHarga_produk()));
        customViewHolder.txtQty.setText("x " + Html.fromHtml(cartItem.getQty()));
        customViewHolder.txtSubTotal.setText("Rp. " + Html.fromHtml(cartItem.getSubtotal()));
        customViewHolder.imgHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapusPesanan(cartItem.getId_produk(), meja);
            }
        });
    }

    private void hapusPesanan(String id_produk, String meja) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("id_produk", id_produk)
                .add("id_meja", meja)
                .build();
        Request request = new Request.Builder()
                .url(Konfigurasi.DELETE_TEMP)
                .post(requestBody)
                .build();

        try{
            Response response = client.newCall(request).execute();
            String respon = response.body().string();
            JSONObject jsonObject = new JSONObject(respon);
            int sukses = jsonObject.getInt(Konfigurasi.TAG_SUKSES);
            if(sukses == 1){
                Toast.makeText(mContext,"Pesanan Dibatalkan", Toast.LENGTH_LONG).show();
                BillActivity.me.refreshItem();
            }else{
                Toast.makeText(mContext,"Pesanan Tidak Dibatalkan", Toast.LENGTH_LONG).show();
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        protected ImageView thumbCart, imgHapus;
        protected TextView txtTitle, txtHarga, txtQty, txtSubTotal;

        public CustomViewHolder(View view) {
            super(view);

            this.thumbCart = (ImageView)view.findViewById(R.id.thumbCart);
            this.imgHapus = (ImageView)view.findViewById(R.id.imgHapus);

            this.txtTitle = (TextView)view.findViewById(R.id.txtTitle);
            this.txtHarga = (TextView)view.findViewById(R.id.txt_harga);
            this.txtQty = (TextView)view.findViewById(R.id.txt_qty);
            this.txtSubTotal = (TextView)view.findViewById(R.id.txt_subtotal);
        }
    }
}
