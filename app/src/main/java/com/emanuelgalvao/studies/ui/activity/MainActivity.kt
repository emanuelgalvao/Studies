package com.emanuelgalvao.studies.ui.activity

import android.os.Bundle
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.emanuelgalvao.studies.R
import com.emanuelgalvao.studies.databinding.ActivityMainBinding
import com.emanuelgalvao.studies.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var mViewModel: MainViewModel

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        mViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mViewModel.getUser()

        val drawerLayout: DrawerLayout = binding.drawerLayout
        navView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_decks, R.id.nav_pomodoro, R.id.nav_settings, R.id.nav_comment, R.id.nav_rate
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        observers()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
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