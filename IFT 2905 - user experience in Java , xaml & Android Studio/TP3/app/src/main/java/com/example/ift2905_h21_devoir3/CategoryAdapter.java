package com.example.ift2905_h21_devoir3;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private CategoryModel[] models;
    private Context context;

    public CategoryAdapter(Context context, CategoryModel[] models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_category, parent, false);
        // Return a new view holder

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindData(models[position], context);
    }

    @Override
    public int getItemCount() {
        return models.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView cardImageView;
        public TextView titleTextView;
        public FloatingActionButton fav;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImageView = itemView.findViewById(R.id.image);
            titleTextView = itemView.findViewById(R.id.title);
            fav = itemView.findViewById(R.id.fav);
        }

        public void bindData(CategoryModel model, Context context) {
            cardImageView.setImageDrawable(ContextCompat.getDrawable(context, model.getImage()));
            titleTextView.setText(model.getTitle());

            final TypedValue colorAccent = new TypedValue ();
            final TypedValue colorPrimary = new TypedValue ();
            context.getTheme().resolveAttribute(R.attr.colorAccent, colorAccent, true);
            context.getTheme().resolveAttribute(R.attr.colorPrimary, colorPrimary, true);

            if (model.isFav()) {
                fav.setImageResource(R.drawable.favorite_checked);
                fav.setImageTintList(ColorStateList.valueOf(colorPrimary.data));
            } else {
                fav.setImageResource(R.drawable.favorite_checked);
                fav.setImageTintList(ColorStateList.valueOf(colorAccent.data));
            }

            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    model.setFav(!model.isFav());

                    if (model.isFav()) {
                        fav.setImageResource(R.drawable.favorite_checked);
                        fav.setImageTintList(ColorStateList.valueOf(colorPrimary.data));
                    } else {
                        fav.setImageResource(R.drawable.favorite_checked);
                        fav.setImageTintList(ColorStateList.valueOf(colorAccent.data));
                    }
                }
            });
        }
    }
}
