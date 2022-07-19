package com.example.ksji833.chatUi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import com.example.ksji833.R
import com.example.ksji833.Utils.AppUtils
import kotlinx.android.synthetic.main.activity_dash_board.*

class DashBoard : AppCompatActivity() {

    private val chatFragment = FirstFragment()
    private val contactsFragment = SecondFragment()
    private val notificationFragment = ThirdFragment()
    private lateinit var appUtil: AppUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        appUtil = AppUtils()

//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
//        val navController = findNavController(R.id.body_container)
//        val appBarConfiguration = AppBarConfiguration(setOf(R.id.firstFragment, R.id.secondFragment, R.id.thirdFragment))
//        setupActionBarWithNavController(navController,appBarConfiguration)
//
//        bottomNavigationView.setupWithNavController(navController)

//        if (savedInstanceState==null){
//           supportFragmentManager.beginTransaction()
//               .replace(R.id.fragment_container, FirstFragment()).commit()
//            bottom_navigation.setOnItemSelectedListener(R.id.firstFragment)
//        }
        replaceFragment(chatFragment)


        bottom_navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.btnChat -> replaceFragment(chatFragment)


                R.id.btnContacts -> replaceFragment(contactsFragment)


                R.id.btnNotification -> replaceFragment(notificationFragment)

            }
            true

//            fragment!!.let {
//                supportFragmentManager.beginTransaction().replace(R.id.fragment_container ,fragment!!)
//                    .commit()
//            }


        }


    }
    private fun replaceFragment(fragment: Fragment){
        if (fragment !=null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.body_container, fragment)
            transaction.commit()
        }
    }

    override fun onPause() {
        super.onPause()
        appUtil.updateOnlineStatus("offline")
    }

    override fun onResume() {
        super.onResume()
        appUtil.updateOnlineStatus("online")
    }
}
