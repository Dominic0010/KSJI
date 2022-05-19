package com.example.ksji833.chatUi

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.ksji833.BR
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ksji833.ChatListModel
import com.example.ksji833.MessageModel
import com.example.ksji833.Utils.AppUtils
import com.example.ksji833.databinding.ActivityMessageBinding
import com.example.ksji833.databinding.LeftItemLayoutBinding
import com.example.ksji833.databinding.RightItemLayoutBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.right_item_layout.*

class MessageActivity : AppCompatActivity() {

    private lateinit var activityMessageBinding: ActivityMessageBinding
    private var hisId: String? = null
    private var hisImage: String? = null
    private var chatId: String? = null
    private lateinit var appUtils: AppUtils
    private lateinit var myId:String
    private var firebaseRecyclerAdapter: FirebaseRecyclerAdapter<MessageModel, ViewHolder>? = null
    private lateinit var myImage:String
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMessageBinding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(activityMessageBinding.root)
        appUtils = AppUtils()
        myId = appUtils.getUID()!!
        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE)
        myImage = sharedPreferences.getString("myImage", "").toString()

        activityMessageBinding.activity = this

        if (intent.hasExtra("chatId")){
            chatId = intent.getStringExtra("chatId")
            hisId = intent.getStringExtra("hisId")
            hisImage = intent.getStringExtra("hisImage")

            readMessages(chatId!!)

        }else {

            hisId = intent.getStringExtra("hisId")
            hisImage = intent.getStringExtra("hisImage")
        }

        activityMessageBinding.hisImage = hisImage

        activityMessageBinding.btnSend.setOnClickListener {
            val message = activityMessageBinding.msgText.text.toString()
            if (message.isEmpty())
                Toast.makeText(this,"Enter Message", Toast.LENGTH_SHORT).show()
            else{
                sendMessage(message)
            }
        }


        if (chatId == null)
            checkChat(hisId!!)
    }

    private fun checkChat(hisId:String){
        val databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(myId)
        val query = databaseReference.orderByChild("member").equalTo(hisId)
        query.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for(ds in snapshot.children){
                        val member= ds.child("member").value.toString()
                        if (hisId==member){
                            chatId = ds.key
                            readMessages(chatId!!)
                            break
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun createChat(message:String){
        var databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(myId)
        chatId = databaseReference.push().key
        val chatListMode=ChatListModel(chatId!!,message,System.currentTimeMillis().toString(),hisId!!)

        databaseReference.child(chatId!!).setValue(chatListMode)

        databaseReference=FirebaseDatabase.getInstance().getReference("ChatList").child(hisId!!)

        val chatList=ChatListModel(chatId!!,message,System.currentTimeMillis().toString(),myId)

        databaseReference.child(chatId!!).setValue(chatList)

        databaseReference = FirebaseDatabase.getInstance().getReference("Chat").child(chatId!!)

        val messageModel = MessageModel(myId,hisId!!,message, type = "text")
        databaseReference.push().setValue(messageModel)
    }


    private fun sendMessage(message: String){

        if (chatId==null)
            createChat(message)
        else{
            var databaseReference= FirebaseDatabase.getInstance().getReference("Chat").child(chatId!!)
            val messageModel= MessageModel(myId,hisId!!,message, type = "text")
            databaseReference.push().setValue(messageModel)

            val map: MutableMap<String, Any> = HashMap()
            map["lastMessage"]=message
            map["date"] = System.currentTimeMillis().toString()

            databaseReference= FirebaseDatabase.getInstance().getReference("ChatList").child(myId).child(chatId!!)
            databaseReference.updateChildren(map)

            databaseReference= FirebaseDatabase.getInstance().getReference("ChatList").child(myId).child(hisId!!)
            databaseReference.updateChildren(map)

        }
    }

    private fun readMessages(chatId:String){
        val query =FirebaseDatabase.getInstance().getReference("Chat").child(chatId)
        val firebaseRecyclerOptions= FirebaseRecyclerOptions.Builder<MessageModel>()
            .setLifecycleOwner(this)
            .setQuery(query,MessageModel::class.java)
            .build()
        query.keepSynced(true)

        firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<MessageModel,ViewHolder>(firebaseRecyclerOptions){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
                var viewDataBinding: ViewDataBinding? = null

                if (viewType == 0)
                    viewDataBinding = RightItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent, false)

                if(viewType==1)
                    viewDataBinding = LeftItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent, false)

                return ViewHolder(viewDataBinding!!)
            }

            override fun onBindViewHolder(holder:ViewHolder, position: Int, messageModel: MessageModel){
                if (getItemViewType(position)==0){
                    holder.viewDataBinding.setVariable(BR.message,messageModel)
                    holder.viewDataBinding.setVariable(BR.message, myImage)
                }

                if (getItemViewType(position)==1){
                    holder.viewDataBinding.setVariable(BR.message,messageModel)
                    holder.viewDataBinding.setVariable(BR.messageImage,hisImage)
                }

            }

            override fun getItemViewType(position: Int): Int{
                val messageModel= getItem(position)
                return if (messageModel.senderId==myId)
                    0
                else
                    1

            }
        }
        activityMessageBinding.messageRecyclerView.layoutManager=LinearLayoutManager(this)
        activityMessageBinding.messageRecyclerView.adapter=firebaseRecyclerAdapter
        firebaseRecyclerAdapter!!.startListening()
    }

    class ViewHolder(var viewDataBinding: ViewDataBinding):
        RecyclerView.ViewHolder(viewDataBinding.root)

    override fun onPause() {
        super.onPause()
        if(firebaseRecyclerAdapter != null)
            firebaseRecyclerAdapter!!.stopListening()
    }

    fun userInfo(){
        val intent = Intent(this,UserInfoActivity::class.java)
        intent.putExtra("userId",hisId)
        startActivity(intent)
    }

}