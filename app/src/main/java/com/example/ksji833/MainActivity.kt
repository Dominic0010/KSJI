package com.example.ksji833

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.example.ksji833.Permissions.AppPermission
import com.example.ksji833.Utils.UserUtils
import com.example.ksji833.chatUi.DashBoard
import com.example.ksji833.databinding.ActivityMain3Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMain3Binding
    private lateinit var navView:NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appPermission: AppPermission
    private lateinit var navController: NavController
    private lateinit var img_avatar:ImageView
    private lateinit var waitingDialog: AlertDialog
    private lateinit var storageReference: StorageReference
    private var imageUri:Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener {
            val intent = Intent(this, DashBoard::class.java)
            this.startActivity(intent)
                //view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
        }
        drawerLayout = binding.drawerLayout
        navView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_sign_out
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



        init()
    }

     private fun init() {

         storageReference = FirebaseStorage.getInstance().getReference()

         waitingDialog = AlertDialog.Builder(this)
             .setMessage("Waiting...")
             .setCancelable(false).create()

        navView.setNavigationItemSelectedListener { it->
            if (it.itemId == R.id.nav_sign_out)
            {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("Sign Out")
                    .setMessage("Do you really want to sign Out?")
                    .setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.dismiss() }
                    .setPositiveButton("SIGN OUT"){ dialogInterface, _ ->
                        FirebaseAuth.getInstance().signOut()
                        val intent = Intent(this@MainActivity,LoginActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
                    }.setCancelable(false)

                    val dialog = builder.create()
                    dialog.setOnShowListener {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setTextColor(resources.getColor(android.R.color.holo_red_dark))
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                            .setTextColor(resources.getColor(R.color.black))
                    }

                dialog.show()
            }
            true

        }

         val headerView = navView.getHeaderView(0)
         val nameEditText = headerView.findViewById<View>(R.id.txt_name) as TextView
         val emailEditText = headerView.findViewById<View>(R.id.txt_email) as TextView
         img_avatar = headerView.findViewById<View>(R.id.img_avatar) as ImageView

//         nameEditText.setText(Common.currentUser!!.name)
//         emailEditText.setText(Common.currentUser!!.email)

         if(Common.currentUser != null && Common.currentUser!!.avatar != null && !TextUtils.isEmpty(Common.currentUser.avatar)){
             Glide.with(this)
                 .load(Common.currentUser.avatar)
                 .into(img_avatar)
         }

         img_avatar.setOnClickListener {
             val intent = Intent()
             intent.setType("image/*")
             intent.setAction(Intent.ACTION_GET_CONTENT)
             startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST)

         }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK){

            if (data != null && data.data !=null)
            {
                imageUri = data.data
                img_avatar.setImageURI(imageUri)

                showDialogupload()
            }
        }
    }

    private fun showDialogupload() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Change Profile Picture?")
            .setMessage("Do you really want to Change Profile Picture?")
            .setNegativeButton("CANCEL") { dialogInterface, _ -> dialogInterface.dismiss() }
            .setPositiveButton("CHANGE"){ dialogInterface, _ ->

                if (imageUri !=null)
                {
                    waitingDialog.show()
                    val avatarFolder = storageReference.child("avatars/"+FirebaseAuth.getInstance().currentUser!!.uid)

                    avatarFolder.putFile(imageUri!!)
                        .addOnFailureListener{e ->
                            Snackbar.make(drawerLayout,e.message!!,Snackbar.LENGTH_LONG).show()
                            waitingDialog.dismiss()
                        }.addOnCompleteListener{ task ->
                            if (task.isSuccessful)
                            {
                                avatarFolder.downloadUrl.addOnSuccessListener { uri ->
                                    val update_data =  HashMap<String,Any>()
                                    update_data.put("avatar",uri.toString())

                                    UserUtils.updateUser(drawerLayout,update_data)
                                }
                            }
                            waitingDialog.dismiss()
                        }.addOnProgressListener { taskSnapshot ->
                            val progress = (100*taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                            waitingDialog.setMessage(StringBuilder("Uploading:").append(progress).append("%"))
                            
                        }
                }

            }.setCancelable(false)

        val dialog = builder.create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(resources.getColor(android.R.color.holo_red_dark))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(resources.getColor(R.color.black))
        }

        dialog.show()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    companion object {
        val PICK_IMAGE_REQUEST = 7272
    }
}
