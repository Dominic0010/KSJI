package com.example.ksji833

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.ksji833.PublicUI.DashboardP
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*
import java.util.concurrent.TimeUnit

class SplashScreenActivity : AppCompatActivity() {

    lateinit var handler: Handler

    companion object {
        private val LOGIN_REQUEST_CODE = 7171
    }
//
    private lateinit var providers: List<AuthUI.IdpConfig>
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var listener: FirebaseAuth.AuthStateListener


//
//    override fun onStart() {
//        super.onStart()
//        delaySplashScreen();
//    }

    override fun onStop() {
        if (firebaseAuth != null && listener != null) firebaseAuth.removeAuthStateListener(listener)
        super.onStop()
    }

//    private fun delaySplashScreen() {
//        Completable.timer(3, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
//            .subscribe({
//                firebaseAuth.addAuthStateListener(listener);
//            })
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen2)

//        startActivity(Intent(this, MainActivity::class.java))
//        finish()

        handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, DashboardP::class.java)
            startActivity(intent)
            finish()
        },3000)


        init()


    }

    private fun init() {
//        providers = Arrays.asList(
////            AuthUI.IdpConfig.PhoneBuilder().build(),
//            AuthUI.IdpConfig.GoogleBuilder().build()
//        )
//
        firebaseAuth = FirebaseAuth.getInstance()
        listener = FirebaseAuth.AuthStateListener { myFirebaseAuth ->
            val user = myFirebaseAuth.currentUser
            if (user != null)
                Toast.makeText(
                    this@SplashScreenActivity,
                    "Welcome: " + user.uid,
                    Toast.LENGTH_SHORT
                ).show()
//            else
//                showLoginLayout()
//        }
        }
    }

//    private fun showLoginLayout() {
//        val authMethodPickerLayout = AuthMethodPickerLayout.Builder(R.layout.activity_login)
//            .setGoogleButtonId(R.id.btnGoogle)
//            .build();
//
//        startActivityForResult(
//            AuthUI.getInstance()
//                .createSignInIntentBuilder()
//                .setAuthMethodPickerLayout(authMethodPickerLayout)
//                .setAvailableProviders(providers)
//                .setIsSmartLockEnabled(false)
//                .build()
//            , LOGIN_REQUEST_CODE)
//    }
//        startActivity(Intent(this, MainActivity::class.java))
//        finish()
//    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == LOGIN_REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val UserInfoModel = FirebaseAuth.getInstance().currentUser
            }
            else
                Toast.makeText(this@SplashScreenActivity,""+response!!.error!!.message,Toast.LENGTH_SHORT).show()
        }
    }
}
