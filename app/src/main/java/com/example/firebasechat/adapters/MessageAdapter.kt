package com.example.firebasechat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechat.R
import com.example.firebasechat.models.Message
import com.google.firebase.auth.FirebaseUser

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

        private val currentUserId = mFirebaseUser!!.uid

        fun bindView(msg: Message) {
            message.text = msg.text
            name.text = msg.name

            val isMe: Boolean = msg.id!! == currentUserId
            if (isMe) {

            }else{

            }
        }
    }

}