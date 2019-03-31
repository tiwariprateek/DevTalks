package com.example.devtalks

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.devtalks.Messages.NewMessageActivity
import com.example.devtalks.models.ChatMessage
import com.example.devtalks.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chatlog.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class chatlogActivity : AppCompatActivity() {
    val adapter=GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatlog)
        val user=intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title=user.usename
        //setupDummyData()
        listenForMessages()
        send_button_chat_log.setOnClickListener {
            performSendMessage()

            recyclerview_chat_log.adapter=adapter
        }
    }
    private fun listenForMessages(){
    val ref =FirebaseDatabase.getInstance().getReference("/messages")
        ref.addChildEventListener(object :ChildEventListener  {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            val chatmessage=p0.getValue(ChatMessage::class.java)
                if (chatmessage!=null) {
                    Log.d("chatlog", chatmessage.text)
                    if (chatmessage.fromId==FirebaseAuth.getInstance().uid) {
                        adapter.add(ChatFromItem(chatmessage.text))
                    }
                    else {
                        adapter.add(ChatToItem(chatmessage.text))
                    }

                    }
                }
            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })
    }

    private fun performSendMessage(){
        //To actually semd message using firebase
        val fromId=FirebaseAuth.getInstance().uid
        val user=intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId=user.uid
        val text=edittext_chat_log.text.toString()
        val reference=FirebaseDatabase.getInstance().getReference("/messages").push()
        if (fromId==null)return
        val chatMessage=ChatMessage(reference.key!!,text,fromId,toId,System.currentTimeMillis()/1000 )
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d("chatlog","Sucsess ${reference.key}")
            }

    }
    private fun setupDummyData(){
        val adapter=GroupAdapter<ViewHolder>()

        adapter.add(ChatFromItem("From Message"))
        adapter.add(ChatToItem("To Message \n To Message"))
        adapter.add(ChatFromItem("From Message"))
        adapter.add(ChatToItem("To Message \n To Message"))
        adapter.add(ChatFromItem("From Message"))
        adapter.add(ChatToItem("To Message \n To Message"))
        adapter.add(ChatFromItem("From Message"))
        adapter.add(ChatToItem("To Message \n To Message"))
        adapter.add(ChatFromItem("From Message"))
        adapter.add(ChatToItem("To Message \n To Message"))
        recyclerview_chat_log.adapter=adapter

    }
}
class ChatFromItem(val text: String):Item<ViewHolder>(){
    override fun getLayout(): Int {
    return R.layout.chat_from_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
    viewHolder.itemView.textView_from_row.text=text
    }

}
class ChatToItem(val text:String):Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_to_row.text=text
    }

}