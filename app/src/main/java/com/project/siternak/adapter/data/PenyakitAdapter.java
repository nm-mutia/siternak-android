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
import com.project.siternak.activities.data.PenyakitDetailActivity;
import com.project.siternak.models.data.PenyakitModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PenyakitAdapter extends RecyclerView.Adapter<PenyakitAdapter.DataPenyakitHolder> implements Filterable {
    private ArrayList<PenyakitModel> penyakitArrayList;
    private ArrayList<PenyakitModel> penyakitArrayListFull;
    private Context mContext;

    public PenyakitAdapter(Context mContext, ArrayList<PenyakitModel> penyakitArrayList){
        this.penyakitArrayList = penyakitArrayList;
        this.mContext = mContext;
        penyakitArrayListFull = new ArrayList<>(penyakitArrayList);
    }

    @NonNull
    @Override
    public DataPenyakitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_data, parent, false);
        return new DataPenyakitHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataPenyakitHolder holder, int position) {
        final PenyakitModel data = penyakitArrayList.get(position);
        holder.tvNumbering.setText(String.valueOf(position + 1));
        holder.tvId.setText(String.valueOf(data.getId()));
        holder.tvNama.setText(data.getNamaPenyakit());
        holder.tvKet.setText(data.getKetPenyakit());
        holder.llDataPenyakit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PenyakitDetailActivity.class);
                intent.putExtra("penyakit", (Serializable) data);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return penyakitArrayList == null ? 0 : penyakitArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return penyakitFilter;
    }

    private Filter penyakitFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<PenyakitModel> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(penyakitArrayListFull);
            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(PenyakitModel data : penyakitArrayListFull){
                    if(String.valueOf(data.getId()).toLowerCase().contains(filterPattern) ||
                            data.getNamaPenyakit().toLowerCase().contains(filterPattern) ||
                            data.getKetPenyakit().toLowerCase().contains(filterPattern)){
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
            penyakitArrayList.clear();
            penyakitArrayList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class DataPenyakitHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_numbering) TextView tvNumbering;
        @BindView(R.id.tv_id) TextView tvId;
        @BindView(R.id.tv_text1) TextView tvNama;
        @BindView(R.id.tv_text2) TextView tvKet;
        @BindView(R.id.tv_tgl) TextView tvText1;
        @BindView(R.id.tv_text3) TextView tvText2;
        @BindView(R.id.ll_data) LinearLayout llDataPenyakit;

        public DataPenyakitHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            tvText1.setVisibility(View.GONE);
            tvText2.setVisibility(View.GONE);
        }
    }
}
