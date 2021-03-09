package com.hkshopu.hk.ui.main.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import com.google.android.material.navigation.NavigationView
import com.hkshopu.hk.Base.BaseActivity
import com.hkshopu.hk.R
import com.hkshopu.hk.databinding.ActivityShopmenuBinding
import com.hkshopu.hk.ui.main.fragment.ShopManageFragment
import com.hkshopu.hk.ui.user.activity.LoginActivity
import com.hkshopu.hk.ui.user.activity.BuildAccountActivity


class ShopmenuActivity: BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityShopmenuBinding
    lateinit var manager: FragmentManager
    lateinit var includedView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopmenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        includedView = binding.navBar.toolbar
        toggle()
        initActivity()
        initClick()
    }

    fun toggle() {
        val toggle = ActionBarDrawerToggle(
                this, binding.drawerLayout,
            includedView as Toolbar?, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    fun initActivity() {
        setSupportActionBar(includedView as Toolbar?)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        manager = supportFragmentManager
//        manager.beginTransaction().add(R.id.main, Fragment_main()).commit()
        binding.navView.setNavigationItemSelectedListener(this)
    }

    fun initClick(){
        val navigationView : NavigationView = findViewById(R.id.nav_view);
        val header = navigationView.getHeaderView(0)
        val login = header.findViewById<Button>(R.id.btn_Login_menu)
        login.setOnClickListener {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }
        val signUp = header.findViewById<Button>(R.id.btn_Signup_menu)
        signUp.setOnClickListener {

            val intent = Intent(this, BuildAccountActivity::class.java)
            startActivity(intent)

        }
    }
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            android.R.id.home -> {
                binding.drawerLayout.openDrawer(GravityCompat.START)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {

            R.id.nav_product -> {
//                manager.beginTransaction().replace(R.id.main, Fragment_import()).commit()
            }
            R.id.nav_shop -> {
//                manager.beginTransaction().replace(R.id.main, Fragment_gallery()).commit()
            }
            R.id.nav_myprofile -> {
//                manager.beginTransaction().replace(R.id.main, Fragment_slideShow()).commit()
            }
            R.id.nav_myshop -> {
//                manager.beginTransaction().replace(R.id.main, Fragment_tools()).commit()
            }
            R.id.nav_shopmanegement -> {
                manager.beginTransaction().replace(R.id.main, ShopManageFragment()).commit()
            }
//            R.id.nav_login -> {
//                val intent = Intent(this, LoginActivity::class.java)
//                startActivity(intent)
//            }
//
//            R.id.nav_signup -> {
//                val intent = Intent(this, RegisterActivity::class.java)
//                startActivity(intent)
//            }

            R.id.nav_setting -> {
//                manager.beginTransaction().replace(R.id.main, Fragment_send()).commit()
            }
            R.id.nav_about -> {
//                manager.beginTransaction().replace(R.id.main, Fragment_send()).commit()
            }
            R.id.nav_contact -> {
//                manager.beginTransaction().replace(R.id.main, Fragment_send()).commit()
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}
