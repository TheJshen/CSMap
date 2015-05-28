package com.example.kcco.csmap.DAO;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * Created by KaChan on 5/9/2015.
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

    public static void logException(Exception e, String className, String functionName){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        Log.e(className, functionName + " Error: \n" + sw.toString());
    }
}
