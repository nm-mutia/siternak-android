package com.project.siternak.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.siternak.R;
import com.project.siternak.activities.auth.LoginActivity;
import com.project.siternak.fragments.DashboardFragment;
import com.project.siternak.fragments.ProfileFragment;
import com.project.siternak.fragments.ScanFragment;
import com.project.siternak.models.data.KematianModel;
import com.project.siternak.models.data.PemilikModel;
import com.project.siternak.models.data.PenyakitModel;
import com.project.siternak.models.data.PerkawinanModel;
import com.project.siternak.models.data.PeternakanModel;
import com.project.siternak.models.data.RasModel;
import com.project.siternak.models.data.RiwayatPenyakitModel;
import com.project.siternak.models.data.TernakModel;
import com.project.siternak.responses.KematianResponse;
import com.project.siternak.responses.OptionsResponse;
import com.project.siternak.responses.PemilikResponse;
import com.project.siternak.responses.PenyakitResponse;
import com.project.siternak.responses.PerkawinanResponse;
import com.project.siternak.responses.PeternakanResponse;
import com.project.siternak.responses.RasResponse;
import com.project.siternak.responses.RiwayatPenyakitResponse;
import com.project.siternak.responses.TernakResponse;
import com.project.siternak.rest.RetrofitClient;
import com.project.siternak.utils.NetworkManager;
import com.project.siternak.utils.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    Fragment selectedFragment;
    private String userToken;
    private boolean isInitialState = true;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        userToken = SharedPrefManager.getInstance(this).getAccessToken();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(this);
        bottomNav.setSelectedItemId(R.id.nav_dashboard);

        displayFragment(new DashboardFragment());
    }


    private void displayFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        if(NetworkManager.isNetworkAvailable(MainActivity.this)){
            Toast.makeText(this, "Initial state: " + isInitialState, Toast.LENGTH_SHORT).show();

            if(isInitialState){
                syncData();
                isInitialState = false;
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        selectedFragment = null;

        switch (item.getItemId()) {
            case R.id.nav_dashboard:
                selectedFragment = new DashboardFragment();
                break;
            case R.id.nav_profile:
                selectedFragment = new ProfileFragment();
                break;
            case R.id.nav_scan:
                selectedFragment = new ScanFragment();
                break;
        }

        if(selectedFragment != null) {
            displayFragment(selectedFragment);
        }

        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void syncData(){
        pushToDb(); // retrieve from firebase
        pullFromDb(); //store to firebase
    }

    private void pullFromDb(){
        Call<OptionsResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getOptions("Bearer " + this.userToken);

        call.enqueue(new Callback<OptionsResponse>() {
            @Override
            public void onResponse(Call<OptionsResponse> call, Response<OptionsResponse> response) {
                OptionsResponse resp = response.body();

                mDatabase = FirebaseDatabase.getInstance();
                mReference = mDatabase.getReference("options");

                List<KematianModel> kematians = resp.getKematians();
                List<PemilikModel> pemiliks = resp.getPemiliks();
                List<PenyakitModel> penyakits = resp.getPenyakits();
                List<PeternakanModel> peternakans = resp.getPeternakan();
                List<RasModel> ras = resp.getRas();
                List<TernakModel> ternaks = resp.getTernaks();

                for(KematianModel data : kematians){
                    mReference.child("kematian").child(data.getId().toString()).setValue(data);
                }

                for(PemilikModel data : pemiliks){
                    mReference.child("pemilik").child(data.getId().toString()).setValue(data);
                }

                for(PenyakitModel data : penyakits){
                    mReference.child("penyakit").child(data.getId().toString()).setValue(data);
                }

                for(PeternakanModel data : peternakans){
                    mReference.child("peternakan").child(data.getId().toString()).setValue(data);
                }

                for(RasModel data : ras){
                    mReference.child("ras").child(data.getId().toString()).setValue(data);
                }

                for(TernakModel data : ternaks){
                    mReference.child("ternak").child(data.getNecktag()).setValue(data);
                }
            }

            @Override
            public void onFailure(Call<OptionsResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal sync: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pushToDb(){
        mDatabase = FirebaseDatabase.getInstance();
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
                                Toast.makeText(MainActivity.this, "Push kematian", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<KematianResponse> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Gagal push kematian: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(MainActivity.this, "Push pemilik", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<PemilikResponse> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Gagal push pemilik: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(MainActivity.this, "Push penyakit", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<PenyakitResponse> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Gagal push penyakit: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(MainActivity.this, "Push peternakan", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<PeternakanResponse> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Gagal push peternakan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(MainActivity.this, "Push ras", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<RasResponse> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Gagal push ras: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(MainActivity.this, "Push perkawinan", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<PerkawinanResponse> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Gagal push perkawinan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(MainActivity.this, "Push riwayat", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<RiwayatPenyakitResponse> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Gagal push riwayat: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(MainActivity.this, "Push ternak", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<TernakResponse> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Gagal push ternak: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(MainActivity.this, "Push kematian", Toast.LENGTH_SHORT).show();
                                mReference2.child("kematian").child(String.valueOf(kematian.getId())).setValue(null);
                            }

                            @Override
                            public void onFailure(Call<KematianResponse> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Gagal push kematian: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(MainActivity.this, "Push pemilik", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<PemilikResponse> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Gagal push pemilik: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(MainActivity.this, "Push penyakit", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<PenyakitResponse> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Gagal push penyakit: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(MainActivity.this, "Push peternakan", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<PeternakanResponse> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Gagal push peternakan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(MainActivity.this, "Push ras", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<RasResponse> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Gagal push ras: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                                .editPerkawinan(perkawinan.getId(), perkawinan.getNecktag(), perkawinan.getNecktag_psg(), perkawinan.getTgl(), "Bearer " + userToken);

                        call.enqueue(new Callback<PerkawinanResponse>() {
                            @Override
                            public void onResponse(Call<PerkawinanResponse> call, Response<PerkawinanResponse> response) {
                                Toast.makeText(MainActivity.this, "Push perkawinan", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<PerkawinanResponse> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Gagal push perkawinan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                                .editRiwayat(riwayat.getId(), riwayat.getPenyakitId(), riwayat.getNecktag(), riwayat.getTglSakit(), riwayat.getObat(),
                                        riwayat.getLamaSakit(), riwayat.getKeterangan(), "Bearer " + userToken);

                        call.enqueue(new Callback<RiwayatPenyakitResponse>() {
                            @Override
                            public void onResponse(Call<RiwayatPenyakitResponse> call, Response<RiwayatPenyakitResponse> response) {
                                Toast.makeText(MainActivity.this, "Push riwayat", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<RiwayatPenyakitResponse> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Gagal push riwayat: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(MainActivity.this, "Push ternak", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<TernakResponse> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Gagal push ternak: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
