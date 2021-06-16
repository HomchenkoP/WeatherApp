package ru.geekbrains.weatherapp.view

import ru.geekbrains.weatherapp.R
import ru.geekbrains.weatherapp.model.CityCategory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainFragmentAdapter(private var onItemViewClickListener: MainFragment.OnItemViewClickListener?) :
    RecyclerView.Adapter<MainFragmentAdapter.MainViewHolder>() {

    private var cityCategories: List<CityCategory> = listOf()

    fun setCityCategories(data: List<CityCategory>) {
        cityCategories = data
        notifyDataSetChanged()
    }

    fun removeListener() {
        onItemViewClickListener = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MainViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_main_recycler_item, parent, false) as View
        )

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(cityCategories[position])
    }

    override fun getItemCount() = cityCategories.size

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(cityCategory: CityCategory) {
            itemView.findViewById<TextView>(R.id.mainFragmentRecyclerItemTextView).text =
                cityCategory.title

            (itemView.findViewById(R.id.innerRecyclerView) as RecyclerView).apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = InnerAdapter(cityCategory.items, onItemViewClickListener)

                // типа заготовка примитивного пейджинга
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        (recyclerView.layoutManager as? LinearLayoutManager)?.findLastCompletelyVisibleItemPosition().takeIf { it != -1 }?.let {
                            if (recyclerView.adapter != null) {
                                if (it >= (recyclerView.adapter as InnerAdapter).itemCount - 1) {
                                    Toast.makeText(recyclerView.context, "Показаны все загруженные данные.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                })
            }
        }
    }
}