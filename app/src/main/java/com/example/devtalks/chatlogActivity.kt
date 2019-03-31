package com.example.devtalks

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import com.example.devtalks.Messages.NewMessageActivity
import com.example.devtalks.Messages.UserItem
import com.example.devtalks.models.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chatlog.*

class chatlogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatlog)
        val user=intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title=user.usename
    val adapter=GroupAdapter<ViewHolder>()

        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        recyclerview_chat_log.adapter=adapter
    }
}
class ChatFromItem:Item<ViewHolder>(){
    override fun getLayout(): Int {
    return R.layout.chat_from_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

    }

}
class ChatToItem:Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

    }

}