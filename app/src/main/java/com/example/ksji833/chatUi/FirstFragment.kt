package com.example.ksji833.chatUi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ksji833.ChatListModel
import com.example.ksji833.R
import com.example.ksji833.UserInfoModel
import com.example.ksji833.Utils.AppUtils
import com.example.ksji833.databinding.ChatItemLayoutBinding
import com.example.ksji833.databinding.FragmentFirstBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


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

        readChat()
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
                val chatItemLayoutBinding: ChatItemLayoutBinding= DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.chat_item_layout,parent,false)

                return ViewHolder(chatItemLayoutBinding)
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int, chatListModel: ChatListModel) {
                val databaseReference = FirebaseDatabase.getInstance().getReference("User").child(chatListModel.member)
                databaseReference.addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            val userInfoModel =  snapshot.getValue(UserInfoModel::class.java)
                            val date = appUtils.getTimeAgo(chatListModel.date.toLong())

                            val chatModel = ChatListModel(chatListModel.chatId,
                                userInfoModel?.name!!, chatListModel.lastMessage, userInfoModel.avatar!!,date)

                            holder.chatItemLayoutBinding.chatModel = chatModel
                            holder.itemView.setOnClickListener {  }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }

        }
        binding.recyclerViewChat.LayoutManger = LinearLayoutManager(context)
        binding.recyclerViewChat.setHasFixedSize(false)
        binding.recyclerViewChat.adapter = firebaseRecyclerAdapter
    }


    class ViewHolder(val chatItemLayoutBinding: ChatItemLayoutBinding): RecyclerView.ViewHolder(chatItemLayoutBinding.root)

    override fun onResume() {
        super.onResume()
        firebaseRecyclerAdapter.startListening()
    }

    override fun onPause() {
        super.onPause()
        firebaseRecyclerAdapter.stopListening()
    }



}


