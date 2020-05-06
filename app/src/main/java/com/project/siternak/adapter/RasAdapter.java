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
import com.project.siternak.activities.data.RasDetailActivity;
import com.project.siternak.models.data.RasModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RasAdapter extends RecyclerView.Adapter<RasAdapter.DataRasHolder> implements Filterable {
    private ArrayList<RasModel> rasArrayList;
    private ArrayList<RasModel> rasArrayListFull;
    private Context mContext;

    public RasAdapter(Context mContext, ArrayList<RasModel> rasArrayList) {
        this.rasArrayList = rasArrayList;
        this.mContext = mContext;
        rasArrayListFull = new ArrayList<>(rasArrayList);
    }

    @NonNull
    @Override
    public DataRasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_data, parent, false);
        return new DataRasHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataRasHolder holder, int position) {
        final RasModel data = rasArrayList.get(position);
        holder.tvNumbering.setText(String.valueOf(position + 1));
        holder.tvId.setText(String.valueOf(data.getId()));
        holder.tvJenisRas.setText(data.getJenisRas());
        holder.tvKetRas.setText(data.getKetRas());
        holder.llDataRas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RasDetailActivity.class);
                intent.putExtra("ras", (Serializable) data);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rasArrayList == null ? 0 : rasArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return rasFilter;
    }

    private Filter rasFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<RasModel> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(rasArrayListFull);
            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(RasModel data : rasArrayListFull){
                    if(String.valueOf(data.getId()).toLowerCase().contains(filterPattern) ||
                            data.getJenisRas().toLowerCase().contains(filterPattern) ||
                            data.getKetRas().toLowerCase().contains(filterPattern)){
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
            rasArrayList.clear();
            rasArrayList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class DataRasHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_numbering) TextView tvNumbering;
        @BindView(R.id.tv_id) TextView tvId;
        @BindView(R.id.tv_text1) TextView tvJenisRas;
        @BindView(R.id.tv_text2) TextView tvKetRas;
        @BindView(R.id.tv_tgl) TextView tvText1;
        @BindView(R.id.tv_text3) TextView tvText2;
        @BindView(R.id.ll_data) LinearLayout llDataRas;

        public DataRasHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            tvText1.setVisibility(View.GONE);
            tvText2.setVisibility(View.GONE);
        }
    }
}
