package com.example.firebasechat.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.firebasechat.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.ByteArrayOutputStream
import java.io.File

class SettingsActivity : AppCompatActivity() {

    private var mDatabase: DatabaseReference? = null
    private var mUser: FirebaseUser? = null
    private var mStorageRef: StorageReference? = null
    private val GALLERY_ID:Int = 1

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

        btn_change_pic.setOnClickListener {
            val galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_ID)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == GALLERY_ID && requestCode == Activity.RESULT_OK){
            val image: Uri = data!!.data!!
            CropImage.activity(image)
                .setAspectRatio(1, 1)
                .start(this)
        }
        if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            super.onActivityResult(requestCode, resultCode, data)
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK){
                val resultUri = result.uri

                val userId = mUser!!.uid
                val stream = ByteArrayOutputStream()
                val thumbnail = File(resultUri.path)
                val bitmap: Bitmap = BitmapFactory.decodeFile(thumbnail.toString())
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                val imageByteArray: ByteArray
                imageByteArray = stream.toByteArray()
                val filePath = mStorageRef!!.child("user_profile_images")
                    .child("$userId.jpg")
                val thumbFilePath = mStorageRef!!.child("user_profile_images")
                    .child("thumbs")
                    .child("$userId.jpg")
                filePath.putFile(resultUri).addOnCompleteListener {
                        task: Task<UploadTask.TaskSnapshot> ->
                    if (task.isSuccessful){
                        val imageUrl = filePath.downloadUrl.toString()
                        val uploadTask: UploadTask = thumbFilePath.putBytes(imageByteArray)
                        uploadTask.addOnCompleteListener{
                                task: Task<UploadTask.TaskSnapshot> ->
                            val thumbUrl = thumbFilePath.downloadUrl.toString()
                            if (task.isSuccessful){
                                val userObj = HashMap<String, Any>()
                                userObj["image"] = imageUrl
                                userObj["thumb_image"] = thumbUrl
                                mDatabase!!.updateChildren(userObj).addOnCompleteListener{
                                        task: Task<Void> ->
                                    if (task.isSuccessful){
                                        Toast.makeText(this,
                                            "Profile Picture updated Successfully",
                                            Toast.LENGTH_SHORT)
                                            .show()
                                        Log.d("PIC UPLOAD", "PICTURE UPDATE SUCCESSFULLY")
//                                        Outgoing transactions from this process must be FLAG_ONEWAY
                                    }else{
                                        Toast.makeText(this,
                                            "Unable to update profile Picture",
                                            Toast.LENGTH_SHORT)
                                            .show()
                                        Log.d("PIC UPLOAD", "PICTURE NOT UPDATE")
                                    }
                                }
                            }else{
                                Log.e("PIC UPLOAD", "UNABLE TO UPLOAD PICTURE")
                            }
                        }
                    }
                }
            }
        }
    }
}
