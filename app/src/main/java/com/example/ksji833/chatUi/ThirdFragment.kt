package com.example.ksji833.chatUi

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.ksji833.Common
import com.example.ksji833.Constants.AppConstants
import com.example.ksji833.Permissions.AppPermission
import com.example.ksji833.R
import com.example.ksji833.UserInfoModel
import com.example.ksji833.ViewModels.ProfileViewModel
import com.example.ksji833.databinding.FragmentThirdBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.dialog_layout.view.*
import kotlinx.android.synthetic.main.fragment_third.*


class ThirdFragment : Fragment() {

    private lateinit var thirdBinding: FragmentThirdBinding
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var dialog: AlertDialog
    private lateinit var userModel:UserInfoModel
    private lateinit var appPermission: AppPermission
    private lateinit var storageReference: StorageReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thirdBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_third,container,false)

        appPermission = AppPermission()
        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences =   requireContext().getSharedPreferences("userData", Context.MODE_PRIVATE)
        userModel = UserInfoModel()

        profileViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(ProfileViewModel::class.java)

        profileViewModel.getUser().observe(viewLifecycleOwner, Observer { userModel ->
            thirdBinding.userModel = userModel

            if (userModel.name!!.contains("")){
                val split = userModel.name!!.split("")

                thirdBinding.txtProfileFName.text = split[0]

            }

            img_avatar

            thirdBinding.cardName.setOnClickListener {
                val intent = Intent(context, EditName::class.java)
                intent.putExtra("name",userModel.name)
                startActivityForResult(intent, 100)
            }
        })

        thirdBinding.imgPickImage.setOnClickListener {
            if (appPermission.isStorageOk(requireContext())){
//                uploadImage()
            }
        }

        thirdBinding.imgEditStatus.setOnClickListener {
            getStatusDialog()
        }



        return thirdBinding.root
    }


    private fun getStatusDialog(){
        val alertDialog = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null, false)
        alertDialog.setView(view)

        view.btnEditStatus.setOnClickListener {
            val status = view.edtUserStatus.text.toString()
            if(status.isEmpty()){
                profileViewModel.updateStatus(status)
                dialog.dismiss()
            }
        }
        dialog = alertDialog.create()
        dialog.show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            100 -> {
                val userName = data?.getStringExtra("name")

                profileViewModel. updateName(userName)
                val editor = sharedPreferences.edit()
                editor.putString("myName", userName).apply()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            AppConstants.STORAGE_PERMISSION->{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                else Toast.makeText(context,"Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImage(imageUri: Uri){
        storageReference = FirebaseStorage.getInstance().reference
        storageReference.child(userModel.uid!!).putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                val task = taskSnapshot.storage.downloadUrl
                task.addOnCompleteListener {
                    if (it.isSuccessful){
                        val imagePath = it.result.toString()

                        val editor = sharedPreferences.edit()
                        editor.putString("myImage", imagePath).apply()

                        profileViewModel.updateImage(imagePath)
                    }
                }
            }
    }


}