package com.example.testweather.ui.weekly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testweather.adapters.WeeklyAdapter
import com.example.testweather.databinding.FragmentWeeklyBinding
import com.example.testweather.model.weather.WeatherFor3Hours
import com.example.testweather.model.weather.WeatherForDay
import com.example.testweather.utils.WeeklyToDailyListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeeklyFragment : Fragment() {

    private val viewModel: WeeklyViewModel by viewModels()

    private var _binding: FragmentWeeklyBinding? = null
    private val binding get() = _binding!!

    lateinit var adapter: WeeklyAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeeklyBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupViewModel()
    }

    private fun setupViewModel() {
        lifecycleScope.launch {
            viewModel.weatherForDay.collect {
                    adapter.updateItems(it)
            }
        }
    }

    private fun setupAdapter() {
        adapter = WeeklyAdapter(requireContext())
        adapter.setupItemClickListener(object : WeeklyAdapter.OnItemClickListener {
            override fun clickItem(weatherForDay: WeatherForDay) {
                weeklyToDailyListener.onClickItemWeather(weatherForDay)
            }

        })

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        lateinit var weeklyToDailyListener :WeeklyToDailyListener

        fun setupWeeklyToDailyListener(listener: WeeklyToDailyListener){
            weeklyToDailyListener = listener
        }
    }
}