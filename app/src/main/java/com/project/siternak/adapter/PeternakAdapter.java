package com.project.siternak.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.siternak.R;
import com.project.siternak.activities.peternak.PeternakDetailActivity;
import com.project.siternak.models.peternak.PeternakModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PeternakAdapter extends RecyclerView.Adapter<PeternakAdapter.PeternakHolder> implements Filterable {
    private ArrayList<PeternakModel> peternakArrayList;
    private ArrayList<PeternakModel> peternakArrayListFull;
    private Context mContext;

    public PeternakAdapter(Context mContext, ArrayList<PeternakModel> peternakArrayList) {
        this.peternakArrayList = peternakArrayList;
        this.mContext = mContext;
        peternakArrayListFull = new ArrayList<>(peternakArrayList);
    }

    @NonNull
    @Override
    public PeternakHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_data, parent, false);
        return new PeternakHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeternakAdapter.PeternakHolder holder, int position) {
        final PeternakModel data = peternakArrayList.get(position);
        holder.tvNumbering.setText(String.valueOf(position + 1));
        holder.tvId.setText(String.valueOf(data.getId()));
        holder.tvNama.setText(data.getNamaPeternak());
        holder.tvPeternakan.setText(String.valueOf(data.getPeternakanId()));
        holder.tvUsername.setText(data.getUsername());
        holder.tvPass.setText(data.getPassword());
        holder.llPeternak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PeternakDetailActivity.class);
                intent.putExtra("peternak", (Serializable) data);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return peternakArrayList == null ? 0 : peternakArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return peternakFilter;
    }

    private Filter peternakFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<PeternakModel> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(peternakArrayListFull);
            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(PeternakModel data : peternakArrayListFull){
                    if(String.valueOf(data.getId()).toLowerCase().contains(filterPattern) ||
                            data.getNamaPeternak().toLowerCase().contains(filterPattern) ||
                            data.getUsername().toLowerCase().contains(filterPattern) ||
                            String.valueOf(data.getPeternakanId()).toLowerCase().contains(filterPattern)){
                        filteredList.add(data);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            peternakArrayList.clear();
            peternakArrayList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class PeternakHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_numbering) TextView tvNumbering;
        @BindView(R.id.tv_id) TextView tvId;
        @BindView(R.id.tv_tgl) TextView tvNama;
        @BindView(R.id.tv_text1) TextView tvPeternakan;
        @BindView(R.id.tv_text2) TextView tvUsername;
        @BindView(R.id.tv_text3) TextView tvPass;
        @BindView(R.id.ll_data) LinearLayout llPeternak;

        public PeternakHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
