package com.project.siternak.adapter.laporan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.siternak.R;
import com.project.siternak.models.data.PerkawinanModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DataKawinAdapter extends RecyclerView.Adapter<DataKawinAdapter.DataKawinHolder> {
    private ArrayList<PerkawinanModel> arrayList;

    public DataKawinAdapter(ArrayList<PerkawinanModel> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public DataKawinHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_table_ternak_kawin, parent, false);
        return new DataKawinHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataKawinHolder holder, int position) {
        int rowPos = holder.getAdapterPosition();

        if(rowPos == 0){
            holder.tvT1Id.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1Necktag.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1NecktagPsg.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1Tgl.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1CreatedAt.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1UpdatedAt.setBackgroundResource(R.drawable.bg_table_header_cell);

            holder.tvT1Id.setText("ID");
            holder.tvT1Necktag.setText("Necktag");
            holder.tvT1NecktagPsg.setText("Necktag\nPasangan");
            holder.tvT1Tgl.setText("Tanggal");
            holder.tvT1CreatedAt.setText("Created At");
            holder.tvT1UpdatedAt.setText("Updated At");
        }
        else{
            PerkawinanModel data = arrayList.get(rowPos - 1);

            holder.tvT1Id.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1Necktag.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1NecktagPsg.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1Tgl.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1CreatedAt.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1UpdatedAt.setBackgroundResource(R.drawable.bg_table_content_cell);

            holder.tvT1Id.setText(String.valueOf(position));
            holder.tvT1Necktag.setText(data.getNecktag());
            holder.tvT1NecktagPsg.setText(String.valueOf(data.getNecktag_psg()));
            holder.tvT1Tgl.setText(String.valueOf(data.getTgl()));
            holder.tvT1CreatedAt.setText(data.getCreated_at());
            holder.tvT1UpdatedAt.setText(data.getUpdated_at());
        }
    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size() + 1;
    }

    public class DataKawinHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_t1_id) TextView tvT1Id;
        @BindView(R.id.tv_t1_necktag) TextView tvT1Necktag;
        @BindView(R.id.tv_t1_necktag_psg) TextView tvT1NecktagPsg;
        @BindView(R.id.tv_t1_tgl) TextView tvT1Tgl;
        @BindView(R.id.tv_t1_created_at) TextView tvT1CreatedAt;
        @BindView(R.id.tv_t1_updated_at) TextView tvT1UpdatedAt;

        public DataKawinHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
