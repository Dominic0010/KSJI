package com.example.ksji833.chatUi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ksji833.ChatListModel
import com.example.ksji833.R
import com.example.ksji833.Utils.AppUtils
import com.example.ksji833.databinding.FragmentFirstBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase


class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding
    private lateinit var appUtils: AppUtils
    private lateinit var firebaseRecyclerAdapter: FirebaseRecyclerAdapter<ChatListModel,ViewHolder>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFirstBinding.inflate(layoutInflater, container, false)
        appUtils = AppUtils()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun readChat(){
        val query = FirebaseDatabase.getInstance().getReference("ChatList").child(appUtils.getUID()!!)
        val firebaseRecyclerOptions = FirebaseRecyclerOptions.Builder<ChatListModel>()
            .setLifecycleOwner(this)
            .setQuery(query,ChatListModel::class.java)
            .build()
        firebaseRecyclerAdapter= object : FirebaseRecyclerAdapter<ChatListModel,ViewHolder>(firebaseRecyclerOptions) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                TODO("Not yet implemented")
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int, model: ChatListModel) {
                TODO("Not yet implemented")
            }

        }
    }



}

class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

}
