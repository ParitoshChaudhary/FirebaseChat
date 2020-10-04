package com.example.firebasechat.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebasechat.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_status.*

class StatusActivity : AppCompatActivity() {

    private var mDatabase: DatabaseReference? = null
    private var mCurrentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)

        supportActionBar!!.title = "Status"

        if(intent.extras != null){
            val oldStatus = intent.extras!!.get("status")
            ed_update_status.setText(oldStatus.toString())
        }else{
            ed_update_status.setText("Enter Your Status Here")
        }

        btn_update_status.setOnClickListener {
            mCurrentUser = FirebaseAuth.getInstance().currentUser
            val userId = mCurrentUser!!.uid

            mDatabase = FirebaseDatabase.getInstance().reference
                .child("Users")
                .child(userId)

            val newStatus = ed_update_status.text.toString().trim()

            mDatabase!!.child("status")
                .setValue(newStatus).addOnCompleteListener(this){
                    task: Task<Void> ->
                    if (task.isSuccessful){
                        Toast.makeText(this, "Status Updated Successfully", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(this, SettingsActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this, "Unable to update status", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

        }

    }
}