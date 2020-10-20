package com.example.firebasechat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebasechat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    private var mCurrentUser: FirebaseUser? = null
    private var mDatabase: DatabaseReference? = null
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar!!.title = "Profile"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (intent.extras != null){
            userId = intent.extras!!.get("user_id").toString()
            mCurrentUser = FirebaseAuth.getInstance().currentUser
            mDatabase = FirebaseDatabase.getInstance().reference.child("Users")
                .child(userId!!)
            setupProfile()
        }
    }

    private fun setupProfile(){
        mDatabase!!.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("display_name").value.toString()
                val status = snapshot.child("status").value.toString()
                val image = snapshot.child("image").value.toString()

                txt_profile_name.text = name
                txt_profile_status.text = status
                Picasso.get()
                    .load(image)
                    .placeholder(R.drawable.profile_img)
                    .into(img_profile_pic)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}