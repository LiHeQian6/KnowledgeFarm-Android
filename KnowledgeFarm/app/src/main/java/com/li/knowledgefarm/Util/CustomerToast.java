package com.li.knowledgefarm.Util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class CustomerToast extends Toast {

    private static Toast customerToast;

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public CustomerToast(Context context) {
        super(context);
    }

    public static Toast getInstance(Context context,String toastText,int type){
        synchronized (CustomerToast.class) {
            if (customerToast == null) {
                customerToast = Toast.makeText(context.getApplicationContext(), "", type);
                customerToast.setGravity(Gravity.BOTTOM, 0, 0);
            }
            customerToast.setText(toastText);
        }
        return customerToast;
    }
}
