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
import com.project.siternak.activities.data.TernakTrashActivity;
import com.project.siternak.activities.data.TernakTrashDetailActivity;
import com.project.siternak.models.data.TernakModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TernakTrashAdapter extends RecyclerView.Adapter<TernakTrashAdapter.DataTernakHolder> implements Filterable {
    private ArrayList<TernakModel> ternakArrayList;
    private ArrayList<TernakModel> ternakArrayListFull;
    private Context mContext;

    public TernakTrashAdapter(Context mContext, ArrayList<TernakModel> ternakArrayList) {
        this.ternakArrayList = ternakArrayList;
        this.mContext = mContext;
        ternakArrayListFull = new ArrayList<>(ternakArrayList);
    }

    @NonNull
    @Override
    public DataTernakHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_data, parent, false);
        return new DataTernakHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataTernakHolder holder, int position) {
        final TernakModel data = ternakArrayList.get(position);
        holder.tvNumbering.setText(String.valueOf(position + 1));
        holder.tvNecktag.setText(String.valueOf(data.getNecktag()));
        holder.tvJenisKelamin.setText(data.getJenisKelamin());
        holder.tvTglLahir.setText(data.getTglLahir());
        holder.tvBlood.setText(data.getBlood());
        if(data.getStatusAda()) {
            holder.tvStatus.setText("Ada");
        }else{
            holder.tvStatus.setText("Tidak Ada");
        }
        holder.llDataTernak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TernakTrashDetailActivity.class);
                intent.putExtra("ternak", (Serializable) data);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ternakArrayList == null ? 0 : ternakArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return ternakFilter;
    }

    private Filter ternakFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<TernakModel> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(ternakArrayListFull);
            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(TernakModel data : ternakArrayListFull){
                    if(String.valueOf(data.getNecktag()).toLowerCase().contains(filterPattern) ||
                            data.getJenisKelamin().toLowerCase().contains(filterPattern) ||
                            data.getTglLahir().toLowerCase().contains(filterPattern) ||
                            data.getBlood().toLowerCase().contains(filterPattern) ||
                            data.getStatusAda().toString().toLowerCase().contains(filterPattern)){
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
            ternakArrayList.clear();
            ternakArrayList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class DataTernakHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_numbering) TextView tvNumbering;
        @BindView(R.id.tv_id) TextView tvNecktag;
        @BindView(R.id.tv_tgl) TextView tvJenisKelamin;
        @BindView(R.id.tv_text1) TextView tvTglLahir;
        @BindView(R.id.tv_text2) TextView tvBlood;
        @BindView(R.id.tv_text3) TextView tvStatus;
        @BindView(R.id.ll_data) LinearLayout llDataTernak;

        public DataTernakHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
