package com.example.firebasechat.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebasechat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import kotlinx.android.synthetic.main.activity_settings.*
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
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK){
                val resultUri = result.uri

                val userId = mUser!!.uid
                val thumbnail = File(resultUri.path)
                val thumbBitmap = Compressor.compress(this, thumbnail){
                    quality(70)
                    resolution(200,200)
                    format(Bitmap.CompressFormat.JPEG)
                }
            }
        }

    }

}