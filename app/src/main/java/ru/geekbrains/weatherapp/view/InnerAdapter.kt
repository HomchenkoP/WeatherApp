package ru.geekbrains.weatherapp.view

import ru.geekbrains.weatherapp.R
import ru.geekbrains.weatherapp.model.Weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class InnerAdapter(
    private var weatherData: List<Weather>,
    private var onItemViewClickListener: MainFragment.OnItemViewClickListener?
) :
    RecyclerView.Adapter<InnerAdapter.InnerViewHolder>() {

    fun removeListener() {
        onItemViewClickListener = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        InnerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.inner_recycler_item, parent, false) as View
        )

    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        holder.bind(weatherData[position])
    }

    override fun getItemCount() = weatherData.size

    inner class InnerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(weather: Weather) {
            itemView.apply {
                findViewById<TextView>(R.id.innerRecyclerItemText).text = weather.city.name
                setOnClickListener {
                    onItemViewClickListener?.onItemViewClick(weather)
                    Toast.makeText(itemView.context, weather.city.name, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}