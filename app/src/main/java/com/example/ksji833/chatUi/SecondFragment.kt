package com.example.ksji833.chatUi

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.widget.Filter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ksji833.Permissions.AppPermission
import com.example.ksji833.R
import androidx.recyclerview.widget.RecyclerView
import com.example.ksji833.Adapter.ContactAdapter
import com.example.ksji833.Constants.AppConstants
import com.example.ksji833.UserInfoModel
import com.example.ksji833.databinding.FragmentSecondBinding
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.contact_item_layout.view.*
import kotlinx.android.synthetic.main.fragment_second.*
import java.util.Locale.filter

class SecondFragment : Fragment() {
    private lateinit var appPermission: AppPermission
    private lateinit var fragmentsecondBinding: FragmentSecondBinding
    private lateinit var mobileContacts:ArrayList<UserInfoModel>
    private lateinit var appContacts:ArrayList<UserInfoModel>
    private lateinit var firebaseAuth: FirebaseAuth
    private var contactAdapter: ContactAdapter? = null
    private lateinit var email:String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentsecondBinding = FragmentSecondBinding.inflate(inflater, container, false)
//        val adapter = GroupAdapter<ViewHolder>()
        appPermission = AppPermission()
        firebaseAuth = FirebaseAuth.getInstance()
        email = firebaseAuth.currentUser?.displayName!!





        fragmentsecondBinding.contactSearchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(contactAdapter != null)
                contactAdapter!!.filter.filter(newText)
                return false
            }
        })

        getContact()
//        getAppContacts(mobileContacts)

        return fragmentsecondBinding.root

//        return inflater.inflate(R.layout.fragment_second, container, false)
    }

//    private fun getContacts() {
////
//        val projection = arrayOf(
//            ContactsContract.Data.DISPLAY_NAME,
////            ContactsContract.CommonDataKinds.Phone.NUMBER
//
//        )
////
//        val contentResolver = requireContext().contentResolver
//        val cursor = contentResolver.query(
//            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//            projection,
//            null,
//            null,
//            null
//        )
////
//        if (cursor != null) {
//            mobileContacts = ArrayList()
//            while (cursor.moveToNext()) {
//                val name =
//                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
////                var number =
////                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
////
////                number = number.replace("\\s".toRegex(), "")
////                val num = number.elementAt(0).toString()
////                if (num == "0")
////                    number = number.replaceFirst("(?:0)+".toRegex(), "+233")
//                val userInfoModel = UserInfoModel()
//                userInfoModel.name = name
//                //userInfoModel.number = number
//                mobileContacts.add(userInfoModel)
//            }
//            cursor.close()
//            getAppContacts(mobileContacts)
//        }
//    }
////
//    private fun getAppContacts(mobileContact:ArrayList<UserInfoModel>){
//        val databaseReference = FirebaseDatabase.getInstance().getReference("User")
//        val query = databaseReference.orderByChild("email")
//        query.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()){
//                    appContacts = ArrayList()
//                    for (data in snapshot.children){
//                        val email = data.child("email").value.toString()
//                        for (mobileModel in mobileContact){
//                            if (mobileModel.email==email){
//                                val userInfoModel = data.getValue(UserInfoModel::class.java)
//                                appContacts.add(userInfoModel!!)
//                            }
//                        }
//
//                    }
//                    fragmentsecondBinding.recyclerViewContact.apply{
//                        layoutManager = LinearLayoutManager(context)
//                        setHasFixedSize(true)
//                        adapter = ContactAdapter(appContacts)
//                        adapter = contactAdapter
//                    }
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        })
//
//    }
//
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when(requestCode){
//            AppConstants.CONTACT_PERMISSION->{
//                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
//                    getContact()
//                else Toast.makeText(context,"Permission Denied",Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    private fun getContact(){
        val ref = FirebaseDatabase.getInstance().getReference("User")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                snapshot.children.forEach{
                    val user = it.getValue(UserInfoModel::class.java)

                    if (user !=null){
                        adapter.add(UserItem())
                    }
                }

                recyclerViewContact.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}
class UserItem: Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.txtContactName.text
    }

    override fun getLayout(): Int {
        return R.layout.contact_item_layout
    }

}