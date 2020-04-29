package com.project.siternak.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.project.siternak.R;
import com.project.siternak.activities.home.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.et_email) EditText editTextEmail;
    @BindView(R.id.et_passsword) EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(Authenticated.getInstance().isLoggedIn()){
//            moveToHome();
//        }
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

//    sementara !!
    @OnClick(R.id.tv_login)
    public void moveToAfterLogin(){
        Intent intent=new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }


//    public void moveToHome(){
//        Intent intent=new Intent(LoginActivity.this, MainActivity.class);
//        startActivity(intent);
//        finish();
//    }

//    @OnClick(R.id.tv_login)
//    public void doLogin(View view){
//        if (!validated()) return;
//
//        String username = this.editTextUsername.getText().toString();
//        String password = this.editTextPassword.getText().toString();
//
//        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Loading");
//        pDialog.setCancelable(false);
//        pDialog.show();
//
//        LoginInteractor interactor = new LoginInteractor(username, password, this, new OnInteractListener() {
//            @Override
//            public void onSuccess(Object object) {
//                pDialog.dismiss();
//                moveToHome();
//            }
//
//            @Override
//            public void onFailure(SLException e) {
//                pDialog.dismiss();
//                SweetAlertDialog swal = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
//                swal.setTitleText("Error");
//                swal.setContentText(e.getMessage());
//                swal.show();
//            }
//        });
//        interactor.execute();
//    }

//    private boolean validated(){
//        boolean pass = true;
//        String username = this.editTextUsername.getText().toString();
//        String password = this.editTextPassword.getText().toString();
//
//        if (username.isEmpty()){
//            pass = false;
//            this.editTextUsername.setError("Username perlu diisi");
//        }
//        if (password.isEmpty()){
//            pass = false;
//            this.editTextPassword.setError("Password perlu diisi");
//        }
//
//        return pass;
//    }
}
