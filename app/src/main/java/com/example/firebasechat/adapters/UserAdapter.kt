package com.example.firebasechat.adapters

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechat.R
import com.example.firebasechat.activities.ChatActivity
import com.example.firebasechat.activities.ProfileActivity
import com.example.firebasechat.models.Users
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.squareup.picasso.Picasso

class UserAdapter(private val list: ArrayList<Users>, val context: Context):
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.users_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private var username: TextView = itemView.findViewById(R.id.txt_username)
        private var user_status: TextView = itemView.findViewById(R.id.txt_user_status)
        private var user_image: ImageView = itemView.findViewById(R.id.img_profile)

        fun bindView(users: Users){
            username.text = users.display_name
            user_status.text = users.user_status

            if (!TextUtils.isEmpty(users.profile_image)) {
                Picasso.get()
                    .load(users.profile_image)
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(user_image)
            }else {
                Picasso.get().load(R.mipmap.ic_launcher).into(user_image)
            }

            itemView.setOnClickListener {
                val options = arrayOf("View Profile", "Send Message")
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Select Options")
                builder.setItems(options) { _, i ->
                    val name = users.display_name
                    val status = users.user_status
                    val image = users.profile_image
                    val userId = users.user_id

                    if (i == 0) {
                        val profileIntent = Intent(context, ProfileActivity::class.java)
                        profileIntent.putExtra("user_id", userId)
                        context.startActivity(profileIntent)
                    } else {
                        val chatIntent = Intent(context, ChatActivity::class.java)
                        chatIntent.putExtra("user_id", userId)
                        chatIntent.putExtra("name", name)
                        chatIntent.putExtra("status", status)
                        chatIntent.putExtra("image", image)
                        context.startActivity(chatIntent)
                    }
                }
                builder.show()
            }
        }
    }
}

