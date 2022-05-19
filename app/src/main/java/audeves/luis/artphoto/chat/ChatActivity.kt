package audeves.luis.artphoto.chat

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import audeves.luis.artphoto.R

class ChatActivity :AppCompatActivity(){
    private var chatId=""
    private var user=""

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_chat)

        intent.getStringExtra("chatId")?.let { chatId = it }
        intent.getStringExtra("user")?.let { user = it }

        if (chatId.isNotEmpty() && user.isNotEmpty()) {
            initViews()
        }

        }

    private fun initViews(){
         val messageRV = findViewById<RecyclerView>(R.id.messagesRecyclerView)
        messageRV.adapter= messageAdapter(user)

        val messagebtn =findViewById<Button>(R.id.sendbtn)
        messagebtn.setOnClickListener{ sendMessage()}

    }
    private fun sendMessage(){
        val msgTxt =findViewById<>(R.id.messageTxt)
        msgTxt.setText("")
    }

}




