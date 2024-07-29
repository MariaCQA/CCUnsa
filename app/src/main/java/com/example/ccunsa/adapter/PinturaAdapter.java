package com.example.ccunsa.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ccunsa.R;
import com.example.ccunsa.model.Pintura;

import java.util.ArrayList;
import java.util.List;

public class PinturaAdapter extends RecyclerView.Adapter<PinturaAdapter.PinturaViewHolder> {
    private List<Pintura> pinturas = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public PinturaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pintura_item, parent, false);
        return new PinturaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PinturaViewHolder holder, int position) {
        Pintura currentPintura = pinturas.get(position);
        holder.textViewTitle.setText(currentPintura.getTitulo());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onItemClick(pinturas.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return pinturas.size();
    }

    public void setPinturas(List<Pintura> pinturas) {
        this.pinturas = pinturas;
        notifyDataSetChanged();
    }

    class PinturaViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;

        public PinturaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Pintura pintura);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
