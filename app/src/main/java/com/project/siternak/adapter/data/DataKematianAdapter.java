package com.project.siternak.adapter.data;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.siternak.R;
import com.project.siternak.activities.data.DataKematianDetailActivity;
import com.project.siternak.models.data.KematianModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DataKematianAdapter extends RecyclerView.Adapter<DataKematianAdapter.DataKematianHolder> {
    private ArrayList<KematianModel> kematianArrayList;
    private Context mContext;

    public DataKematianAdapter(Context mContext, ArrayList<KematianModel> kematianArrayList){
        this.kematianArrayList = kematianArrayList;
        this.mContext = mContext;
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
        holder.tvNumbering.setText(String.valueOf(position + 1));
        holder.tvId.setText(String.valueOf(kematianArrayList.get(position).getId()));
        holder.tvTgl.setText(kematianArrayList.get(position).getTgl_kematian());
        holder.tvWaktu.setText(kematianArrayList.get(position).getWaktu_kematian());
        holder.tvPenyebab.setText(kematianArrayList.get(position).getPenyebab());
        holder.tvKondisi.setText(kematianArrayList.get(position).getKondisi());
        holder.llDataKematian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DataKematianDetailActivity.class);
//                intent.putExtra("event", data);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return kematianArrayList == null ? 0 : kematianArrayList.size();
    }

    public class DataKematianHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_numbering) TextView tvNumbering;
        @BindView(R.id.tv_id) TextView tvId;
        @BindView(R.id.tv_tgl) TextView tvTgl;
        @BindView(R.id.tv_text1) TextView tvWaktu;
        @BindView(R.id.tv_text2) TextView tvPenyebab;
        @BindView(R.id.tv_text3) TextView tvKondisi;
        @BindView(R.id.ll_data_kematian) LinearLayout llDataKematian;

        public DataKematianHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
