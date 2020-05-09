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
import com.project.siternak.activities.data.RiwayatPenyakitDetailActivity;
import com.project.siternak.models.data.RiwayatPenyakitModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RiwayatPenyakitAdapter extends RecyclerView.Adapter<RiwayatPenyakitAdapter.DataRiwayatHolder> implements Filterable {
    private ArrayList<RiwayatPenyakitModel> riwayatArrayList;
    private ArrayList<RiwayatPenyakitModel> riwayatArrayListFull;
    private Context mContext;

    public RiwayatPenyakitAdapter(Context mContext, ArrayList<RiwayatPenyakitModel> riwayatArrayList) {
        this.riwayatArrayList = riwayatArrayList;
        this.mContext = mContext;
        riwayatArrayListFull = new ArrayList<>(riwayatArrayList);
    }

    @NonNull
    @Override
    public DataRiwayatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_data, parent, false);
        return new DataRiwayatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataRiwayatHolder holder, int position) {
        final RiwayatPenyakitModel data = riwayatArrayList.get(position);
        holder.tvNumbering.setText(String.valueOf(position + 1));
        holder.tvId.setText(String.valueOf(data.getId()));
        holder.tvPenyakit.setText(String.valueOf(data.getPenyakitId()));
        holder.tvNecktag.setText(data.getNecktag());
        holder.tvTgl.setText(data.getTglSakit());
        holder.llDataRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RiwayatPenyakitDetailActivity.class);
                intent.putExtra("riwayat", (Serializable) data);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return riwayatArrayList == null ? 0 : riwayatArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return riwayatFilter;
    }

    private Filter riwayatFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<RiwayatPenyakitModel> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(riwayatArrayListFull);
            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(RiwayatPenyakitModel data : riwayatArrayListFull){
                    if(String.valueOf(data.getId()).toLowerCase().contains(filterPattern) ||
                            data.getPenyakitId().toString().toLowerCase().contains(filterPattern) ||
                            data.getNecktag().toLowerCase().contains(filterPattern) ||
                            data.getTglSakit().toLowerCase().contains(filterPattern)){
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
            riwayatArrayList.clear();
            riwayatArrayList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class DataRiwayatHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_numbering) TextView tvNumbering;
        @BindView(R.id.tv_id) TextView tvId;
        @BindView(R.id.tv_tgl) TextView tvPenyakit;
        @BindView(R.id.tv_text1) TextView tvNecktag;
        @BindView(R.id.tv_text2) TextView tvTgl;
        @BindView(R.id.tv_text3) TextView tvText2;
        @BindView(R.id.ll_data)
        LinearLayout llDataRiwayat;

        public DataRiwayatHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            tvText2.setVisibility(View.GONE);
        }
    }
}
