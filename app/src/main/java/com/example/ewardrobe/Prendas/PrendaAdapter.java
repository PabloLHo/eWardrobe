package com.example.ewardrobe.Prendas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ewardrobe.R;

import java.util.List;

public class PrendaAdapter extends RecyclerView.Adapter<PrendaAdapter.ViewHolder> implements  View.OnClickListener {

    private List<Prenda> prendas;
    private Context context;

    private View.OnClickListener listener;

    public PrendaAdapter(List<Prenda> prendas, Context context) {
        this.prendas = prendas;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarjeta_prenda, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return prendas.size();
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

        private ImageView img;
        private TextView txt;
        private TextView color;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //img = itemView.findViewById(R.id.txt_foto);
        }
    }

}
