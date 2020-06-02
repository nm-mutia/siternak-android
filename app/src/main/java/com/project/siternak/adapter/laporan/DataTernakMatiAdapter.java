package com.project.siternak.adapter.laporan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.siternak.R;
import com.project.siternak.models.data.TernakMatiModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DataTernakMatiAdapter extends RecyclerView.Adapter<DataTernakMatiAdapter.DataTernakMatiHolder> {
    private ArrayList<TernakMatiModel> ternakArrayList;

    public DataTernakMatiAdapter(ArrayList<TernakMatiModel> ternakArrayList) {
        this.ternakArrayList = ternakArrayList;
    }

    @NonNull
    @Override
    public DataTernakMatiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_table_ternak_mati, parent, false);
        return new DataTernakMatiHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataTernakMatiHolder holder, int position) {
        int rowPos = holder.getAdapterPosition();

        if(rowPos == 0){
            holder.tvT1No.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1Necktag.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1Kematian.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1KematianTgl.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1KematianWaktu.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1KematianPenyebab.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1KematianKondisi.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1Pemilik.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1Peternakan.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1Ras.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1Jk.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1TglLahir.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1BobotLahir.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1PukulLahir.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1LamaDikandungan.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1LamaLaktasi.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1TglLepasSapih.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1Blood.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1Ayah.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1Ibu.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1BobotTubuh.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1PanjangTubuh.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1TinggiTubuh.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1CacatFisik.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1CiriLain.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1StatusAda.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1CreatedAt.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1UpdatedAt.setBackgroundResource(R.drawable.bg_table_header_cell);
            holder.tvT1DeletedAt.setBackgroundResource(R.drawable.bg_table_header_cell);

            holder.tvT1No.setText("No.");
            holder.tvT1Necktag.setText("Necktag");
            holder.tvT1Kematian.setText("ID\nKematian");
            holder.tvT1KematianTgl.setText("Tanggal\nKematian");
            holder.tvT1KematianWaktu.setText("Waktu\nKematian");
            holder.tvT1KematianPenyebab.setText("Penyebab\nKematian");
            holder.tvT1KematianKondisi.setText("Kondisi\nKematian");
            holder.tvT1Pemilik.setText("ID\nPemilik");
            holder.tvT1Peternakan.setText("ID\nPeternakan");
            holder.tvT1Ras.setText("ID\nRas");
            holder.tvT1Jk.setText("Jenis\nKelamin");
            holder.tvT1TglLahir.setText("Tanggal\nLahir");
            holder.tvT1BobotLahir.setText("Bobot\nLahir");
            holder.tvT1PukulLahir.setText("Pukul\nLahir");
            holder.tvT1LamaDikandungan.setText("Lama Di\nKandungan");
            holder.tvT1LamaLaktasi.setText("Lama\nLaktasi");
            holder.tvT1TglLepasSapih.setText("Tanggal\nLepas Sapih");
            holder.tvT1Blood.setText("Blood");
            holder.tvT1Ayah.setText("Ayah");
            holder.tvT1Ibu.setText("Ibu");
            holder.tvT1BobotTubuh.setText("Bobot\nTubuh");
            holder.tvT1PanjangTubuh.setText("Panjang\nTubuh");
            holder.tvT1TinggiTubuh.setText("Tinggi\nTubuh");
            holder.tvT1CacatFisik.setText("Cacat Fisik");
            holder.tvT1CiriLain.setText("Ciri Lain");
            holder.tvT1StatusAda.setText("Status\nAda");
            holder.tvT1CreatedAt.setText("Created At");
            holder.tvT1UpdatedAt.setText("Updated At");
            holder.tvT1DeletedAt.setText("Deleted At");
        }
        else{
            TernakMatiModel data = ternakArrayList.get(rowPos - 1);

            holder.tvT1No.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1Necktag.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1Kematian.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1KematianTgl.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1KematianWaktu.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1KematianPenyebab.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1KematianKondisi.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1Pemilik.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1Peternakan.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1Ras.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1Jk.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1TglLahir.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1BobotLahir.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1PukulLahir.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1LamaDikandungan.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1LamaLaktasi.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1TglLepasSapih.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1Blood.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1Ayah.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1Ibu.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1BobotTubuh.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1PanjangTubuh.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1TinggiTubuh.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1CacatFisik.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1CiriLain.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1StatusAda.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1CreatedAt.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1UpdatedAt.setBackgroundResource(R.drawable.bg_table_content_cell);
            holder.tvT1DeletedAt.setBackgroundResource(R.drawable.bg_table_content_cell);

            holder.tvT1No.setText(String.valueOf(position));
            holder.tvT1Necktag.setText(data.getNecktag());
            holder.tvT1Kematian.setText(String.valueOf(data.getKematianId()));
            holder.tvT1KematianTgl.setText(data.getTglKematian());
            holder.tvT1KematianWaktu.setText(data.getWaktuKematian());
            holder.tvT1KematianPenyebab.setText(data.getPenyebab());
            holder.tvT1KematianKondisi.setText(data.getKondisi());
            holder.tvT1Pemilik.setText(String.valueOf(data.getPemilikId()));
            holder.tvT1Peternakan.setText(String.valueOf(data.getPeternakanId()));
            holder.tvT1Ras.setText(String.valueOf(data.getRasId()));
            holder.tvT1Jk.setText(data.getJenisKelamin());
            holder.tvT1TglLahir.setText(data.getTglLahir());
            holder.tvT1BobotLahir.setText(data.getBobotLahir());
            holder.tvT1PukulLahir.setText(data.getPukulLahir());
            holder.tvT1LamaDikandungan.setText(data.getLamaDiKandungan());
            holder.tvT1LamaLaktasi.setText(data.getLamaLaktasi());
            holder.tvT1TglLepasSapih.setText(data.getTglLepasSapih());
            holder.tvT1Blood.setText(data.getBlood());
            holder.tvT1Ayah.setText(data.getNecktag_ayah());
            holder.tvT1Ibu.setText(data.getNecktag_ibu());
            holder.tvT1BobotTubuh.setText(data.getBobotTubuh());
            holder.tvT1PanjangTubuh.setText(data.getPanjangTubuh());
            holder.tvT1TinggiTubuh.setText(data.getTinggiTubuh());
            holder.tvT1CacatFisik.setText(data.getCacatFisik());
            holder.tvT1CiriLain.setText(data.getCiriLain());
            if(data.getStatusAda()){
                holder.tvT1StatusAda.setText("Ada");
            }
            else{
                holder.tvT1StatusAda.setText("Tidak Ada");
            }
            holder.tvT1CreatedAt.setText(data.getCreated_at());
            holder.tvT1UpdatedAt.setText(data.getUpdated_at());
            holder.tvT1DeletedAt.setText(data.getDeleted_at());
        }
    }

    @Override
    public int getItemCount() {
        return ternakArrayList == null ? 0 : ternakArrayList.size() + 1;
    }

    public class DataTernakMatiHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_t1_no) TextView tvT1No;
        @BindView(R.id.tv_t1_necktag) TextView tvT1Necktag;
        @BindView(R.id.tv_t1_kematian) TextView tvT1Kematian;
        @BindView(R.id.tv_t1_kematian_tgl) TextView tvT1KematianTgl;
        @BindView(R.id.tv_t1_kematian_waktu) TextView tvT1KematianWaktu;
        @BindView(R.id.tv_t1_kematian_penyebab) TextView tvT1KematianPenyebab;
        @BindView(R.id.tv_t1_kematian_kondisi) TextView tvT1KematianKondisi;
        @BindView(R.id.tv_t1_pemilik) TextView tvT1Pemilik;
        @BindView(R.id.tv_t1_peternakan) TextView tvT1Peternakan;
        @BindView(R.id.tv_t1_ras) TextView tvT1Ras;
        @BindView(R.id.tv_t1_jk) TextView tvT1Jk;
        @BindView(R.id.tv_t1_tgl_lahir) TextView tvT1TglLahir;
        @BindView(R.id.tv_t1_bobot_lahir) TextView tvT1BobotLahir;
        @BindView(R.id.tv_t1_pukul_lahir) TextView tvT1PukulLahir;
        @BindView(R.id.tv_t1_lama_dikandungan) TextView tvT1LamaDikandungan;
        @BindView(R.id.tv_t1_lama_laktasi) TextView tvT1LamaLaktasi;
        @BindView(R.id.tv_t1_tgl_lepas_sapih) TextView tvT1TglLepasSapih;
        @BindView(R.id.tv_t1_blood) TextView tvT1Blood;
        @BindView(R.id.tv_t1_ayah) TextView tvT1Ayah;
        @BindView(R.id.tv_t1_ibu) TextView tvT1Ibu;
        @BindView(R.id.tv_t1_bobot_tubuh) TextView tvT1BobotTubuh;
        @BindView(R.id.tv_t1_panjang_tubuh) TextView tvT1PanjangTubuh;
        @BindView(R.id.tv_t1_tinggi_tubuh) TextView tvT1TinggiTubuh;
        @BindView(R.id.tv_t1_cacat_fisik) TextView tvT1CacatFisik;
        @BindView(R.id.tv_t1_ciri_lain) TextView tvT1CiriLain;
        @BindView(R.id.tv_t1_status_ada) TextView tvT1StatusAda;
        @BindView(R.id.tv_t1_created_at) TextView tvT1CreatedAt;
        @BindView(R.id.tv_t1_updated_at) TextView tvT1UpdatedAt;
        @BindView(R.id.tv_t1_deleted_at) TextView tvT1DeletedAt;

        public DataTernakMatiHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
