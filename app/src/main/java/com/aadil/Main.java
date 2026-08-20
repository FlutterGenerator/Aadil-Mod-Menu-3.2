package com.aadil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Process;
import android.text.Html;
import android.text.Spanned;

public class Main {

    private static native void CheckOverlayPermission(Context context);

    static {
        System.loadLibrary("GamerAadil");
    }

    public static void StartWithoutPermission(final Context context) {
        CrashHandler.init(context, true);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Notice");
        builder.setCancelable(false);

        String message = "It seems like you have implemented "
                + "\"StartWithoutPermission\" Activity but Sorry this Menu works "
                + "only with overlay permission so please implement with "
                + "\"Start\" Activity.";

        Spanned spanned;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY);
        } else {
            spanned = Html.fromHtml(message);
        }

        builder.setMessage(spanned);

        builder.setPositiveButton("Close App",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Process.killProcess(Process.myPid());
                    }
                });

        builder.show();
    }

    public static void Start(Context context) {
        CrashHandler.init(context, false);
        CheckOverlayPermission(context);
    }
}