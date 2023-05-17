package com.example.testweather.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.testweather.R
import com.example.testweather.databinding.ItemDayWeatherBinding
import com.example.testweather.model.weather.WeatherForDay
import com.example.testweather.utils.toCelsius
import com.example.testweather.utils.toDateLongString

class WeeklyAdapter(private val context: Context) : RecyclerView.Adapter<WeeklyAdapter.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null
    private val currentListItems = mutableListOf<WeatherForDay>()



    fun setupItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

    fun updateItems(newListItems: List<WeatherForDay>){
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = currentListItems.size
            override fun getNewListSize() = newListItems.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return currentListItems[oldItemPosition] == newListItems[newItemPosition]
            }


            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                currentListItems[oldItemPosition] == newListItems[newItemPosition]
        })

        currentListItems.clear()
        currentListItems.addAll(newListItems)

        diffResult.dispatchUpdatesTo(this)
    }
    interface OnItemClickListener{
        fun clickItem(weatherForDay: WeatherForDay)
    }

    inner class ViewHolder(private val binding: ItemDayWeatherBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(weatherForDay: WeatherForDay) {
            binding.txtTemperature.text = weatherForDay.averageTempInDayCelsius.toString() + context.getString(R.string.celsius)
            binding.txtMaxDegrees.text = context.getString(R.string.max) + weatherForDay.maxTempInDayCelsius.toString() + context.getString(R.string.celsius)
            binding.txtMinDegrees.text = context.getString(R.string.min) + weatherForDay.maxTempInDayCelsius.toString() + context.getString(R.string.celsius)
            binding.txtDayOfWeek.text = weatherForDay.stringDateLong

            binding.root.setOnClickListener {
                onItemClickListener?.clickItem(weatherForDay)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDayWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentListItems[position])
    }

    override fun getItemCount(): Int = currentListItems.size
}