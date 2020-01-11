package com.example.ofertasuv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<Promociones_Restaurantes> promos;
    onNoteListener listener;

    public RecyclerViewAdapter(Context context, List<Promociones_Restaurantes> promos, onNoteListener onNoteListener) {
        this.context = context;
        this.promos = promos;
        this.listener = onNoteListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.card_restaurantes,parent,false);
        return new MyViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.promo.setText(promos.get(position).getPromocion());
        holder.rest.setText(promos.get(position).getNombre_restaurante());
        holder.horario.setText(promos.get(position).getHorario());
        holder.dia.setText(promos.get(position).getDia());
        Picasso.with(context)
                .load(promos.get(position).getImagenNet())
                .fit()
                .centerCrop()
                .into(holder.imagen);
    }

    @Override
    public int getItemCount() {
        return promos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView promo,rest,dia,horario;
        ImageView imagen;
        onNoteListener onNoteListener;
        public MyViewHolder(@NonNull View itemView, onNoteListener onNoteListener) {
            super(itemView);
            promo = (TextView)itemView.findViewById(R.id.NombrePromo);
            rest = (TextView) itemView.findViewById(R.id.Restaurante);
            dia = (TextView) itemView.findViewById(R.id.dia);
            horario = (TextView) itemView.findViewById(R.id.horario);
            imagen = (ImageView) itemView.findViewById(R.id.imagenCommida);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface onNoteListener{
        void onNoteClick(int position);
    }
}
