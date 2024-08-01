package com.example.ccunsa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ccunsa.R;
import com.example.ccunsa.model.Pintura;
import java.util.List;

public class PinturaAdapter extends RecyclerView.Adapter<PinturaAdapter.PinturaViewHolder> {

    private List<Pintura> pinturas;
    private Context context;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Pintura pintura);
    }

    public PinturaAdapter(Context context, List<Pintura> pinturas, OnItemClickListener listener) {
        this.context = context;
        this.pinturas = pinturas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PinturaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pintura, parent, false);
        return new PinturaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PinturaViewHolder holder, int position) {
        Pintura pintura = pinturas.get(position);
        holder.pinturaName.setText(pintura.getPaintingName());
        holder.pinturaAuthor.setText(pintura.getAuthorName());
        holder.pinturaDescription.setText(pintura.getDescription());
        holder.pinturaIcon.setImageResource(context.getResources().getIdentifier(pintura.getIconPath(), "drawable", context.getPackageName()));
        holder.itemView.setOnClickListener(v -> listener.onItemClick(pintura));
    }

    @Override
    public int getItemCount() {
        return pinturas.size();
    }

    public static class PinturaViewHolder extends RecyclerView.ViewHolder {
        TextView pinturaName;
        TextView pinturaAuthor;
        TextView pinturaDescription;
        ImageView pinturaIcon;

        public PinturaViewHolder(@NonNull View itemView) {
            super(itemView);
            pinturaName = itemView.findViewById(R.id.pintura_name);
            pinturaAuthor = itemView.findViewById(R.id.pintura_author);
            pinturaDescription = itemView.findViewById(R.id.pintura_description);
            pinturaIcon = itemView.findViewById(R.id.pintura_icon);
        }
    }

    public void updatePinturas(List<Pintura> nuevasPinturas) {
        this.pinturas = nuevasPinturas;
        notifyDataSetChanged();
    }
}
