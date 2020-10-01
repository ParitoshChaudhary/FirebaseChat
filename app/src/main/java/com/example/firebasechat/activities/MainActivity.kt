package com.example.firebasechat.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebasechat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var user: FirebaseUser? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener {
            firebaseAuth: FirebaseAuth ->
            user = mAuth!!.currentUser

            if (user != null){
                startActivity(Intent(this, DashboardActivity::class.java))
            }else{
                Toast.makeText(this, "User Not Signed In", Toast.LENGTH_SHORT).show()
            }
        }

        btn_create_acnt.setOnClickListener{
            startActivity(Intent(this, CreateAccountActivity::class.java))
        }

        btn_login.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }

    override fun onStart() {
        super.onStart()
        mAuthListener?.let { mAuth!!.addAuthStateListener(it) }
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null){
            mAuth!!.removeAuthStateListener(mAuthListener!!)
        }
    }
}