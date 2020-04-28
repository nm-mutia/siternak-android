package com.project.siternak.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.project.siternak.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.et_name) EditText etName;
    @BindView(R.id.et_username) EditText etUsername;
    @BindView(R.id.et_email) EditText etEmail;
    @BindView(R.id.et_passsword) EditText etPassword;
    @BindView(R.id.et_confirm_passsword) EditText etPasswordConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

//    public void moveToHome(){
//        Intent intent=new Intent(RegisterActivity.this, MainActivity.class);
//        startActivity(intent);
//        finish();
//    }

//    @OnClick(R.id.tv_register)
//    public void register(){
//        if (!validated())return;
//
//        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Loading");
//        pDialog.setCancelable(false);
//        pDialog.show();
//
//        String username = this.etUsername.getText().toString();
//        String email = this.etEmail.getText().toString();
//        String password = this.etPassword.getText().toString();
//        String passwordConfirmation = this.etPasswordConfirmation.getText().toString();
//
//        RegisterInteractor interactor = new RegisterInteractor(username, email, password, passwordConfirmation, this);
//        interactor.setCallback(new OnInteractListener<User>() {
//            @Override
//            public void onSuccess(User object) {
//                pDialog.dismiss();
//                moveToHome();
//            }
//
//            @Override
//            public void onFailure(SLException e) {
//                pDialog.dismiss();
//                SweetAlertDialog swal = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.ERROR_TYPE);
//                swal.setTitleText("Error");
//                swal.setContentText(e.getMessage());
//                swal.show();
//
//                if (e.getErrors() != null){
//                    for (Map.Entry<String, List<String>> error : e.getErrors().entrySet()){
//                        if(error.getKey().equals("username")){
//                            etUsername.setError(error.getValue().get(0));
//                        }
//                        if(error.getKey().equals("email")){
//                            etEmail.setError(error.getValue().get(0));
//                        }
//                        if(error.getKey().equals("password")){
//                            etPassword.setError(error.getValue().get(0));
//                        }
//                    }
//                }
//            }
//        });
//        interactor.execute();
//    }
//
//    private Boolean validated(){
//        Boolean res = true;
//
//        String username = this.etUsername.getText().toString();
//        String email = this.etEmail.getText().toString();
//        String password = this.etPassword.getText().toString();
//        String passwordConfirmation = this.etPasswordConfirmation.getText().toString();
//
//        if (username.equals("") || username.length() < 8){
//            res = false;
//            this.etUsername.setError("Username perlu diisi dan minimal 8 karakter");
//        }
//        if (email.equals("")){
//            res = false;
//            this.etEmail.setError("Email perlu diisi");
//        }
//        if (password.equals("") || password.length() < 6){
//            res = false;
//            this.etPassword.setError("Password perlu diisi dan minimal terdiri dari 6 karakter");
//        }
//        if (!passwordConfirmation.equals(password)){
//            res = false;
//            this.etPasswordConfirmation.setError("Konfirmasi password harus sama seperti password");
//        }
//
//        return res;
//    }

}
