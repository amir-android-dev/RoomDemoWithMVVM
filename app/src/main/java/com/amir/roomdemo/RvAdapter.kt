package com.amir.roomdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.amir.roomdemo.databinding.ListItemBinding
import com.amir.roomdemo.db.SubscriberEntity
/*
take this subscribersList definition from the constructor and keep it as a reference variable
 */
class RvAdapter( private val clickListener:(SubscriberEntity)->Unit) :
    RecyclerView.Adapter<MViewHolder>() {
    private val subscriberEntityList= ArrayList<SubscriberEntity>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.list_item, parent, false)
        return MViewHolder(binding)
    }

    //to display data on the items
    override fun onBindViewHolder(holder: MViewHolder, position: Int) {
        holder.bind(subscriberEntityList[position],clickListener)
    }

    override fun getItemCount(): Int {

        return subscriberEntityList.size
    }
    //create a function to set the list of subscribers
    fun setList(subscriberEntity: List<SubscriberEntity>){
        //to clear the older list and assign the new updated to it
        subscriberEntityList.clear()
        subscriberEntityList.addAll(subscriberEntity)

    }
}


class MViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(subscriberEntity: SubscriberEntity,clickListener:(SubscriberEntity)->Unit) {
        binding.nameTextView.text = subscriberEntity.name
        binding.emailTextView.text = subscriberEntity.email
        binding.listItemLayout.setOnClickListener {
            clickListener(subscriberEntity)
        }
    }
}