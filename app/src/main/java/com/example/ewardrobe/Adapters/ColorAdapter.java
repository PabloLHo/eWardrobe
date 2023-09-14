package com.example.ewardrobe.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ewardrobe.BBDD.Color;
import com.example.ewardrobe.R;

import java.util.ArrayList;
import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> implements  View.OnClickListener {

    private List<Color> color;

    private Context context;

    private View.OnClickListener listener;

    public ColorAdapter ( List<String> colores, Context context) {
        color = new ArrayList<>();
        for (int i = 0; i < colores.size(); i++) {
            color.add(new Color(colores.get(i).replace(" ", ""), context.getResources().getIdentifier(colores.get(i).replace(" ", ""), "color", context.getPackageName())));
        }
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarjeta_color, parent, false);
        view.setOnClickListener(this);
        return new ColorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = color.get(position).getNombre();
        holder.textView.setText(item);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.imageView.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(color.get(position).getId())));
            holder.imageView.setBackground(context.getDrawable(R.drawable.circulo));
        }
    }

    @Override
    public int getItemCount() {
        return color.size();
    }


    public void setOnclickListener(View.OnClickListener listener){
        this.listener = listener;
    }
    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.colorTexto);
            imageView = itemView.findViewById(R.id.color);
        }
    }
}
