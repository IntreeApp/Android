package com.intree.development.presentation.home.room

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.intree.development.R

class TagAdapter(val tagList: ArrayList<String>) : RecyclerView.Adapter<TagAdapter.ViewHolder>() {

    var mRecyclerView: RecyclerView? = null

    // holder class to hold reference
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //get view reference
        var etTag: TextView = view.findViewById(R.id.etTagItem) as TextInputEditText
        var tIndex: TextView = view.findViewById(R.id.tvTagNumber) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create view holder to hold reference
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_room_tag,
                parent,
                false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //set values
        holder.etTag.text = tagList[position]
        holder.tIndex.text = (position + 1).toString()

        holder.etTag.doOnTextChanged { text, start, before, count ->
            tagList[position] = text.toString()
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    override fun getItemCount(): Int {
        return tagList.size
    }

    fun addTag() {
        tagList.add("#")
        Log.d("====TAG_LIST===", "SIZE: ${tagList.size}")
        notifyItemInserted(tagList.size - 1)
    }

    //TODO test
    fun removeLastTag() {
        if (tagList.size > 3) {
            tagList.removeAt(tagList.size - 1)
            notifyDataSetChanged() //TODO optimize
            //notifyItemInserted(tagList.size - 2) //! TODO replace with non-error adapter notif.
        }
    }

//    private fun updateData(scanResult: ArrayList<String>) {
//        tagList.clear()
//        //notifyDataSetChanged()
//        tagList.addAll(scanResult)
//        notifyItemInserted(tagList.size - 1)
//    }
}