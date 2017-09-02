package com.example.thinhhoang.mychat.Common

import android.content.Context
import android.widget.Toast

/**
 * Created by thinhhoang on 9/2/17.
 */
class Toast {
    public fun Toast(mContext: Context, message: String){
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}