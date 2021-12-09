package com.example.assignment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.databinding.ItemTruckBinding
import com.example.assignment.models.TrucksModel
import com.example.assignment.utils.Utils
import java.util.concurrent.TimeUnit


class TrucksAdapter(
    var context: Context,
    var list: ArrayList<TrucksModel.Data>
) : RecyclerView.Adapter<TrucksAdapter.ViewHolder>() {


    inner class ViewHolder(var binding: ItemTruckBinding) : RecyclerView.ViewHolder(binding.root) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTruckBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TrucksAdapter.ViewHolder, position: Int) {

        val data = list[position]
        holder.binding.tvTitle.text = data.truckNumber.toString()
        val t = Utils.currentTimeStamp()
        val diff = t!!-data.lastWaypoint.updateTime
        val diffInDays: Long =diff.div(24.times(60).times(60).times(1000))
        if (diffInDays<1) {
            holder.binding.tvTime.text = "${diffInDays.toString().split("-")[1]} days ago"
        }
        val diffStop = t!!-data.lastRunningState.stopStartTime
        val diffsInDays: Long =diffStop.div(24.times(60).times(60).times(1000))
        if (diffsInDays<1) {
            if(data.lastRunningState.truckRunningState==1)
            holder.binding.tvDesc.text = "Running since ${diffsInDays.toString().split("-")[1]} days"
            else
            holder.binding.tvDesc.text = "Stopped since ${diffsInDays.toString().split("-")[1]} days"
        }
        holder.binding.tvSpeed.text = " ${data.lastWaypoint.speed.toString()} km/h"

    }


    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(list: ArrayList<TrucksModel.Data>) {
        this.list = list
        notifyDataSetChanged()
    }

}