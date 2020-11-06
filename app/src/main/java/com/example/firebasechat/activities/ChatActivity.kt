package com.example.firebasechat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechat.R
import com.example.firebasechat.adapters.MessageAdapter
import com.example.firebasechat.adapters.UserAdapter
import com.example.firebasechat.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat.*

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

        mFirebaseDatabaseRef = FirebaseDatabase.getInstance().reference.child("messages")

        mFirebaseDatabaseRef!!.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val children = snapshot.children
                    for (ds in children){
                        val msg = Message()
                        msg.text = ds.child("text").getValue(String::class.java)
                        msg.id = ds.child("id").getValue(String::class.java)
                        msg.name = ds.child("name").getValue(String::class.java)
                        msgList?.add(msg)
                        msgAdapter = MessageAdapter(msgList!!, this@ChatActivity)
                        mLinearLayoutManager = LinearLayoutManager(this@ChatActivity)
                        mLinearLayoutManager!!.stackFromEnd = true
                        rcv_messages!!.adapter = msgAdapter
                        rcv_messages!!.layoutManager = mLinearLayoutManager
                    }
                    msgAdapter?.notifyDataSetChanged()
                }else{
                    Log.i("DATA MESSAGE", " ==============>>>> NO DATA AVAILABLE")
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
        btn_send.setOnClickListener {
            if (intent.extras!!.get("name").toString() != ""){
                val currentUserName = intent!!.extras!!.get("name")
                val currentUserId = mFirebaseUser!!.uid

                val message = Message(currentUserId, ed_message.text.toString().trim(),
                    currentUserName.toString().trim())

                mFirebaseDatabaseRef!!.child("messages")
                    .push().setValue(message)
                ed_message.setText("")
            }
        }
    }
}