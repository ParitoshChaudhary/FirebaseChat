package com.example.firebasechat.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechat.R
import com.example.firebasechat.adapters.UserAdapter
import com.example.firebasechat.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_users.*

class UsersFragment : Fragment() {

    private var mUserDatabase: DatabaseReference? = null
    private var userList: ArrayList<Users>? = null
    private var userAdapter: UserAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var rcv_users: RecyclerView? = null
    private var mStorageRef: StorageReference? = null
    private var mUser: FirebaseUser? = null
    private var mDatabase: DatabaseReference? = null
    private var currentUserName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_users, container, false)
        rcv_users = rootView.findViewById(R.id.rcv_users) as RecyclerView
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mUserDatabase = FirebaseDatabase.getInstance().reference.child("Users")
        userList = ArrayList<Users>()

        mUser = FirebaseAuth.getInstance().currentUser
        val userId = mUser!!.uid
        mDatabase = mUserDatabase!!.child(userId)

        mDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUserName = snapshot.child("display_name").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("USER LIST ERROR", "===========>> $error")
            }

        })

        rcv_users?.setHasFixedSize(true)

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
                    user.user_id = ds.child("user_id").getValue(String::class.java)
                    if (!user.display_name.equals(currentUserName)){
                        userList?.add(user)
                    }
                    Log.e("USER DATA", "=====${user.display_name} +  ${user.user_status} + " +
                            "${user.thumb_img} + ${user.profile_image}")
                    userAdapter = context?.let { UserAdapter(userList!!, it) }
                    layoutManager = LinearLayoutManager(context)
                    rcv_users?.adapter = userAdapter
                    rcv_users?.layoutManager = layoutManager
                }
                userAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("USER DATA ERROR", "========$error")
            }
        })
    }
}