package com.example.salvadorelizarraras.bakingapp3;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.salvadorelizarraras.bakingapp3.Recipe.Recipe;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Salvador Elizarraras on 06/03/2018.
 */

public class AdapterHome extends RecyclerView.Adapter<AdapterHome.MyViewHolder> {



    private ArrayList<Recipe> mData;
    private Context mContext;
    private Listeners listener;
    private Recipe item;

    public void setListener(Listeners listener){
        this.listener = listener;
    }

    public void swapData(ArrayList<Recipe> mData){
        if (mData == null) return;

        this.mData = mData;

        this.notifyDataSetChanged();
    }


    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.home_item,parent,false);

        return new MyViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TypedArray image = mContext.getResources().obtainTypedArray(R.array.images);
        holder.itemView.setTag(position);
        holder.mTitle.setText(mData.get(position).getName());
        Bitmap bitmap = null;
        try {

            bitmap = (mData.get(position).getImage().isEmpty() || mData.get(position).getImage() == null) ? drawableToBitmap(image.getDrawable(position)):
                    Picasso.get().load(mData.get(position).getImage().toString()).get();
        }catch(IOException e){
            e.getStackTrace();
        }

        holder.mImage.setImageBitmap(bitmap);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
            }
        });
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    @Override
    public int getItemCount() {
        return (mData == null ) ? 0 : mData.size();
    }

    public Recipe getItem(int position) {
        return mData.get(position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.mTitle)
        TextView mTitle;
        @BindView(R.id.mImageView)
        ImageView mImage;

        public MyViewHolder(View itemView , AdapterHome context) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }


    }
    public interface Listeners {
        void onClick(View view);
    }
}
