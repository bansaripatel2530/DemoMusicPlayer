package com.sa.baseproject.appview.Music.adapter

import android.app.Activity
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sa.baseproject.BR
import com.sa.baseproject.R
import com.sa.baseproject.appview.Music.model.AudioModel

class MusicListAdapter(var activity: Activity,val listener: OnClickListener): RecyclerView.Adapter<MusicListAdapter.ViewHolder>(){
    private var data: ArrayList<AudioModel>?=ArrayList<AudioModel>()
    private var onClickListener:OnClickListener?=null
    interface OnClickListener{
        fun onClick(item: AudioModel, data: ArrayList<AudioModel>, position: Int)
    }


    fun setData(data: ArrayList<AudioModel>?) {
        if (data != null) {
            this.data?.clear()
            this.data?.addAll(data)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_musiclist, parent, false)
        this.onClickListener = listener
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data!![position])
        holder.itemView.setOnClickListener {
            onClickListener!!.onClick(data!![position],data!!,position)
        }
    }

    override fun getItemCount(): Int {
        return this.data!!.size
    }


    inner class ViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(source: AudioModel) {
            binding.setVariable(BR.source, source)
            binding.executePendingBindings()
        }

    }
}