package com.intree.development.presentation.adapter

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.intree.development.R
import com.intree.development.presentation.home.create_group.vm.CreateGroupViewModel
import com.makeramen.roundedimageview.RoundedImageView
import java.lang.ref.WeakReference


class CreateGroupAdapter(
    private val contactList: MutableList<CreateGroupViewModel>,
    val onCheckChanged: (selected: Int, total: Int) -> Unit
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var checkedItemsCount = 0
    private val checkedStateArray = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.list_item_create_group_person, parent, false)
        return CreateGroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CreateGroupViewHolder -> {
                val createGroupViewModel = contactList.get(position)
                holder.avatarView?.setImageResource(contactList[position].avatar)
                holder.nameView?.text = contactList[position].name
                //in some cases, it will prevent unwanted situations
                holder.checkBox?.setOnCheckedChangeListener(null)

                holder.checkBox?.isChecked = checkedStateArray.get(position)

                // This counter doesn't count right
                holder.checkBox?.setOnCheckedChangeListener{ _, isChecked ->
                    if (isChecked) {

                        checkedItemsCount += 1
                    } else {
                        checkedItemsCount -= 1
                    }
                    onCheckChanged(checkedItemsCount, contactList.size)
                }


            }
        }
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    fun reload(list: MutableList<CreateGroupViewModel>) {
        this.contactList.clear()
        this.contactList.addAll(list)
        notifyDataSetChanged()
    }

    inner class CreateGroupViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private var view: WeakReference<View> = WeakReference(itemView)
        internal var avatarView: RoundedImageView? = null
        internal var nameView: TextView? = null
        internal var checkBox: CheckBox? = null


        init {
            findView()
            itemView.setOnClickListener {
                checkBox = itemView.findViewById(R.id.createGroupListItemCheckBox) as CheckBox
                if(!checkBox!!.isChecked) {
                    checkBox!!.isChecked = true
                    checkedItemsCount += 1
                } else {
                    checkBox!!.isChecked = false
                    checkedItemsCount -= 1
                }
                onCheckChanged(checkedItemsCount, contactList.size)

                val position = adapterPosition
                checkedStateArray.put(position, true)

            }
        }

        private fun findView() {
            avatarView = view.get()?.findViewById(R.id.createGroupListItemAvatar)
            nameView = view.get()?.findViewById(R.id.createGroupListItemName)
        }
    }
}



