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
import com.project.siternak.models.data.PenyakitModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PenyakitOptionAdapter extends RecyclerView.Adapter<PenyakitOptionAdapter.OptionHolder> implements Filterable {
    private List<PenyakitModel> arrayList;
    private List<PenyakitModel> arrayListFull;
    private Context mContext;

    public PenyakitOptionAdapter(Context mContext, List<PenyakitModel> arrayList) {
        this.arrayList = arrayList;
        this.mContext = mContext;
        arrayListFull = new ArrayList<>(arrayList);
    }

    @NonNull
    @Override
    public OptionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_option, parent, false);
        return new OptionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionHolder holder, int position) {
        PenyakitModel option = arrayList.get(position);
        holder.tv_option.setText(option.getId()+". "+option.getNamaPenyakit());
        holder.ll_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent();
                intent.putExtra("penyakit", option);
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
            List<PenyakitModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(arrayListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (PenyakitModel item : arrayListFull) {
                    if (item.getNamaPenyakit().toLowerCase().contains(filterPattern)) {
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
