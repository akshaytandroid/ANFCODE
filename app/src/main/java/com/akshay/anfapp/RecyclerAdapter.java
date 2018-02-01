package com.akshay.anfapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.lang.reflect.Type;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private List<AFResponseBean> responseBeen;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView productTopDescription, productTitle, productPromoMessage, productBottomDescription;
        ImageView productPhoto;
        LinearLayout productContentLinearLayout;

        MyViewHolder(View view) {
            super(view);
            productPhoto = (ImageView) view.findViewById(R.id.product_photo);
            productTopDescription = (TextView) view.findViewById(R.id.product_top_description);
            productTitle = (TextView) view.findViewById(R.id.product_title);
            productPromoMessage = (TextView) view.findViewById(R.id.product_promo_message);
            productBottomDescription = (TextView) view.findViewById(R.id.product_bottom_description);
            productContentLinearLayout = (LinearLayout) view.findViewById(R.id.product_content_linear_layout);
        }
    }


    public RecyclerAdapter(Context mContext, List<AFResponseBean> responseBeanList) {
        this.mContext = mContext;
        this.responseBeen = responseBeanList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final AFResponseBean result = responseBeen.get(position);

        //GLIDE:  Glide is an Image Loader Library recommended by Google.
        Glide.with(mContext).load(result.getBackgroundImage()).into(holder.productPhoto);

        holder.productTopDescription.setText(result.getTopDescription());
        holder.productTopDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);

        holder.productTitle.setText(result.getTitle());
        holder.productTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        holder.productTitle.setTextColor(Color.BLACK);
        holder.productTitle.setTypeface(holder.productTitle.getTypeface(), Typeface.BOLD);

        holder.productPromoMessage.setText(result.getPromoMessage());
        holder.productPromoMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);



        String bottomDescription = result.getBottomDescription();
        if (bottomDescription != null) {
            holder.productBottomDescription.setVisibility(View.VISIBLE);
            holder.productBottomDescription.setMovementMethod(LinkMovementMethod.getInstance());
            holder.productBottomDescription.setText(Html.fromHtml(bottomDescription));
            holder.productBottomDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        }

        if (result.getContent() != null) {
            holder.productContentLinearLayout.removeAllViews();
            renderButtons(holder, result);
        }

    }

    private void renderButtons(MyViewHolder holder, final AFResponseBean result) {
        int numberOfButtonsInEachCard = result.getContent().size();

        for (int i = 0; i < numberOfButtonsInEachCard; i++) {
            Button contentButton = new Button(mContext);

            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                contentButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.button_background));
            } else {
                contentButton.setBackground(mContext.getResources().getDrawable(R.drawable.button_background));
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            layoutParams.setMargins(0, 8, 0, 8);
            contentButton.setLayoutParams(layoutParams);

            contentButton.setText(result.getContent().get(i).getTitle());
            contentButton.setId(i);
            contentButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            final int finalI = i;
            contentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (result.getContent().get(finalI).getTarget() != null) {
                        openWebUrl(result.getContent().get(finalI).getTarget());
                    }
                }
            });

            holder.productContentLinearLayout.addView(contentButton);
        }
    }

    private void openWebUrl(String targetURL) {
        Intent intent = new Intent();
        intent.setClass(mContext,WebviewActivity.class);
        intent.putExtra("url",targetURL);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return responseBeen.size();
    }
}
