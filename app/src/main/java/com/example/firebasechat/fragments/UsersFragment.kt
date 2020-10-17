package com.example.firebasechat.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasechat.R
import com.example.firebasechat.adapters.UserAdapter
import com.example.firebasechat.models.Users
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_users.*

class UsersFragment : Fragment() {

    private var mUserDatabase: DatabaseReference? = null
    private var userList: ArrayList<Users>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        mUserDatabase = FirebaseDatabase.getInstance().reference.child("Users")

        rcv_users.setHasFixedSize(true)
        rcv_users.layoutManager = linearLayoutManager

        mUserDatabase!!.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                val count = children.count().toString()
                for(ds in snapshot.children){
                    val user = Users()
                    user.display_name = ds.child("display_name").getValue(String::class.java)
                    user.user_status = ds.child("status").getValue(String::class.java)
                    user.profile_image = ds.child("image").getValue(String::class.java)
                    user.thumb_img = ds.child("thumb_image").getValue(String::class.java)
                    userList?.add(user)
                    Log.e("USER DATA", "=====${user.display_name} +  ${user.user_status} + " +
                            "${user.thumb_img} + ${user.profile_image}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("USER DATA ERROR", "========$error")
            }

        })
    }
}