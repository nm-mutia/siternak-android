package com.project.siternak.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.siternak.models.data.KematianModel;
import com.project.siternak.models.data.PemilikModel;
import com.project.siternak.models.data.PenyakitModel;
import com.project.siternak.models.data.PerkawinanModel;
import com.project.siternak.models.data.PeternakanModel;
import com.project.siternak.models.data.RasModel;
import com.project.siternak.models.data.RiwayatPenyakitModel;
import com.project.siternak.models.data.TernakModel;
import com.project.siternak.models.peternak.PeternakModel;
import com.project.siternak.responses.KematianResponse;
import com.project.siternak.responses.OptionsResponse;
import com.project.siternak.responses.PemilikResponse;
import com.project.siternak.responses.PenyakitResponse;
import com.project.siternak.responses.PerkawinanResponse;
import com.project.siternak.responses.PeternakResponse;
import com.project.siternak.responses.PeternakanResponse;
import com.project.siternak.responses.RasResponse;
import com.project.siternak.responses.RiwayatPenyakitResponse;
import com.project.siternak.responses.TernakResponse;
import com.project.siternak.rest.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseHelper {
    private Context mContext ;
    private String userToken;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef, mReference;

    public FirebaseHelper(Context mContext){
        this.mContext = mContext;
        userToken = SharedPrefManager.getInstance(mContext).getAccessToken();
        mDatabase = FirebaseDatabase.getInstance();
    }

    public void syncData(){
        pullFromDb(); //store to firebase
        pushToDb(); // retrieve from firebase
    }

    private void pullFromDb(){
        Call<OptionsResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getOptions("Bearer " + userToken);

        call.enqueue(new Callback<OptionsResponse>() {
            @Override
            public void onResponse(Call<OptionsResponse> call, Response<OptionsResponse> response) {
                OptionsResponse resp = response.body();

                mRef = mDatabase.getReference("options");

                List<KematianModel> kematians = resp.getKematians();
                List<PemilikModel> pemiliks = resp.getPemiliks();
                List<PenyakitModel> penyakits = resp.getPenyakits();
                List<PeternakanModel> peternakans = resp.getPeternakan();
                List<RasModel> ras = resp.getRas();
                List<TernakModel> ternaks = resp.getTernaks();

                for(KematianModel data : kematians){
                    mRef.child("kematian").child(data.getId().toString()).setValue(data);
                }

                for(PemilikModel data : pemiliks){
                    mRef.child("pemilik").child(data.getId().toString()).setValue(data);
                }

                for(PenyakitModel data : penyakits){
                    mRef.child("penyakit").child(data.getId().toString()).setValue(data);
                }

                for(PeternakanModel data : peternakans){
                    mRef.child("peternakan").child(data.getId().toString()).setValue(data);
                }

                for(RasModel data : ras){
                    mRef.child("ras").child(data.getId().toString()).setValue(data);
                }

                for(TernakModel data : ternaks){
                    mRef.child("ternak").child(data.getNecktag()).setValue(data);
                }

                mRef.keepSynced(true);
            }

            @Override
            public void onFailure(Call<OptionsResponse> call, Throwable t) {
                Toast.makeText(mContext, "Gagal sync: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void pushToDb(){
        mReference = mDatabase.getReference("datas").child("addData");

        mReference.child("kematian").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        KematianModel kematian = data.getValue(KematianModel.class);

                        Call<KematianResponse> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .addKematian(kematian.getTgl_kematian(), kematian.getWaktu_kematian(), kematian.getPenyebab(), kematian.getKondisi(), "Bearer " + userToken);

                        call.enqueue(new Callback<KematianResponse>() {
                            @Override
                            public void onResponse(Call<KematianResponse> call, Response<KematianResponse> response) {
                                Toast.makeText(mContext, "Push kematian " + data.getKey(), Toast.LENGTH_SHORT).show();
                                data.getRef().removeValue();
                            }

                            @Override
                            public void onFailure(Call<KematianResponse> call, Throwable t) {
                                Toast.makeText(mContext, "Gagal push kematian: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mReference.child("pemilik").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        PemilikModel pemilik = data.getValue(PemilikModel.class);

                        Call<PemilikResponse> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .addPemilik(pemilik.getKtp(), pemilik.getNama_pemilik(), "Bearer " + userToken);

                        call.enqueue(new Callback<PemilikResponse>() {
                            @Override
                            public void onResponse(Call<PemilikResponse> call, Response<PemilikResponse> response) {
                                Toast.makeText(mContext, "Push pemilik", Toast.LENGTH_SHORT).show();
                                data.getRef().removeValue();
                            }

                            @Override
                            public void onFailure(Call<PemilikResponse> call, Throwable t) {
                                Toast.makeText(mContext, "Gagal push pemilik: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mReference.child("penyakit").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        PenyakitModel penyakit = data.getValue(PenyakitModel.class);

                        Call<PenyakitResponse> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .addPenyakit(penyakit.getNamaPenyakit(), penyakit.getKetPenyakit(), "Bearer " + userToken);

                        call.enqueue(new Callback<PenyakitResponse>() {
                            @Override
                            public void onResponse(Call<PenyakitResponse> call, Response<PenyakitResponse> response) {
                                Toast.makeText(mContext, "Push penyakit", Toast.LENGTH_SHORT).show();
                                data.getRef().removeValue();
                            }

                            @Override
                            public void onFailure(Call<PenyakitResponse> call, Throwable t) {
                                Toast.makeText(mContext, "Gagal push penyakit: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mReference.child("peternakan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        PeternakanModel peternakan = data.getValue(PeternakanModel.class);

                        Call<PeternakanResponse> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .addPeternakan(peternakan.getNamaPeternakan(), peternakan.getKeterangan(), "Bearer " + userToken);

                        call.enqueue(new Callback<PeternakanResponse>() {
                            @Override
                            public void onResponse(Call<PeternakanResponse> call, Response<PeternakanResponse> response) {
                                Toast.makeText(mContext, "Push peternakan", Toast.LENGTH_SHORT).show();
                                data.getRef().removeValue();
                            }

                            @Override
                            public void onFailure(Call<PeternakanResponse> call, Throwable t) {
                                Toast.makeText(mContext, "Gagal push peternakan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mReference.child("ras").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        RasModel ras = data.getValue(RasModel.class);

                        Call<RasResponse> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .addRas(ras.getJenisRas(), ras.getKetRas(), "Bearer " + userToken);

                        call.enqueue(new Callback<RasResponse>() {
                            @Override
                            public void onResponse(Call<RasResponse> call, Response<RasResponse> response) {
                                Toast.makeText(mContext, "Push ras", Toast.LENGTH_SHORT).show();
                                data.getRef().removeValue();
                            }

                            @Override
                            public void onFailure(Call<RasResponse> call, Throwable t) {
                                Toast.makeText(mContext, "Gagal push ras: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mReference.child("perkawinan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        PerkawinanModel perkawinan = data.getValue(PerkawinanModel.class);

                        Call<PerkawinanResponse> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .addPerkawinan(perkawinan.getNecktag(), perkawinan.getNecktag_psg(), perkawinan.getTgl(), "Bearer " + userToken);

                        call.enqueue(new Callback<PerkawinanResponse>() {
                            @Override
                            public void onResponse(Call<PerkawinanResponse> call, Response<PerkawinanResponse> response) {
                                Toast.makeText(mContext, "Push perkawinan", Toast.LENGTH_SHORT).show();
                                data.getRef().removeValue();
                            }

                            @Override
                            public void onFailure(Call<PerkawinanResponse> call, Throwable t) {
                                Toast.makeText(mContext, "Gagal push perkawinan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mReference.child("riwayatPenyakit").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        RiwayatPenyakitModel riwayat = data.getValue(RiwayatPenyakitModel.class);

                        Call<RiwayatPenyakitResponse> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .addRiwayat(riwayat.getPenyakitId(), riwayat.getNecktag(), riwayat.getTglSakit(), riwayat.getObat(),
                                        riwayat.getLamaSakit(), riwayat.getKeterangan(), "Bearer " + userToken);

                        call.enqueue(new Callback<RiwayatPenyakitResponse>() {
                            @Override
                            public void onResponse(Call<RiwayatPenyakitResponse> call, Response<RiwayatPenyakitResponse> response) {
                                Toast.makeText(mContext, "Push riwayat", Toast.LENGTH_SHORT).show();
                                data.getRef().removeValue();
                            }

                            @Override
                            public void onFailure(Call<RiwayatPenyakitResponse> call, Throwable t) {
                                Toast.makeText(mContext, "Gagal push riwayat: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mReference.child("ternak").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        TernakModel ternak = data.getValue(TernakModel.class);

                        Call<TernakResponse> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .addTernak(ternak.getPemilikId(), ternak.getPeternakanId(), ternak.getRasId(), ternak.getKematianId(),
                                        ternak.getJenisKelamin(), ternak.getTglLahir(), ternak.getBobotLahir(), ternak.getPukulLahir(),
                                        ternak.getLamaDiKandungan(), ternak.getLamaLaktasi(), ternak.getTglLepasSapih(), ternak.getBlood(),
                                        ternak.getNecktag_ayah(), ternak.getNecktag_ibu(), ternak.getBobotTubuh(), ternak.getPanjangTubuh(),
                                        ternak.getTinggiTubuh(), ternak.getCacatFisik(), ternak.getCiriLain(), ternak.getStatusAda(), "Bearer " + userToken);

                        call.enqueue(new Callback<TernakResponse>() {
                            @Override
                            public void onResponse(Call<TernakResponse> call, Response<TernakResponse> response) {
                                Toast.makeText(mContext, "Push ternak", Toast.LENGTH_SHORT).show();
                                data.getRef().removeValue();
                            }

                            @Override
                            public void onFailure(Call<TernakResponse> call, Throwable t) {
                                Toast.makeText(mContext, "Gagal push ternak: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mReference.child("peternak").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        PeternakModel peternak = data.getValue(PeternakModel.class);

                        Call<PeternakResponse> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .addPeternak(peternak.getPeternakanId(), peternak.getNamaPeternak(), peternak.getUsername(), peternak.getEmail(), "Bearer " + userToken);

                        call.enqueue(new Callback<PeternakResponse>() {
                            @Override
                            public void onResponse(Call<PeternakResponse> call, Response<PeternakResponse> response) {
                                Toast.makeText(mContext, "Push peternak", Toast.LENGTH_SHORT).show();
                                data.getRef().removeValue();
                            }

                            @Override
                            public void onFailure(Call<PeternakResponse> call, Throwable t) {
                                Toast.makeText(mContext, "Gagal push peternak: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // ------------------ edit data -----------------------------------------------------
        DatabaseReference mReference2 = mDatabase.getReference("datas").child("editData");

        mReference2.child("kematian").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        KematianModel kematian = data.getValue(KematianModel.class);

                        Call<KematianResponse> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .editKematian(kematian.getId(), kematian.getTgl_kematian(), kematian.getWaktu_kematian(), kematian.getPenyebab(), kematian.getKondisi(), "Bearer " + userToken);

                        call.enqueue(new Callback<KematianResponse>() {
                            @Override
                            public void onResponse(Call<KematianResponse> call, Response<KematianResponse> response) {
                                Toast.makeText(mContext, "Push data kematian: " + kematian.getId(), Toast.LENGTH_SHORT).show();
                                data.getRef().removeValue();
                            }

                            @Override
                            public void onFailure(Call<KematianResponse> call, Throwable t) {
                                Toast.makeText(mContext, "Gagal push kematian "+ kematian.getId() + ": " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mReference2.child("pemilik").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        PemilikModel pemilik = data.getValue(PemilikModel.class);

                        Call<PemilikResponse> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .editPemilik(pemilik.getId(), pemilik.getKtp(), pemilik.getNama_pemilik(), "Bearer " + userToken);

                        call.enqueue(new Callback<PemilikResponse>() {
                            @Override
                            public void onResponse(Call<PemilikResponse> call, Response<PemilikResponse> response) {
                                Toast.makeText(mContext, "Push pemilik: " + pemilik.getId(), Toast.LENGTH_SHORT).show();
                                data.getRef().removeValue();
                            }

                            @Override
                            public void onFailure(Call<PemilikResponse> call, Throwable t) {
                                Toast.makeText(mContext, "Gagal push pemilik: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mReference2.child("penyakit").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        PenyakitModel penyakit = data.getValue(PenyakitModel.class);

                        Call<PenyakitResponse> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .editPenyakit(penyakit.getId(), penyakit.getNamaPenyakit(), penyakit.getKetPenyakit(), "Bearer " + userToken);

                        call.enqueue(new Callback<PenyakitResponse>() {
                            @Override
                            public void onResponse(Call<PenyakitResponse> call, Response<PenyakitResponse> response) {
                                Toast.makeText(mContext, "Push penyakit: " + penyakit.getId(), Toast.LENGTH_SHORT).show();
                                data.getRef().removeValue();
                            }

                            @Override
                            public void onFailure(Call<PenyakitResponse> call, Throwable t) {
                                Toast.makeText(mContext, "Gagal push penyakit: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mReference2.child("peternakan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        PeternakanModel peternakan = data.getValue(PeternakanModel.class);

                        Call<PeternakanResponse> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .editPeternakan(peternakan.getId(), peternakan.getNamaPeternakan(), peternakan.getKeterangan(), "Bearer " + userToken);

                        call.enqueue(new Callback<PeternakanResponse>() {
                            @Override
                            public void onResponse(Call<PeternakanResponse> call, Response<PeternakanResponse> response) {
                                Toast.makeText(mContext, "Push peternakan", Toast.LENGTH_SHORT).show();
                                data.getRef().removeValue();
                            }

                            @Override
                            public void onFailure(Call<PeternakanResponse> call, Throwable t) {
                                Toast.makeText(mContext, "Gagal push peternakan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mReference2.child("ras").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        RasModel ras = data.getValue(RasModel.class);

                        Call<RasResponse> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .editRas(ras.getId(), ras.getJenisRas(), ras.getKetRas(), "Bearer " + userToken);

                        call.enqueue(new Callback<RasResponse>() {
                            @Override
                            public void onResponse(Call<RasResponse> call, Response<RasResponse> response) {
                                Toast.makeText(mContext, "Push ras", Toast.LENGTH_SHORT).show();
                                data.getRef().removeValue();
                            }

                            @Override
                            public void onFailure(Call<RasResponse> call, Throwable t) {
                                Toast.makeText(mContext, "Gagal push ras: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mReference2.child("perkawinan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        PerkawinanModel perkawinan = data.getValue(PerkawinanModel.class);

                        Call<PerkawinanResponse> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .editPerkawinan(perkawinan.getId(), perkawinan.getNecktag(), perkawinan.getNecktag_psg(), perkawinan.getTgl(), "Bearer " + userToken);

                        call.enqueue(new Callback<PerkawinanResponse>() {
                            @Override
                            public void onResponse(Call<PerkawinanResponse> call, Response<PerkawinanResponse> response) {
                                Toast.makeText(mContext, "Push perkawinan", Toast.LENGTH_SHORT).show();
                                data.getRef().removeValue();
                            }

                            @Override
                            public void onFailure(Call<PerkawinanResponse> call, Throwable t) {
                                Toast.makeText(mContext, "Gagal push perkawinan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mReference2.child("riwayatPenyakit").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        RiwayatPenyakitModel riwayat = data.getValue(RiwayatPenyakitModel.class);

                        Call<RiwayatPenyakitResponse> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .editRiwayat(riwayat.getId(), riwayat.getPenyakitId(), riwayat.getNecktag(), riwayat.getTglSakit(), riwayat.getObat(),
                                        riwayat.getLamaSakit(), riwayat.getKeterangan(), "Bearer " + userToken);

                        call.enqueue(new Callback<RiwayatPenyakitResponse>() {
                            @Override
                            public void onResponse(Call<RiwayatPenyakitResponse> call, Response<RiwayatPenyakitResponse> response) {
                                Toast.makeText(mContext, "Push riwayat", Toast.LENGTH_SHORT).show();
                                data.getRef().removeValue();
                            }

                            @Override
                            public void onFailure(Call<RiwayatPenyakitResponse> call, Throwable t) {
                                Toast.makeText(mContext, "Gagal push riwayat: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mReference2.child("ternak").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        TernakModel ternak = data.getValue(TernakModel.class);

                        Call<TernakResponse> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .editTernak(ternak.getNecktag(), ternak.getPemilikId(), ternak.getPeternakanId(), ternak.getRasId(), ternak.getKematianId(),
                                        ternak.getJenisKelamin(), ternak.getTglLahir(), ternak.getBobotLahir(), ternak.getPukulLahir(),
                                        ternak.getLamaDiKandungan(), ternak.getLamaLaktasi(), ternak.getTglLepasSapih(), ternak.getBlood(),
                                        ternak.getNecktag_ayah(), ternak.getNecktag_ibu(), ternak.getBobotTubuh(), ternak.getPanjangTubuh(),
                                        ternak.getTinggiTubuh(), ternak.getCacatFisik(), ternak.getCiriLain(), ternak.getStatusAda(), "Bearer " + userToken);

                        call.enqueue(new Callback<TernakResponse>() {
                            @Override
                            public void onResponse(Call<TernakResponse> call, Response<TernakResponse> response) {
                                Toast.makeText(mContext, "Push ternak", Toast.LENGTH_SHORT).show();
                                data.getRef().removeValue();
                            }

                            @Override
                            public void onFailure(Call<TernakResponse> call, Throwable t) {
                                Toast.makeText(mContext, "Gagal push ternak: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mReference2.child("peternak").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        PeternakModel peternak = data.getValue(PeternakModel.class);

                        Call<PeternakResponse> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .editPeternak(peternak.getId(), peternak.getPeternakanId(), peternak.getNamaPeternak(), "Bearer " + userToken);

                        call.enqueue(new Callback<PeternakResponse>() {
                            @Override
                            public void onResponse(Call<PeternakResponse> call, Response<PeternakResponse> response) {
                                Toast.makeText(mContext, "Push peternak", Toast.LENGTH_SHORT).show();
                                data.getRef().removeValue();
                            }

                            @Override
                            public void onFailure(Call<PeternakResponse> call, Throwable t) {
                                Toast.makeText(mContext, "Gagal push peternak: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
