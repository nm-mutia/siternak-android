package com.project.siternak.adapter.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.siternak.R;
import com.project.siternak.models.data.KematianModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DataKematianAdapter extends RecyclerView.Adapter<DataKematianAdapter.DataKematianHolder> {
    private ArrayList<KematianModel> kematianArrayList;
    private Context mContext;

    public DataKematianAdapter(Context mcontext, ArrayList<KematianModel> kematianArrayList){
        this.kematianArrayList = kematianArrayList;
        this.mContext = mcontext;
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
        holder.tv_id.setText(data.getId());
        holder.tv_tgl.setText(data.getTgl_kematian());
        holder.tv_waktu.setText(data.getWaktu_kematian());
        holder.tv_penyebab.setText(data.getPenyebab());
        holder.tv_kondisi.setText(data.getKondisi());

        holder.ll_data_kematian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), EventHistoryDetailActivity.class);
//                intent.putExtra("event", data);
//                mContext.startActivity(intent);
            }
        });
//        Glide.with(holder.itemView.getContext())
//                .load(data.getPictureCompleteUrl())
//                .into(holder.ivEventPhoto);
    }

    @Override
    public int getItemCount() {
        return kematianArrayList== null ? 0 : kematianArrayList.size();
    }

    public class DataKematianHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_id) TextView tv_id;
        @BindView(R.id.tv_tgl) TextView tv_tgl;
        @BindView(R.id.tv_text1) TextView tv_waktu;
        @BindView(R.id.tv_text2) TextView tv_penyebab;
        @BindView(R.id.tv_text3) TextView tv_kondisi;
        @BindView(R.id.ll_data_kematian) LinearLayout ll_data_kematian;

        public DataKematianHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
