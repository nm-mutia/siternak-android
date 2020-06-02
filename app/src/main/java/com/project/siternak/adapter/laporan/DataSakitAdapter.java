package com.project.siternak.adapter.laporan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.siternak.R;
import com.project.siternak.models.data.RiwayatPenyakitModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DataSakitAdapter extends RecyclerView.Adapter<DataSakitAdapter.DataSakitHolder> {
    private ArrayList<RiwayatPenyakitModel> arrayList;

    public DataSakitAdapter(ArrayList<RiwayatPenyakitModel> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public DataSakitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_table_ternak_sakit, parent, false);
        return new DataSakitHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataSakitHolder holder, int position) {
        int rowPos = holder.getAdapterPosition();

        if(rowPos == 0){
            holder.tvT1Id.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1Penyakit.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1Necktag.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1TglSakit.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1Obat.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1LamaSakit.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1Ket.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1CreatedAt.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1UpdatedAt.setBackgroundResource(R.drawable.bg_table_header_cell);

            holder.tvT1Id.setText("ID");
            holder.tvT1Penyakit.setText("ID\nPenyakit");
            holder.tvT1Necktag.setText("Necktag");
            holder.tvT1TglSakit.setText("Tanggal\nSakit");
            holder.tvT1Obat.setText("Obat");
            holder.tvT1LamaSakit.setText("Lama Sakit");
            holder.tvT1Ket.setText("Keterangan");
            holder.tvT1CreatedAt.setText("Created At");
            holder.tvT1UpdatedAt.setText("Updated At");
        }
        else{
            RiwayatPenyakitModel data = arrayList.get(rowPos - 1);

            holder.tvT1Id.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1Penyakit.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1Necktag.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1TglSakit.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1Obat.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1LamaSakit.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1Ket.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1CreatedAt.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1UpdatedAt.setBackgroundResource(R.drawable.bg_table_content_cell);

            holder.tvT1Id.setText(String.valueOf(position));
            holder.tvT1Penyakit.setText(String.valueOf(data.getPenyakitId()));
            holder.tvT1Necktag.setText(data.getNecktag());
            holder.tvT1TglSakit.setText(data.getTglSakit());
            holder.tvT1Obat.setText(data.getObat());
            holder.tvT1LamaSakit.setText(String.valueOf(data.getLamaSakit()));
            holder.tvT1Ket.setText(data.getKeterangan());
            holder.tvT1CreatedAt.setText(data.getCreated_at());
            holder.tvT1UpdatedAt.setText(data.getUpdated_at());
        }
    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size() + 1;
    }

    public class DataSakitHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_t1_id) TextView tvT1Id;
        @BindView(R.id.tv_t1_penyakit) TextView tvT1Penyakit;
        @BindView(R.id.tv_t1_necktag) TextView tvT1Necktag;
        @BindView(R.id.tv_t1_tgl_sakit) TextView tvT1TglSakit;
        @BindView(R.id.tv_t1_obat) TextView tvT1Obat;
        @BindView(R.id.tv_t1_lama_sakit) TextView tvT1LamaSakit;
        @BindView(R.id.tv_t1_ket) TextView tvT1Ket;
        @BindView(R.id.tv_t1_created_at) TextView tvT1CreatedAt;
        @BindView(R.id.tv_t1_updated_at) TextView tvT1UpdatedAt;

        public DataSakitHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
