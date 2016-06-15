package com.appdevper.mapnavi.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * Created by worawit on 3/6/16.
 */
public class Utils {
    public static AlertDialog showDialog(final Activity act, String header, String msg) {
        return new AlertDialog.Builder(act)
                .setTitle(header)
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static AlertDialog showDialog(final Activity act, String header, String msg, DialogInterface.OnClickListener listener) {
        return new AlertDialog.Builder(act)
                .setTitle(header)
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, listener)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return dist;
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}
