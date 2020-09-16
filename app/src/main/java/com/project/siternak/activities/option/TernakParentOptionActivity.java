package com.project.siternak.activities.option;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.siternak.R;
import com.project.siternak.adapter.TernakOptionAdapter;
import com.project.siternak.models.data.TernakModel;
import com.project.siternak.responses.TernakGetResponse;
import com.project.siternak.rest.RetrofitClient;
import com.project.siternak.utils.DialogUtils;
import com.project.siternak.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TernakParentOptionActivity extends AppCompatActivity {
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.sv)
    SearchView sv;

    private TernakOptionAdapter adapter;
    private String userToken;
    private int parent, jenisKelamin;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private List<TernakModel> datas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_option);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_cross);
        TextView tv_actionbar_title=getSupportActionBar().getCustomView().findViewById(R.id.tv_actionbar_title);

        ButterKnife.bind(this);
        parent = (int) getIntent().getIntExtra("parent", 0);
        jenisKelamin = (int) getIntent().getIntExtra("match", 0);

        if(parent == 5){
            tv_actionbar_title.setText("Necktag Ayah");
        }
        else if(parent == 6){
            tv_actionbar_title.setText("Necktag Ibu");
        }

        if(jenisKelamin == 1){
            tv_actionbar_title.setText("Necktag Jantan");
        }
        else if(jenisKelamin == 2){
            tv_actionbar_title.setText("Necktag Betina");
        }

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
        setData();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @OnClick(R.id.ib_actionbar_close)
    public void close(){
        this.onBackPressed();
    }

    private void setData() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        SweetAlertDialog loadingDialog = DialogUtils.getLoadingPopup(this);

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("options").child("ternak");

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadingDialog.cancel();

                if(dataSnapshot.exists()){
                    datas = new ArrayList<>();

                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        TernakModel ternak = data.getValue(TernakModel.class);
                        datas.add(ternak);
                    }

                    if(parent == 5 || jenisKelamin == 1){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            datas.removeIf(n -> (n.getJenisKelamin().equals("Betina"))); //hapus data betina -> ayah/jantan
                        }
                    }
                    else if(parent == 6 || jenisKelamin == 2){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            datas.removeIf(n -> (n.getJenisKelamin().equals("Jantan"))); //hapus data jantan -> ibu/betina
                        }
                    }

                    adapter = new TernakOptionAdapter(TernakParentOptionActivity.this, datas);
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loadingDialog.cancel();
            }
        });
    }
}
