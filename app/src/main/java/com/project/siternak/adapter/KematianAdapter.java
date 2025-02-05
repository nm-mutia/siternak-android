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
import com.project.siternak.activities.data.KematianDetailActivity;
import com.project.siternak.models.data.KematianModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KematianAdapter extends RecyclerView.Adapter<KematianAdapter.DataKematianHolder> implements Filterable {
    private ArrayList<KematianModel> kematianArrayList;
    private ArrayList<KematianModel> kematianArrayListFull;
    private Context mContext;

    public KematianAdapter(Context mContext, ArrayList<KematianModel> kematianArrayList){
        this.kematianArrayList = kematianArrayList;
        this.mContext = mContext;
        kematianArrayListFull = new ArrayList<>(kematianArrayList);
    }

    @NonNull
    @Override
    public DataKematianHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_data, parent, false);
        return new DataKematianHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataKematianHolder holder, int position) {
        final KematianModel data = kematianArrayList.get(position);
        holder.tvNumbering.setText(String.valueOf(position + 1));
        holder.tvId.setText(String.valueOf(data.getId()));
        holder.tvTgl.setText(data.getTgl_kematian());
        holder.tvWaktu.setText(data.getWaktu_kematian());
        holder.tvPenyebab.setText(data.getPenyebab());
        holder.tvKondisi.setText(data.getKondisi());
        holder.llDataKematian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), KematianDetailActivity.class);
                intent.putExtra("kematian", (Serializable) data);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return kematianArrayList == null ? 0 : kematianArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return kematianFilter;
    }

    private Filter kematianFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<KematianModel> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(kematianArrayListFull);
            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(KematianModel data : kematianArrayListFull){
                    if(String.valueOf(data.getId()).toLowerCase().contains(filterPattern) ||
                            data.getTgl_kematian().toLowerCase().contains(filterPattern) ||
                            data.getWaktu_kematian().toLowerCase().contains(filterPattern) ||
                            data.getPenyebab().toLowerCase().contains(filterPattern) ||
                            data.getKondisi().toLowerCase().contains(filterPattern)){
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
            kematianArrayList.clear();
            kematianArrayList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class DataKematianHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_numbering) TextView tvNumbering;
        @BindView(R.id.tv_id) TextView tvId;
        @BindView(R.id.tv_tgl) TextView tvTgl;
        @BindView(R.id.tv_text1) TextView tvWaktu;
        @BindView(R.id.tv_text2) TextView tvPenyebab;
        @BindView(R.id.tv_text3) TextView tvKondisi;
        @BindView(R.id.ll_data) LinearLayout llDataKematian;

        public DataKematianHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
