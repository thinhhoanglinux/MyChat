package com.example.thinhhoang.mychat.Config

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseUser

/**
 * Created by thinhhoang on 9/2/17.
 */
class SPManager {
    private var mContext: Context ?= null
    private var sharedPreferences: SharedPreferences ?= null

    constructor(mContext: Context?) {
        this.mContext = mContext
        getSharedPreferences()
    }
    fun getSharedPreferences() {
        sharedPreferences = mContext?.getSharedPreferences("DATA",Context.MODE_PRIVATE)
    }
    fun writeSharedPreferences(users: FirebaseUser){
        var editor: SharedPreferences.Editor ?= sharedPreferences?.edit()
        editor?.putString("KEY_1", users.toString())
        editor?.apply()
    }
    fun checkSharedPreferences(): Boolean{
        var isCheck: Boolean = true
        if(sharedPreferences!!.getString("KEY_1","null").equals("null")){
            isCheck = false
        }
        return isCheck
    }
    fun clearSharedPreferences(){
        sharedPreferences!!.edit().clear().apply()
    }
}