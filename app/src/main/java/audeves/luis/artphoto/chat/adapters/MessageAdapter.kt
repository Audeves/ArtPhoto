package audeves.luis.artphoto.chat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import audeves.luis.artphoto.R
import audeves.luis.artphoto.chat.models.Message

class MessageAdapter(private val user:String): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private var messages: List<Message> = emptyList()

    fun setData(list:List<Message>){
        messages=list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageViewHolder {
        return MessageViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_message,
            parent,
            false
        )

        )
    }

}