package com.example.firebasechat.adapters

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechat.R
import com.example.firebasechat.models.Message
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class MessageAdapter(private val list: ArrayList<Message>, val context: Context):
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    private var mFirebaseUser: FirebaseUser? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageAdapter.ViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private var message: TextView = itemView.findViewById(R.id.txt_msg_text)
        private var name: TextView = itemView.findViewById(R.id.txt_msg_name)
        private var img_left: ImageView = itemView.findViewById(R.id.img_pic_msg)
        private var img_right: ImageView = itemView.findViewById(R.id.img_msg_right)
        private var mFirebaseDatabaseRef: DatabaseReference? = null

        private val currentUserId = mFirebaseUser!!.uid

        fun bindView(msg: Message) {
            message.text = msg.text
            name.text = msg.name
            mFirebaseDatabaseRef = FirebaseDatabase.getInstance().reference

            val isMe: Boolean = msg.id!! == currentUserId
            if (isMe) {
                img_right.visibility = View.VISIBLE
                img_left.visibility = View.GONE
                message.gravity = (Gravity.RIGHT or Gravity.CENTER_VERTICAL)
                name.gravity = (Gravity.RIGHT or Gravity.CENTER_VERTICAL)
                mFirebaseDatabaseRef!!.child("Users").child(currentUserId)
                    .addValueEventListener(object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var imgUrl = snapshot.child("image")
                            Picasso.get()
                                .load(imgUrl.toString())
                                .placeholder(android.R.drawable.ic_menu_report_image)
                                .error(android.R.drawable.ic_menu_report_image)
                                .into(img_right)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("ERROR IN CHAT", error.toString())
                        }
                    })
            }else{
                img_right.visibility = View.GONE
                img_left.visibility = View.VISIBLE
                message.gravity = (Gravity.LEFT or Gravity.CENTER_VERTICAL)
                name.gravity = (Gravity.LEFT or Gravity.CENTER_VERTICAL)
                mFirebaseDatabaseRef!!.child("Users").child(msg.id!!)
                    .addValueEventListener(object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val imgUrl = snapshot.child("image")
                            Picasso.get()
                                .load(imgUrl.toString())
                                .placeholder(android.R.drawable.ic_menu_report_image)
                                .error(android.R.drawable.ic_menu_report_image)
                                .into(img_left)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("ERROR IN CHAT", error.toString())
                        }
                    })
            }
        }
    }

}