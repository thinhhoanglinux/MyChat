package com.example.thinhhoang.mychat

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.example.thinhhoang.mychat.Config.SPManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var myRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onChangeStatusBar()
        setContentView(R.layout.activity_main)
        main.setPadding(0, getHeightStatusBar(), 0, 0)
        initFirebase()
        txtGreeding.setText(mAuth.currentUser?.email)
        txtGreeding.setOnClickListener {
            val ab: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
            ab.setTitle("Warning")
                    .setMessage("Do you want to log out?")
                    .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            onGoAuthentication()
                        }
                    })
                    .setNegativeButton("No", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            ab.setCancelable(true)
                        }
                    })
                    .setNeutralButton("Ask me late", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            ab.setCancelable(true)
                        }
                    })
            val alert: AlertDialog = ab.create()
            alert.show()
        }
        initToolbar()
    }

    override fun onStart() {
        super.onStart()
        val mUser = mAuth.currentUser as FirebaseUser
        upDateUI(mUser)
    }

    private fun onChangeStatusBar() {
        val window: Window = this@MainActivity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    private fun getHeightStatusBar(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_main)
        supportActionBar?.title = "Chat"
        supportActionBar?.subtitle = "Hello, ${mAuth.currentUser?.email}"
        toolbar_main.setNavigationIcon(R.drawable.menu)
        toolbar_main.setNavigationOnClickListener {
            drawerLayout.openDrawer(Gravity.START)
        }
    }

    private fun initFirebase() {
        mAuth = FirebaseAuth.getInstance()
        myRef = FirebaseDatabase.getInstance().getReference()
    }

    private fun onGoAuthentication() {
        mAuth.signOut()
        startActivity(Intent(this@MainActivity, AuthenticationActivity::class.java))
        finish()
        SPManager(this@MainActivity).clearSharedPreferences()
    }

    private fun upDateUI(mUser: FirebaseUser) {
        if (mUser != null) {
            val email = mUser.email
            val name = mUser.displayName
            val phone = mUser.phoneNumber
            val photo = mUser.photoUrl
        }
    }
}
