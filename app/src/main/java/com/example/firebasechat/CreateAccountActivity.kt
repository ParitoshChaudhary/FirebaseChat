package com.example.firebasechat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccountActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        mAuth = FirebaseAuth.getInstance()

        btn_create.setOnClickListener{
            val email = ed_email.text.toString().trim()
            val password = ed_password.text.toString().trim()
            val name = ed_display_name.text.toString().trim()

            if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password) || !TextUtils.isEmpty(name)){
                createAccount(email, password, name)
            }else{
                Toast.makeText(this, "All Fields are required", Toast.LENGTH_SHORT)
                    .show()
            }

        }

    }

    private fun createAccount(email: String, password: String, name: String){
        mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){
                task: Task<AuthResult> ->
            if (task.isSuccessful){
                val currentUser = mAuth!!.currentUser
                val userId = currentUser!!.uid

                mDatabase = FirebaseDatabase.getInstance().reference.child("Users")
                    .child(userId)

                val userObject = HashMap<String, String>()
                userObject["display_name"] = name
                userObject["status"] = "Hello There..."
                userObject["image"] = "Default"
                userObject["thumb_image"] = "Default"

                mDatabase!!.setValue(userObject).addOnCompleteListener(this){
                        task: Task<Void> ->
                    if(task.isSuccessful){
                        Toast.makeText(this, "User Created", Toast.LENGTH_SHORT)
                            .show()
                        Log.d("USER : ", "CREATED SUCCESSFULLY")
                        val intent = Intent(this, DashboardActivity::class.java)
                        intent.putExtra("name", name)
                        startActivity(intent)
                        finish()

                    }else{
                        Toast.makeText(this, "User Not Created", Toast.LENGTH_SHORT)
                            .show()
                        Log.d("USER : ", "UNSUCCESSFUL")
                    }
                }
            }else{
                Log.e("Exception", "CREATE ACCOUNT FAILURE", task.exception)
            }
        }
    }

}