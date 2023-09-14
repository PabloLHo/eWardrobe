package com.example.ewardrobe.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ewardrobe.R;

import java.util.List;

public class CaracAdapter extends RecyclerView.Adapter<CaracAdapter.ViewHolder> implements  View.OnClickListener{

        private List<String> caracteristicas;

        private Context context;

        private View.OnClickListener listener;

        public CaracAdapter ( List<String> caracteristicas, Context context) {
            this.caracteristicas = caracteristicas;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarjeta_caracteristica, parent, false);
            view.setOnClickListener(this);
            return new CaracAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CaracAdapter.ViewHolder holder, int position) {
            String item = caracteristicas.get(position);
            holder.textView.setText("• " + item);
        }

        @Override
        public int getItemCount() {
            return caracteristicas.size();
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

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textView);
            }
        }

}
