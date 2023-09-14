package com.example.ewardrobe.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ewardrobe.Fragments.OutfitFragment;
import com.example.ewardrobe.Fragments.PrendasFragment;
import com.example.ewardrobe.R;

import java.util.List;

public class FiltroAdapter extends RecyclerView.Adapter<FiltroAdapter.ViewHolder> implements  View.OnClickListener {

    private List<String> botones;

    private String tipo;

    private Context context;

    private View.OnClickListener listener;

    private PrendasFragment prendasFragment;
    private OutfitFragment outfitFragment;

    private boolean outfit = false;

    public FiltroAdapter (List<String> caracteristicas, Context context, PrendasFragment fragment, String tipo) {
        this.botones = caracteristicas;
        this.context = context;
        this.prendasFragment = fragment;
        this.tipo = tipo;
    }

    public FiltroAdapter (List<String> caracteristicas, Context context, OutfitFragment fragment, String tipo) {
        this.botones = caracteristicas;
        this.context = context;
        this.outfitFragment = fragment;
        this.tipo = tipo;
        outfit = true;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarjeta_button, parent, false);
        view.setOnClickListener(this);
        return new FiltroAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FiltroAdapter.ViewHolder holder, int position) {
        String item = botones.get(position);
        holder.button.setText(item);

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = holder.button;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (button.getBackgroundTintList().getDefaultColor() == context.getResources().getColor(R.color.green_strong)) {
                        button.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green_clear)));
                        if(outfit){
                            outfitFragment.filtradoBotones(button.getText().toString(), "no-filtrar", tipo);
                        }else {
                            prendasFragment.filtradoBotones(button.getText().toString(), "no-filtrar", tipo);
                        }
                    } else {
                        button.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green_strong)));
                        if(outfit){
                            outfitFragment.filtradoBotones(button.getText().toString(), "filtrar", tipo);
                        }else {
                            prendasFragment.filtradoBotones(button.getText().toString(), "filtrar", tipo);
                        }

                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return botones.size();
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

        private Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.boton);
        }
    }
}
