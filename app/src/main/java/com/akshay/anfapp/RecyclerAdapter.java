package com.akshay.anfapp;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.PersonViewHolder> {

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView productTopDescription;
        TextView productTitle;
        TextView productPromoMessage;
        TextView productBottomDescription;
        ImageView productPhoto;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            productPhoto = (ImageView) itemView.findViewById(R.id.product_photo);
            productTopDescription = (TextView) itemView.findViewById(R.id.product_top_description);
            productTitle = (TextView) itemView.findViewById(R.id.product_title);
            productPromoMessage = (TextView) itemView.findViewById(R.id.product_promo_message);
            productBottomDescription = (TextView) itemView.findViewById(R.id.product_bottom_description);
        }
    }

    List<AFResponseBean> responseBeanList;

    RecyclerAdapter(List<AFResponseBean> responseBeanList) {
        this.responseBeanList = responseBeanList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.productTopDescription.setText(responseBeanList.get(i).getTopDescription());
        personViewHolder.productTitle.setText(responseBeanList.get(i).getTitle());

    }

    @Override
    public int getItemCount() {
        return responseBeanList.size();
    }
}
