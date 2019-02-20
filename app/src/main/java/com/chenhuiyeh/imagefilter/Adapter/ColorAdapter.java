package com.chenhuiyeh.imagefilter.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chenhuiyeh.imagefilter.R;

import java.util.ArrayList;
import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {

    Context mContext;
    List<Integer> colorList;
    ColorAdapterListener listener;

    public ColorAdapter(Context context, ColorAdapterListener listener) {
        this.mContext = context;
        this.colorList = genColorList();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.color_item, parent, false);
        return new ColorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        holder.color_section.setCardBackgroundColor(colorList.get(position));

    }

    @Override
    public int getItemCount() {
        return colorList == null ? 0 : colorList.size();
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder {

        CardView color_section;

        public ColorViewHolder(View itemView) {
            super(itemView);
            color_section = itemView.findViewById(R.id.color_section);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onColorSelected(colorList.get(getAdapterPosition()));
                }
            });
        }
    }

    private List<Integer> genColorList() {
        List<Integer> colorList = new ArrayList<>();

        colorList.add(Color.parseColor("#131722"));
        colorList.add(Color.parseColor("#ff545e"));
        colorList.add(Color.parseColor("#57bb82"));
        colorList.add(Color.parseColor("#dbeeff"));
        colorList.add(Color.parseColor("#ba5796"));
        colorList.add(Color.parseColor("#bb349b"));
        colorList.add(Color.parseColor("#ffff99"));
        colorList.add(Color.parseColor("#ffcccc"));
        colorList.add(Color.parseColor("#33ffff"));
        colorList.add(Color.parseColor("#800040"));
        colorList.add(Color.parseColor("#d2b48c"));
        colorList.add(Color.parseColor("#00c2d1"));
        colorList.add(Color.parseColor("#00d178"));

        return colorList;

    }

    public interface ColorAdapterListener {
        void onColorSelected(int color);
    }
}
