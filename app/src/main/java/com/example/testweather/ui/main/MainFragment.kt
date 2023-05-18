package com.example.testweather.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.testweather.R
import com.example.testweather.database.converters.WeatherFor3HoursConverter
import com.example.testweather.database.converters.WeatherForDayConverter
import com.example.testweather.databinding.FragmentMainBinding
import com.example.testweather.model.weather.WeatherFor3Hours
import com.example.testweather.model.weather.WeatherForDay
import com.example.testweather.ui.weekly.WeeklyFragment
import com.example.testweather.utils.Constants
import com.example.testweather.utils.WeeklyToDailyListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val weatherForDayConverter = WeatherForDayConverter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupViewModel()
        setupBottomNavBar()
        setupListenerFromWeek()

        binding.txtNameCity.setOnClickListener {
            viewModel.clickOnSelectLocation(binding.txtNameCity)
        }

    }

    private fun setupBottomNavBar() {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.containerFragmentMain) as NavHostFragment

        binding.bottomNavigationView.setupWithNavController(navHostFragment.navController)

    }

    private fun setupViewModel() {
        lifecycleScope.launch {
            viewModel.location.collect {location->
                binding.txtNameCity.text = location?.nameCity

            }
        }
    }

    private fun setupListenerFromWeek() {
        val listener = object : WeeklyToDailyListener {
            override fun onClickItemWeather(weatherForDay: WeatherForDay) {
                val bundle = Bundle()
                val string = weatherForDayConverter.weatherForDayToString(weatherForDay)
                bundle.putString(Constants.ACTION_WEEKLY_TO_DAILY_WITH_WEATHER, string)
               findNavController().navigate(
                    R.id.action_mainFragment_to_dailyFragmentInMainNav,
                    bundle
                )
            }
        }
        WeeklyFragment.setupWeeklyToDailyListener(listener)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}