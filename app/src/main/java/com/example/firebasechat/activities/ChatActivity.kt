package com.example.firebasechat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechat.R
import com.example.firebasechat.adapters.MessageAdapter
import com.example.firebasechat.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private var userId: String? = null
    private var mFirebaseDatabaseRef: DatabaseReference? = null
    private var mFirebaseUser: FirebaseUser? = null
    private var rcv_messages: RecyclerView? = null
    private var mLinearLayoutManager: LinearLayoutManager? = null
    private var msgList: ArrayList<Message>? = null
    private var msgAdapter: MessageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mFirebaseUser = FirebaseAuth.getInstance().currentUser
        rcv_messages = findViewById(R.id.rcv_message)

        userId = intent.extras!!.getString("user_id")
        mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager!!.stackFromEnd = true

        mFirebaseDatabaseRef = FirebaseDatabase.getInstance().reference.child("messages")

        mFirebaseDatabaseRef!!.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                for (ds in children){
                    val msg = Message()
                    msg.text = ds.child("text").getValue(String::class.java)
                    msg.id = ds.child("id").getValue(String::class.java)
                    msg.name = ds.child("name").getValue(String::class.java)
                    msgList?.add(msg)
                    msgAdapter = msgList?.let { MessageAdapter(it, this@ChatActivity) }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
}