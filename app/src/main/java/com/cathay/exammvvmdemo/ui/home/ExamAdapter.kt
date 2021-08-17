package com.cathay.exammvvmdemo.ui.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.cathay.exammvvmdemo.R
import com.cathay.exammvvmdemo.data.entity.ExamEntity
import com.cathay.exammvvmdemo.databinding.ItemExamBinding
import com.cathay.exammvvmdemo.utils.DataUtil.sortOutData

class ExamAdapter(var mutableList: MutableList<ExamEntity>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onItemClickListener:((Int, View, Any)->Unit)? = null
    private var showAns = false
    fun setOnItemClickListener(onItemClickListener:(Int, View, Any)->Unit) {
        this.onItemClickListener =onItemClickListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exam,parent,false)
        val binding = DataBindingUtil.bind<ViewDataBinding>(view)
        return ViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder
        holder.bind(mutableList[position])
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position,it,mutableList[position])
        }
    }

    fun setData(list:MutableList<ExamEntity>,isShowAns:Boolean = false){
        this.mutableList = list
        this.showAns = isShowAns
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: ViewDataBinding):RecyclerView.ViewHolder(binding.root){
        private val itemBinding = binding as ItemExamBinding

        @SuppressLint("SetTextI18n")
        fun bind(bean:ExamEntity){
            itemBinding.itemNum.text = "${(absoluteAdapterPosition+1)}."
            if (sortOutData(bean.ans) == sortOutData(bean.userAns)){
                itemBinding.answer.text = ""
                itemBinding.answer.setTextColor(ContextCompat.getColor(itemBinding.root.context, R.color.black))
            }else{
                if (showAns)itemBinding.answer.text = sortOutData(bean.ans)
                itemBinding.answer.setTextColor(ContextCompat.getColor(itemBinding.root.context, R.color.purple_200))
            }
            itemBinding.userAnswer.text = sortOutData(bean.userAns)
            itemBinding.topic.text = bean.topic
        }
    }

    override fun getItemCount(): Int {
       return mutableList.size
    }
}