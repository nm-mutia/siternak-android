package com.project.siternak.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.widget.TextView;

import com.project.siternak.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DialogUtils {
    @FunctionalInterface
    public interface OptionClickListener {
        void onOptionClick(int option);
    }

    public static void dialogMultiPick(Context context, CharSequence[] sequence, String title, TextView view){
        Activity activity = (Activity) context;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
                .setItems(sequence, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        view.setText(sequence[which]);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void dialogMultiPick(
            Context context, CharSequence[] sequence, String title, TextView view,
            OptionClickListener optionClickListener
    ){
        Activity activity = (Activity) context;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
                .setItems(sequence, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        view.setText(sequence[which]);
                        optionClickListener.onOptionClick(which);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void swalFinishingActivity(Context context, String text){
        SweetAlertDialog success= new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
        success.setTitleText(text).setConfirmText("OK")
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        ((Activity)context).setResult(Activity.RESULT_OK);
                        ((Activity)context).finish();
                    }
                });
        success.show();
    }

    public static void swalSuccess(Context context, String text){
        SweetAlertDialog failed= new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
        failed.setTitleText(text).setConfirmText("OK");
        failed.show();
    }

    public static void swalFailed(Context context, String text){
        SweetAlertDialog failed= new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        failed.setTitleText(text).setConfirmText("OK");
        failed.show();
    }

    public static SweetAlertDialog getLoadingPopup(Context context){
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Mohon Tunggu");
        pDialog.setCancelable(false);
        pDialog.show();
        return pDialog;
    }

    private static final String ALERT_TYPE = "_ALERT_TYPE";
    private static final String ALERT_MESSAGE = "_ALERT_TITLE";
    enum AlertType {
        SUCCESS(0), FAILED(1), WARNING(2);

        int status;
        AlertType(int status){
            this.status = status;
        }

        int getStatus(){
            return status;
        }
    }

    public static Intent getIntentWithSwal(
            Context context, Class<? extends Activity> activity,
            AlertType type, String message

    ){
        Intent intent = new Intent(context.getApplicationContext(), activity);
        intent.putExtra(ALERT_TYPE, type.getStatus());
        intent.putExtra(ALERT_MESSAGE, message);

        return intent;
    }
    private static boolean colorIsSetted = false;
    private static int dangerColor = 0;
    private static int warningColor = 0;
    private static int successColor = 0;

    public static void showAlertIfExist(Context context, Intent intent){
        if(!colorIsSetted){
            dangerColor = context.getResources().getColor(R.color.danger);
            warningColor = context.getResources().getColor(R.color.yellowProgress);
            successColor = context.getResources().getColor(R.color.greenSuccess);
        }

        String title = intent.getStringExtra(ALERT_MESSAGE);

        if(title == null)return;

        int statusType = intent.getIntExtra(ALERT_TYPE, -1);

        if(statusType == -1)return;

        int swalType = 0;
        int color = 0;

        if(statusType == AlertType.SUCCESS.getStatus()){
            swalType = SweetAlertDialog.SUCCESS_TYPE;
            color = successColor;
        }
        else if(statusType == AlertType.WARNING.getStatus()){
            swalType = SweetAlertDialog.PROGRESS_TYPE;
            color = warningColor;
        }
        else if(statusType == AlertType.FAILED.getStatus()){
            swalType = SweetAlertDialog.ERROR_TYPE;
            color = dangerColor;
        }

        SweetAlertDialog pDialog = new SweetAlertDialog(context, swalType);
        pDialog.getProgressHelper().setBarColor(color);
        pDialog.setTitleText(title);
        pDialog.show();
    }
}
