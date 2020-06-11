package com.project.siternak.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.siternak.R;
import com.project.siternak.models.scan.Instance;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScanResultAdapter extends RecyclerView.Adapter<ScanResultAdapter.ScanResultHolder> {
    private ArrayList<Instance> instanceArrayList;
    private Context mContext;
    private String title;

    public ScanResultAdapter(Context mContext, ArrayList<Instance> instanceArrayList, String title) {
        this.mContext = mContext;
        this.instanceArrayList = instanceArrayList;
        this.title = title;
    }

    @NonNull
    @Override
    public ScanResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_instance, parent, false);
        return new ScanResultHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanResultHolder holder, int position) {
        final Instance data = instanceArrayList.get(position);
        holder.tvTitle.setText(title);
        holder.tvNecktag.setText(data.getNecktag());
        holder.tvJk.setText(data.getJenisKelamin());
        holder.tvRas.setText(data.getRas());
        holder.tvTglLahir.setText(data.getTglLahir());
        holder.tvBlood.setText(data.getBlood());
        holder.tvPeternakan.setText(data.getPeternakan());
        holder.tvAyah.setText(data.getAyah());
        holder.tvIbu.setText(data.getIbu());
    }

    @Override
    public int getItemCount() {
        return instanceArrayList == null ? 0 : instanceArrayList.size();
    }

    public class ScanResultHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_inst_title) TextView tvTitle;
        @BindView(R.id.tv_inst_necktag) TextView tvNecktag;
        @BindView(R.id.tv_inst_jk) TextView tvJk;
        @BindView(R.id.tv_inst_ras) TextView tvRas;
        @BindView(R.id.tv_inst_tgl_lahir) TextView tvTglLahir;
        @BindView(R.id.tv_inst_blood) TextView tvBlood;
        @BindView(R.id.tv_inst_peternakan) TextView tvPeternakan;
        @BindView(R.id.tv_inst_ayah) TextView tvAyah;
        @BindView(R.id.tv_inst_ibu) TextView tvIbu;

        public ScanResultHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
