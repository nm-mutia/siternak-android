package com.project.siternak.adapter.data;

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
import com.project.siternak.activities.data.PemilikDetailActivity;
import com.project.siternak.models.data.PemilikModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PemilikAdapter extends RecyclerView.Adapter<PemilikAdapter.DataPemilikHolder> implements Filterable {
    private ArrayList<PemilikModel> pemilikArrayList;
    private ArrayList<PemilikModel> pemilikArrayListFull;
    private Context mContext;

    public PemilikAdapter(Context mContext, ArrayList<PemilikModel> pemilikArrayList){
        this.pemilikArrayList = pemilikArrayList;
        this.mContext = mContext;
        pemilikArrayListFull = new ArrayList<>(pemilikArrayList);
    }

    @NonNull
    @Override
    public DataPemilikHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_data, parent, false);
        return new DataPemilikHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataPemilikHolder holder, int position) {
        final PemilikModel data = pemilikArrayList.get(position);
        holder.tvNumbering.setText(String.valueOf(position + 1));
        holder.tvId.setText(String.valueOf(data.getId()));
        holder.tvKtp.setText(data.getKtp());
        holder.tvNama.setText(data.getNama_pemilik());
        holder.llDataPemilik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PemilikDetailActivity.class);
                intent.putExtra("pemilik", (Serializable) data);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pemilikArrayList == null ? 0 : pemilikArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return pemilikFilter;
    }

    private Filter pemilikFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<PemilikModel> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(pemilikArrayListFull);
            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(PemilikModel data : pemilikArrayListFull){
                    if(String.valueOf(data.getId()).toLowerCase().contains(filterPattern) ||
                            data.getKtp().toLowerCase().contains(filterPattern) ||
                            data.getNama_pemilik().toLowerCase().contains(filterPattern)){
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
            pemilikArrayList.clear();
            pemilikArrayList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };


    public class DataPemilikHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_numbering) TextView tvNumbering;
        @BindView(R.id.tv_id) TextView tvId;
        @BindView(R.id.tv_text1) TextView tvKtp;
        @BindView(R.id.tv_text2) TextView tvNama;
        @BindView(R.id.tv_tgl) TextView tvText1;
        @BindView(R.id.tv_text3) TextView tvText2;
        @BindView(R.id.ll_data) LinearLayout llDataPemilik;

        public DataPemilikHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            tvText1.setVisibility(View.GONE);
            tvText2.setVisibility(View.GONE);
        }
    }
}
