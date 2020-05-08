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
import com.project.siternak.activities.data.PerkawinanDetailActivity;
import com.project.siternak.models.data.PerkawinanModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PerkawinanAdapter extends RecyclerView.Adapter<PerkawinanAdapter.DataPerkawinanHolder> implements Filterable {
    private ArrayList<PerkawinanModel> perkawinanArrayList;
    private ArrayList<PerkawinanModel> perkawinanArrayListFull;
    private Context mContext;

    public PerkawinanAdapter(Context mContext, ArrayList<PerkawinanModel> perkawinanArrayList) {
        this.perkawinanArrayList = perkawinanArrayList;
        this.mContext = mContext;
        perkawinanArrayListFull = new ArrayList<>(perkawinanArrayList);
    }

    @NonNull
    @Override
    public DataPerkawinanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_data, parent, false);
        return new DataPerkawinanHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataPerkawinanHolder holder, int position) {
        final PerkawinanModel data = perkawinanArrayList.get(position);
        holder.tvNumbering.setText(String.valueOf(position + 1));
        holder.tvId.setText(String.valueOf(data.getId()));
        holder.tvTgl.setText(data.getTgl());
        holder.tvNecktag.setText(data.getNecktag());
        holder.tvNecktagPsg.setText(data.getNecktag_psg());
        holder.llDataPerkawinan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PerkawinanDetailActivity.class);
                intent.putExtra("perkawinan", (Serializable) data);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return perkawinanArrayList == null ? 0 : perkawinanArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return perkawinanFilter;
    }

    private Filter perkawinanFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<PerkawinanModel> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(perkawinanArrayListFull);
            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(PerkawinanModel data : perkawinanArrayListFull){
                    if(String.valueOf(data.getId()).toLowerCase().contains(filterPattern) ||
                            data.getTgl().toLowerCase().contains(filterPattern) ||
                            data.getNecktag().toLowerCase().contains(filterPattern) ||
                            data.getNecktag_psg().toLowerCase().contains(filterPattern)){
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
            perkawinanArrayList.clear();
            perkawinanArrayList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };


    public class DataPerkawinanHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_numbering) TextView tvNumbering;
        @BindView(R.id.tv_id) TextView tvId;
        @BindView(R.id.tv_tgl) TextView tvTgl;
        @BindView(R.id.tv_text1) TextView tvNecktag;
        @BindView(R.id.tv_text2) TextView tvNecktagPsg;
        @BindView(R.id.tv_text3) TextView tvText2;
        @BindView(R.id.ll_data) LinearLayout llDataPerkawinan;

        public DataPerkawinanHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            tvText2.setVisibility(View.GONE);
        }
    }
}
