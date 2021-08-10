package com.emanuelgalvao.studies.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.emanuelgalvao.studies.R
import com.emanuelgalvao.studies.databinding.ActivityMainBinding
import com.emanuelgalvao.studies.viewmodel.MainViewModel
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var mViewModel: MainViewModel

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.color_primary_dark, theme)

        setSupportActionBar(binding.appBarMain.toolbar)

        mViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mViewModel.getUser()

        drawerLayout = binding.drawerLayout
        navView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_decks, R.id.nav_pomodoro, R.id.nav_settings, R.id.nav_comment, R.id.nav_rate
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        listeners()
        observers()
    }

    override fun onResume() {
        super.onResume()

        if (intent.extras != null)
            findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_pomodoro)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun listeners() {
        navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener {
            logout()
            false
        }
    }

    private fun logout() {
        mViewModel.logout()
        startActivity(Intent(this, LoginActivity::class.java))
        finishAffinity()
    }

    private fun observers() {
        mViewModel.user.observe(this, {
            val textUserName: TextView = navView.getHeaderView(0).findViewById(R.id.text_name)
            val textEmail: TextView = navView.getHeaderView(0).findViewById(R.id.text_email)

            textUserName.text = it.name
            textEmail.text = it.email
        })
    }
}