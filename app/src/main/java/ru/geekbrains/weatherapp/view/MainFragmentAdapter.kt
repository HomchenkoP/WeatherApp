package ru.geekbrains.weatherapp.view

import ru.geekbrains.weatherapp.R
import ru.geekbrains.weatherapp.model.WeatherCategory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainFragmentAdapter(private var onItemViewClickListener: MainFragment.OnItemViewClickListener?) :
    RecyclerView.Adapter<MainFragmentAdapter.MainViewHolder>() {

    private var weatherCategory: List<WeatherCategory> = listOf()

    fun setWeatherCategory(data: List<WeatherCategory>) {
        weatherCategory = data
        notifyDataSetChanged()
    }

    fun removeListener() {
        onItemViewClickListener = null
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_main_recycler_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(weatherCategory[position])
    }

    override fun getItemCount(): Int {
        return weatherCategory.size
    }

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(weatherCategory: WeatherCategory) {
            itemView.findViewById<TextView>(R.id.mainFragmentRecyclerItemTextView).text =
                weatherCategory.title
            val innerRecyclerView: RecyclerView = itemView.findViewById(R.id.innerRecyclerView)
            innerRecyclerView.layoutManager = LinearLayoutManager(innerRecyclerView.context, LinearLayoutManager.HORIZONTAL, false)
            innerRecyclerView.adapter = InnerAdapter(weatherCategory.items, onItemViewClickListener)
        }
    }
}