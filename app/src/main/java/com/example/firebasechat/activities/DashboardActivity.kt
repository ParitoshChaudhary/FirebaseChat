package com.example.firebasechat.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.firebasechat.R
import com.example.firebasechat.adapters.ViewPagerAdpater
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    private var pagerAdapter: ViewPagerAdpater? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        supportActionBar!!.title = "Dashboard"
        pagerAdapter = ViewPagerAdpater(supportFragmentManager)
        view_pager.adapter = pagerAdapter
        tab_layout.setupWithViewPager(view_pager)
        tab_layout.setTabTextColors(Color.WHITE, Color.YELLOW)

        if(intent.extras != null){
            val name = intent.extras!!.get("name")
            Toast.makeText(this, "$name is logged in", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        if (item.itemId == R.id.menu_logout){
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }else if (item.itemId == R.id.menu_settings){
            startActivity(Intent(this, SettingsActivity::class.java))
            finish()
        }
        return true
    }

}