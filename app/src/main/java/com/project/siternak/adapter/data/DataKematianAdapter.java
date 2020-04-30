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
import com.project.siternak.activities.data.DataKematianActivity;
import com.project.siternak.models.KematianModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DataKematianAdapter extends RecyclerView.Adapter<DataKematianAdapter.DataKematianHolder> {
    private List<KematianModel> kematianArrayList;
    private Context mContext;

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
        holder.tv_id.setText("1");
        holder.tv_tgl.setText("2020-12-12");
        holder.tv_waktu.setText("12:00:00");
        holder.tv_penyebab.setText("halo");
        holder.tv_kondisi.setText("haloha");

//        final KematianModel data = kematianArrayList.get(position);
//        holder.tv_id.setText(data.getEventType());
//        holder.tv_tgl.setText(data.getEventName());
//        holder.tv_waktu.setText(data.getEventDate());
//        holder.tv_penyebab.setText(data.getEventDate());
//        holder.tv_kondisi.setText(data.getEventDate());
//        holder.ll_data_kematian.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), EventHistoryDetailActivity.class);
//                intent.putExtra("event", data);
//                mContext.startActivity(intent);
//            }
//        });
//        Glide.with(holder.itemView.getContext())
//                .load(data.getPictureCompleteUrl())
//                .into(holder.ivEventPhoto);
    }

    @Override
    public int getItemCount() {
        return kematianArrayList== null? 0: kematianArrayList.size();
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
