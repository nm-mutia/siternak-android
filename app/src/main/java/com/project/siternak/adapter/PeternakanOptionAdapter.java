package com.project.siternak.adapter;

import android.app.Activity;
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
import com.project.siternak.models.data.PeternakanModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PeternakanOptionAdapter extends RecyclerView.Adapter<PeternakanOptionAdapter.OptionHolder> implements Filterable {
    private List<PeternakanModel> arrayList;
    private List<PeternakanModel> arrayListFull;
    private Context mContext;

    public PeternakanOptionAdapter(Context mContext, List<PeternakanModel> arrayList) {
        this.arrayList = arrayList;
        this.mContext = mContext;
        arrayListFull = new ArrayList<>(arrayList);
    }

    @NonNull
    @Override
    public PeternakanOptionAdapter.OptionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_option, parent, false);
        return new OptionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeternakanOptionAdapter.OptionHolder holder, int position) {
        PeternakanModel option = arrayList.get(position);
        holder.tv_option.setText(option.getId()+"-"+option.getNamaPeternakan());
        holder.ll_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent();
                intent.putExtra("peternakan", option);
                ((Activity)mContext).setResult(Activity.RESULT_OK,intent);
                ((Activity)mContext).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<PeternakanModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(arrayListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (PeternakanModel item : arrayListFull) {
                    if (item.getNamaPeternakan().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            arrayList.clear();
            arrayList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    public class OptionHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_option)
        TextView tv_option ;
        @BindView(R.id.ll_option)
        LinearLayout ll_option;

        public OptionHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
