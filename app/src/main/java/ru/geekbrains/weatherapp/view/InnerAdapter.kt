package ru.geekbrains.weatherapp.view

import ru.geekbrains.weatherapp.R
import ru.geekbrains.weatherapp.model.City

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class InnerAdapter(
    private var cityData: List<City>,
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
        holder.bind(cityData[position])
    }

    override fun getItemCount() = cityData.size

    inner class InnerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(city: City) {
            itemView.apply {
                findViewById<TextView>(R.id.innerRecyclerItemText).text = city.name
                setOnClickListener {
                    onItemViewClickListener?.onItemViewClick(city)
                    Toast.makeText(itemView.context, city.name, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}