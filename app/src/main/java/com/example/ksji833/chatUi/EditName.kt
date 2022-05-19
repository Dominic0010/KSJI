package com.example.ksji833.chatUi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.ksji833.Utils.AppUtils
import com.example.ksji833.databinding.ActivityEditNameBinding

class EditName : AppCompatActivity() {

    private lateinit var editNameBinding: ActivityEditNameBinding
    private lateinit var Name: String
    private lateinit var appUtil:AppUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editNameBinding = ActivityEditNameBinding.inflate(layoutInflater)
        setContentView(editNameBinding.root)
        appUtil = AppUtils()

            if(intent.hasExtra("name")){
                val name = intent.getStringExtra("name")
                if (name!!.contains("")){
                    val split = name.split("")
                    editNameBinding.edtName.setText(split[0])
                }
            }

        editNameBinding.btnEditName.setOnClickListener {
            if (areFieldEmpty()) {
                val intent = Intent()

                intent.putExtra("name","$Name")
                setResult(100, intent)
                finish()
            }
        }
    }

    private fun areFieldEmpty():Boolean{
        Name = editNameBinding.edtName.text.toString()

        var required: Boolean =false
        var view: View? = null

        if(Name.isEmpty()){
            editNameBinding.edtName.error = "Field is required"
            required = true
            view = editNameBinding.edtName
        }

        return if (required){
            view?.requestFocus()
            false
        } else true
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