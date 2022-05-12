package com.example.ksji833.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.ksji833.UserInfoModel
import com.example.ksji833.chatUi.MessageActivity
import com.example.ksji833.chatUi.UserInfoActivity
import com.example.ksji833.databinding.ContactItemLayoutBinding
import com.firebase.ui.auth.data.model.User
import java.util.*
import java.util.logging.LogRecord
import kotlin.collections.ArrayList

class ContactAdapter (private var appContacts:ArrayList<UserInfoModel>):
    RecyclerView.Adapter<ContactAdapter.ViewHolder>(), Filterable {

    private var allContact:ArrayList<UserInfoModel> = appContacts


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val contactItemLayoutBinding = ContactItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(contactItemLayoutBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val UserInfoModel = allContact[position]
        holder.contactItemLayoutBinding.userInfoModel = UserInfoModel

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context,UserInfoActivity::class.java)
            intent.putExtra("userId", UserInfoModel.uid)
            it.context.startActivity(intent)
        }

        holder.contactItemLayoutBinding.imgContact.setOnClickListener {
            val intent = Intent(it.context, UserInfoActivity::class.java)
            intent.putExtra("userId", UserInfoModel.uid)
            it.context.startActivity(intent)
        }

        holder.itemView.setOnClickListener {
            val intent= Intent(it.context, MessageActivity::class.java)
            intent.putExtra("hisId",UserInfoModel.uid)
            intent.putExtra("hisImage",UserInfoModel.avatar)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return allContact.size
    }

    class ViewHolder(val contactItemLayoutBinding: ContactItemLayoutBinding) : RecyclerView.ViewHolder(contactItemLayoutBinding.root) {

    }

    override fun getFilter(): Filter {
        return object:Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchContent = constraint.toString()
                if (searchContent.isEmpty())
                    allContact = appContacts
                else{

                    val filterContact = ArrayList<UserInfoModel>()
                    for (UserInfoModel in appContacts) {
                        if (UserInfoModel.name!!.toLowerCase(Locale.ROOT).trim().contains(searchContent.toLowerCase(
                                Locale.ROOT
                            ).trim()
                            )
                        )
                        filterContact.add(UserInfoModel)
                    }
                    allContact = filterContact
                }

                val filterResults = FilterResults()
                filterResults.values = allContact
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                allContact = results?.values as ArrayList<UserInfoModel>
                notifyDataSetChanged()
            }
        }

    }


}