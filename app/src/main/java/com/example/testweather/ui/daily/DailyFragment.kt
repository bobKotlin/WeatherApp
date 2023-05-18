package com.example.testweather.ui.daily

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.testweather.R
import com.example.testweather.database.converters.WeatherForDayConverter
import com.example.testweather.databinding.FragmentDailyBinding
import com.example.testweather.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DailyFragment : Fragment() {

    private val viewModel: DailyViewModel by viewModels()

    private var _binding: FragmentDailyBinding? = null
    private val binding
        get() = _binding!!


    private val weatherForDayConverter = WeatherForDayConverter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDailyBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        getArgument()
    }

    @SuppressLint("SetTextI18n")
    private fun setupViewModel() {
        lifecycleScope.launch {
            viewModel.weatherOneDay.collect {
                if (it == null)
                    return@collect

                if (_binding == null)
                    return@collect

                binding.txtTemperature.text =
                    it.averageTempInDayCelsius.toString() + requireContext().getString(R.string.celsius)
                binding.txtWind.text = it.averageWindInDay.toString() + " m/s"
                binding.txtRain.text = it.maxProbabilityRainInDay.toString() + " %"
                binding.txtHumidity.text = it.averageHumidityInDay.toString() + " %"
                binding.txtDayOfWeek.text = it.stringDateLong
                binding.txtSun.text = it.descriptionSun


                binding.chartTemperatureByHour.init(it)
            }
        }
    }


    private fun getArgument() {
        val jsonStringWeatherByDay =
            arguments?.getString(Constants.SEND_WEATHER_WEATHER_FOR_DAY_KEY)

        if (jsonStringWeatherByDay != null) {
            val weatherByDay = weatherForDayConverter.stringToWeatherForDay(jsonStringWeatherByDay)
            viewModel.getWeatherByDay(weatherByDay)
        } else {
            viewModel.getWeatherTodayDay()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}