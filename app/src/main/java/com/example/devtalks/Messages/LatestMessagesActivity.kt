package com.example.devtalks.Messages

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Adapter
import com.example.devtalks.R
import com.example.devtalks.models.ChatMessage
import com.example.devtalks.models.User
import com.example.devtalks.registerlogin.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_messages.*
import kotlinx.android.synthetic.main.latest_message_row.view.*

class LatestMessagesActivity : AppCompatActivity() {
    companion object {
        var currentUser:User?=null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)
        fetchCurrentUser()
        //setupDummyRows()
        verifyUserIsLoggedIn()
        listenForLatestMessages()
        recyclerview_latest_messages.adapter=adapter
        recyclerview_latest_messages.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
    //Set item click listener for the adapter
        adapter.setOnItemClickListener { item, view ->
            Log.d("LatestMessages","Click listener")
            val intent=Intent(this,chatlogActivity::class.java)
//       Missing chat item member
            val row=item as LatestMessagesRow

            intent.putExtra(NewMessageActivity.USER_KEY,row.chatPartnerUser)
            startActivity(intent)
        }

    }

    val latestMessagesMap=HashMap<String,ChatMessage>()
    private fun refreshRecyclerView(){
        adapter.clear()
        latestMessagesMap.values.forEach {
            adapter.add(LatestMessagesActivity.LatestMessagesRow(it))
        }
    }
    private fun listenForLatestMessages(){
        val fromId=FirebaseAuth.getInstance().uid
        val ref=FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object :ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            val chatMessage=p0.getValue(ChatMessage::class.java)?:return
                latestMessagesMap[p0.key!!]=chatMessage
                refreshRecyclerView()
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage=p0.getValue(ChatMessage::class.java)?:return

                latestMessagesMap[p0.key!!]=chatMessage
                refreshRecyclerView()
            }


        })

    }
    val adapter=GroupAdapter<ViewHolder>()
//    private fun setupDummyRows(){
//
//        adapter.add(LatestMessagesRow())
//        adapter.add(LatestMessagesRow())
//        adapter.add(LatestMessagesRow())


    private fun fetchCurrentUser(){
    val uid=FirebaseAuth.getInstance().uid
    val ref=FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                currentUser=p0.getValue(User::class.java)
                Log.d("Latest Messages","Current User:${currentUser?.usename}")
            }
        })
    }
    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }
    class LatestMessagesRow(val chatMessage: ChatMessage): Item<ViewHolder>(){
        var chatPartnerUser:User?=null
        override fun getLayout(): Int {
            return R.layout.latest_message_row

        }

        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.latest_message_textview.text=chatMessage.text
            val chatPartnerId:String
            if (chatMessage.fromId== FirebaseAuth.getInstance().uid) {
                chatPartnerId = chatMessage.toId
            }
            else{
                chatPartnerId=chatMessage.fromId
            }

            val ref= FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    chatPartnerUser=p0.getValue(User::class.java)
                    viewHolder.itemView.username_textview.text=chatPartnerUser?.usename
                    val targetImage=viewHolder.itemView.imageview_latest_message
                    Picasso.get().load(chatPartnerUser?.profileImageUrl).into(targetImage)
                }
            })


//            viewHolder.itemView.username_textview.text=chatMessage.text
        }

    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.menu_new_message ->{
                val intent = Intent(this, NewMessageActivity::class.java)
                startActivity(intent)

            }
            R.id.menu_sign_out ->{
                val intent = Intent(this, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}


