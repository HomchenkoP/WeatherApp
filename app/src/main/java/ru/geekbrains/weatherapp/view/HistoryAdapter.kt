package ru.geekbrains.weatherapp.view

import ru.geekbrains.weatherapp.R
import ru.geekbrains.weatherapp.model.Weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.RecyclerItemViewHolder>() {

    private var historyData: List<Weather> = arrayListOf()

    fun setData(data: List<Weather>) {
        this.historyData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        return RecyclerItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_history_recycler_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(historyData[position])
    }

    override fun getItemCount(): Int {
        return historyData.size
    }

    inner class RecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(data: Weather) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemView.findViewById<TextView>(R.id.recyclerViewItem).text =
                    String.format("%s %d %s", data.city.name, data.temperature, data.condition)
                itemView.setOnClickListener {
                    Toast.makeText(itemView.context, data.city.name, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}