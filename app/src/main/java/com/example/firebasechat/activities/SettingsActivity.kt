package com.example.firebasechat.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebasechat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private var mDatabase: DatabaseReference? = null
    private var mUser: FirebaseUser? = null
    private var mStorageRef: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        mUser = FirebaseAuth.getInstance().currentUser
        var userId = mUser!!.uid
        mDatabase = FirebaseDatabase.getInstance().reference
            .child("Users")
            .child(userId)

        mDatabase!!.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(data: DataSnapshot) {
                val name = data.child("display_name").value
                var image = data.child("image").value
                val status = data.child("status").value
                var thumbnail = data.child("thumb_image").value

                txt_name.text = name.toString()
                txt_status.text = status.toString()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        btn_change_status.setOnClickListener {
            val intent = Intent(this, StatusActivity::class.java)
            intent.putExtra("status", txt_status.text.toString().trim())
            startActivity(intent)
        }

    }
}