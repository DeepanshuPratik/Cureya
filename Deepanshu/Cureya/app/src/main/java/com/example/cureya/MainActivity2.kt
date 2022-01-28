package com.example.cureya

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cureya.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

import android.app.Activity

import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.cureya.databinding.ActivityMain2Binding


class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding= ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val  navView:BottomNavigationView= binding.navView

        val navController= findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration= AppBarConfiguration(

            setOf(

                R.id.homeFragment,R.id.chatFragment,R.id.chatbotFragment,R.id.communityFragment ,R.id.relaxationFragment


            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)





    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu,menu)

        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.aboutUsfragment -> {
                val navController: NavController =
                    Navigation.findNavController(this, R.id.nav_host_fragment_activity_main)
                navController.navigateUp()
                navController.navigate(R.id.aboutUsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return (Navigation.findNavController(this, R.id.nav_host_fragment_activity_main).navigateUp()
                || super.onSupportNavigateUp())
    }


}