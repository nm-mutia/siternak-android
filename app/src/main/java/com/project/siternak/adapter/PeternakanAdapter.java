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
import com.project.siternak.activities.data.PeternakanDetailActivity;
import com.project.siternak.models.data.PeternakanModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PeternakanAdapter extends RecyclerView.Adapter<PeternakanAdapter.DataPeternakanHolder> implements Filterable {
    private ArrayList<PeternakanModel> peternakanArrayList;
    private ArrayList<PeternakanModel> peternakanArrayListFull;
    private Context mContext;

    public PeternakanAdapter(Context mContext, ArrayList<PeternakanModel> peternakanArrayList) {
        this.peternakanArrayList = peternakanArrayList;
        this.mContext = mContext;
        peternakanArrayListFull = new ArrayList<>(peternakanArrayList);
    }

    @NonNull
    @Override
    public DataPeternakanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_data, parent, false);
        return new DataPeternakanHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataPeternakanHolder holder, int position) {
        final PeternakanModel data = peternakanArrayList.get(position);
        holder.tvNumbering.setText(String.valueOf(position + 1));
        holder.tvId.setText(String.valueOf(data.getId()));
        holder.tvNama.setText(data.getNamaPeternakan());
        holder.tvKet.setText(data.getKeterangan());
        holder.llDataPeternakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PeternakanDetailActivity.class);
                intent.putExtra("peternakan", (Serializable) data);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return peternakanArrayList == null ? 0 : peternakanArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return peternakanFilter;
    }

    private Filter peternakanFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<PeternakanModel> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(peternakanArrayListFull);
            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(PeternakanModel data : peternakanArrayListFull){
                    if(String.valueOf(data.getId()).toLowerCase().contains(filterPattern) ||
                            data.getNamaPeternakan().toLowerCase().contains(filterPattern) ||
                            data.getKeterangan().toLowerCase().contains(filterPattern)){
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
            peternakanArrayList.clear();
            peternakanArrayList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class DataPeternakanHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_numbering) TextView tvNumbering;
        @BindView(R.id.tv_id) TextView tvId;
        @BindView(R.id.tv_text1) TextView tvNama;
        @BindView(R.id.tv_text2) TextView tvKet;
        @BindView(R.id.tv_tgl) TextView tvText1;
        @BindView(R.id.tv_text3) TextView tvText2;
        @BindView(R.id.ll_data) LinearLayout llDataPeternakan;

        public DataPeternakanHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            tvText1.setVisibility(View.GONE);
            tvText2.setVisibility(View.GONE);
        }
    }
}
