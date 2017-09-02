package com.example.thinhhoang.mychat

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.example.thinhhoang.mychat.Common.Toast
import com.example.thinhhoang.mychat.Config.SPManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_authentication.*


class AuthenticationActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var myRef: DatabaseReference
    lateinit var loading: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (SPManager(this@AuthenticationActivity).checkSharedPreferences()) {
            onGo()
        }
        setContentView(R.layout.activity_authentication)
        onChangeColoroStatusBar()
        initFirebase();
    }
    private fun onChangeColoroStatusBar() {
        val window: Window = this@AuthenticationActivity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    private fun showProgressBar(isLoading: Boolean) {
        if (isLoading) {
            loading = ProgressDialog.show(this@AuthenticationActivity, "Loading", "Please wait...")
        } else {
            loading.dismiss()
        }
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btnSignIn -> {
                if (!checkEdt()) {
                    return
                }
                onSignIn()
            }
            R.id.btnSignUp -> {
                if (!checkEdt()) {
                    return
                }
                onSignUp()
            }
        }
    }

    private fun onSignIn() {
        val email = edtEmail.text.toString()
        val password = edtPassword.text.toString()
        showProgressBar(true)
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        onGo()
                        showProgressBar(false)
                        val mUser: FirebaseUser = mAuth.currentUser as FirebaseUser
                        myRef.child("/Users/${mUser.uid}/profile").setValue(mUser)
                        SPManager(this@AuthenticationActivity).writeSharedPreferences(mUser)
                    } else {
                        showProgressBar(false)
                        Toast().Toast(this@AuthenticationActivity, it.exception.toString())
                    }
                }
    }

    private fun onSignUp() {
        val email = edtEmail.text.toString()
        val password = edtPassword.text.toString()
        showProgressBar(true)
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        showProgressBar(false)
                        Toast().Toast(this@AuthenticationActivity, "Created ${email} Success!")
                    } else {
                        showProgressBar(false)
                        Toast().Toast(this@AuthenticationActivity, "${it.exception}")
                    }
                }
    }

    private fun initFirebase() {
        mAuth = FirebaseAuth.getInstance()
        myRef = FirebaseDatabase.getInstance().getReference()
    }

    private fun checkEdt(): Boolean {
        var isCheck: Boolean = true
        val email = edtEmail.text.toString()
        val password = edtPassword.text.toString()
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Please enter email!")
            isCheck = false
        }
        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Please enter password!")
            isCheck = false
        }
        return isCheck
    }

    private fun onGo() {
        startActivity(Intent(this@AuthenticationActivity, MainActivity::class.java))
        finish()
    }
}
