package com.example.firebasechat.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.firebasechat.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        btn_login.setOnClickListener{
            val email = ed_email_login.text.toString().trim()
            val password = ed_password_login.text.toString().trim()

            if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
                loginUser(email, password)
            }else{
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT)
                    .show()
            }

        }

    }

    private fun loginUser(email: String, password: String){
        mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){
            task: Task<AuthResult> ->
            if (task.isSuccessful){
                val name = email.split("@")[0]
                val intent = Intent(this, DashboardActivity::class.java)
                intent.putExtra("name", name)
                startActivity(intent)
                finish()
            }
        }
    }

}