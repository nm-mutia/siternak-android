package com.project.siternak.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.project.siternak.R;
import com.project.siternak.models.data.TernakModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BarcodeAdapter extends RecyclerView.Adapter<BarcodeAdapter.DataBarcodeHolder> {
    private ArrayList<TernakModel> arrayList;
    private Context mContext;

    public BarcodeAdapter(Context mContext, ArrayList<TernakModel> arrayList){
        this.arrayList = arrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public DataBarcodeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_barcode, parent, false);
        return new DataBarcodeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataBarcodeHolder holder, int position) {
        final TernakModel data = arrayList.get(position);
        holder.tvNumbering.setText(String.valueOf(position + 1));
        holder.tvNecktag.setText(String.valueOf(data.getNecktag()));

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
//            BitMatrix bitMatrix = multiFormatWriter.encode(data.getNecktag(), BarcodeFormat.CODE_128, holder.ivBarcode.getWidth(), holder.ivBarcode.getHeight());
            BitMatrix bitMatrix = multiFormatWriter.encode(data.getNecktag(), BarcodeFormat.CODE_128, 240, 50);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for(int i = 0; i < width; i++){
                for(int j = 0; j < height; j++){
                    bitmap.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }
            holder.ivBarcode.setImageBitmap(bitmap);
        }
        catch (WriterException e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    public class DataBarcodeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_numbering) TextView tvNumbering;
        @BindView(R.id.iv_barcode) ImageView ivBarcode;
        @BindView(R.id.tv_necktag) TextView tvNecktag;

        public DataBarcodeHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
