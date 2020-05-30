package com.project.siternak.activities.grafik;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.project.siternak.R;
import com.project.siternak.fragments.GrafikFragment;

import butterknife.OnClick;

public class GrafikFragmentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafik_fragment);

        String grafik = getIntent().getStringExtra("grafik");
        GrafikFragment grafikFragment = new GrafikFragment();
        Bundle bundle = new Bundle();
        bundle.putString("grafik", grafik);
        grafikFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, grafikFragment).commit();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_primary_arrow);

        TextView tv_actionbar_title = getSupportActionBar().getCustomView().findViewById(R.id.tv_actionbar_title);
        tv_actionbar_title.setText("Grafik - " + grafik);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.ib_actionbar_close)
    public void close(){
        onBackPressed();
    }
}
