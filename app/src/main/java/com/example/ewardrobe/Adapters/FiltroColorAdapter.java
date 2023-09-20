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

public class FiltroColorAdapter extends RecyclerView.Adapter<FiltroColorAdapter.ViewHolder> implements  View.OnClickListener {

    private List<String> botones;

    private Context context;

    private View.OnClickListener listener;

    private PrendasFragment prendasFragment;
    private OutfitFragment outfitFragment;

    private int outfit = 0;

    public FiltroColorAdapter(List<String> caracteristicas, Context context, PrendasFragment fragment) {
        this.botones = caracteristicas;
        this.context = context;
        this.prendasFragment = fragment;
    }

    public FiltroColorAdapter (List<String> caracteristicas, Context context, OutfitFragment fragment) {
        this.botones = caracteristicas;
        this.context = context;
        this.outfitFragment = fragment;
        outfit = 1;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarjeta_button, parent, false);
        view.setOnClickListener(this);
        return new FiltroColorAdapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull FiltroColorAdapter.ViewHolder holder, int position) {
        String item = botones.get(position);
        holder.button.setText(item);
        if(item.equals("Negro")){
            holder.button.setTextColor(context.getResources().getColor(R.color.white));
        }
        int colorIdCompat = context.getResources().getIdentifier(item, "color", context.getPackageName());
        holder.button.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(colorIdCompat)));
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = holder.button;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (button.getBackgroundTintList().getDefaultColor() == context.getResources().getColor(R.color.Gris_strong)) {
                        int colorId = context.getResources().getColor(context.getResources().getIdentifier(item, "color", context.getPackageName()));
                        button.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(colorIdCompat)));
                        if(outfit == 1){
                            outfitFragment.filtradoBotones(button.getText().toString(), "no-filtrar", "color");
                        }else if(outfit == 0) {
                            prendasFragment.filtradoBotones(button.getText().toString(), "no-filtrar", "color");
                        }
                    } else {
                        button.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.Gris_strong)));
                        if(outfit == 1){
                            outfitFragment.filtradoBotones(button.getText().toString(), "filtrar", "color");
                        }else if(outfit == 0) {
                            prendasFragment.filtradoBotones(button.getText().toString(), "filtrar", "color");
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
