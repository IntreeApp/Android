package com.intree.development.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.intree.development.R
import com.intree.development.data.model.ReferContactData
import com.intree.development.databinding.ItemContactBinding
import com.intree.development.presentation.interfaces.IContacts

class ContactsAdapter(private val listener: IContacts) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = ArrayList<ReferContactData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding = ItemContactBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContactsVH(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ContactsVH) {
            holder.bind(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ContactsVH(private val itemBinding: ItemContactBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: ReferContactData) {
            val context = itemView.context
            itemBinding.tvName.text = model.name

            if (model.photoUri.isEmpty()) {
                Glide.with(context)
                    .load(R.drawable.ic_no_photo)
                    .override(200, 200)
                    .into(itemBinding.imgRoundedProfilePhoto)
            } else {
                Glide.with(context)
                    .load(model.photoUri)
                    .override(200, 200)
                    .into(itemBinding.imgRoundedProfilePhoto)
            }

            itemBinding.btnInvite.setOnClickListener {
                listener.onItemClicked(model)
            }
            listener.onItemReady()
        }
    }

    fun setData(contacts: List<ReferContactData>) {
        list.clear()
        list.addAll(contacts)
        notifyDataSetChanged()
    }


}