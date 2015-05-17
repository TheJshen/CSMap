package com.example.kcco.csmap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;
/**
 * Created by jason on 5/16/15.
 */
public class Messenger {
    public static void toast(String message, Activity activity) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }

    public static void error(String errorMessage, Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(errorMessage);
        builder.setTitle("Sorry");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                //close the dialog
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
