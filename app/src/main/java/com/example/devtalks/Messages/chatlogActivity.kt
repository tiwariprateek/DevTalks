package com.example.devtalks.Messages

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.devtalks.R
import com.example.devtalks.models.ChatMessage
import com.example.devtalks.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chatlog.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class chatlogActivity : AppCompatActivity() {
    val adapter=GroupAdapter<ViewHolder>()
    var toUser:User? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatlog)
        toUser=intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title=toUser?.usename
        //setupDummyData()
        listenForMessages()
        send_button_chat_log.setOnClickListener {
            performSendMessage()}

            recyclerview_chat_log.adapter=adapter

    }
    private fun listenForMessages(){
        val fromId=FirebaseAuth.getInstance().uid
        val toId=toUser?.uid
    val ref =FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
        ref.addChildEventListener(object :ChildEventListener  {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatmessage=p0.getValue(ChatMessage::class.java)
                if (chatmessage!=null) {
                    Log.d("chatlog", chatmessage.text)
                    if (chatmessage.fromId==FirebaseAuth.getInstance().uid) {
                        val currentUser=LatestMessagesActivity.currentUser ?: return
                        adapter.add(ChatFromItem(chatmessage.text, currentUser))

                    }
                    else {
                        adapter.add(ChatToItem(chatmessage.text, toUser!!))
                    }

                }
            recyclerview_chat_log.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }
            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })
    }

    private fun performSendMessage(){
        //To actually send message using firebase
        val fromId=FirebaseAuth.getInstance().uid
        val user=intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId=user.uid
        val text=edittext_chat_log.text.toString()

        if (fromId==null)return
        val reference=FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val Toreference=FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()
        val chatMessage=ChatMessage(reference.key!!,text,fromId,toId,System.currentTimeMillis()/1000 )
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d("chatlog","Sucsess ${reference.key}")
                //To clear the text from the edit text box
                edittext_chat_log.text.clear()
                recyclerview_chat_log.scrollToPosition(adapter.itemCount-1)
            }
        //To display both the messages in the conversation
        Toreference.setValue(chatMessage)
        //For latest Messages Activity
        val latestMessageRef=FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        latestMessageRef.setValue(chatMessage)

        val latestMessageToRef=FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        latestMessageToRef.setValue(chatMessage)


    }

}
class ChatFromItem(val text: String,val user: User):Item<ViewHolder>(){
    override fun getLayout(): Int {
    return R.layout.chat_from_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
    viewHolder.itemView.textView_from_row.text=text
        val uri=user.profileImageUrl
        val targetImageView=viewHolder.itemView.imageview_chat_from_row
        Picasso.get().load(uri).into(targetImageView)


    }

}
class ChatToItem(val text:String,val user: User):Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_to_row.text=text
        val uri=user.profileImageUrl
        val targetImageView=viewHolder.itemView.imageview_chat_to_row
        Picasso.get().load(uri).into(targetImageView)



    }

}