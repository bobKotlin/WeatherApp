package com.example.testweather.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.testweather.R
import com.example.testweather.database.converters.WeatherForDayConverter
import com.example.testweather.databinding.FragmentMainBinding
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



        setupOnBackPressed()
        setupBottomNavBar()
        setupListenerFromWeek()
        setupClickListeners()
        setupViewModel()


    }

    private fun setupOnBackPressed() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finishAffinity()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun setupClickListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            val isRefreshing = binding.swipeRefreshLayout.isRefreshing

            if (isRefreshing)
                viewModel.updateWeather { isHaveInternet ->
                    checkInternet(isHaveInternet)
                }

        }


        binding.txtNameCity.setOnClickListener {
            viewModel.clickOnSelectLocation(binding.txtNameCity) { cityName ->
                binding.swipeRefreshLayout.isRefreshing = true

                viewModel.updateWeather(cityName) { isHaveInternet ->
                    checkInternet(isHaveInternet)
                }
            }
        }

    }

    private fun setupBottomNavBar() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.containerFragmentMain) as NavHostFragment

        binding.bottomNavigationView.setupWithNavController(navHostFragment.navController)

    }

    private fun setupViewModel() {
        lifecycleScope.launch {
            viewModel.location.collect {
                Log.d("TagProj", "location: $it")
                binding.txtNameCity.text = it?.nameCity

            }

        }
        lifecycleScope.launch {
            viewModel.isLoaded.collect {
                Log.d("TagProj", "location: $it")

                if (it) {
                    binding.swipeRefreshLayout.isRefreshing = false

                }
            }
        }

    }


    private fun setupListenerFromWeek() {
        val listener = object : WeeklyToDailyListener {
            override fun onClickItemWeather(weatherForDay: WeatherForDay) {
                val bundle = Bundle()
                val string = weatherForDayConverter.weatherForDayToString(weatherForDay)
                bundle.putString(Constants.SEND_WEATHER_WEATHER_FOR_DAY_KEY, string)
                findNavController().navigate(
                    R.id.dailyFragmentInMainNav,
                    bundle
                )
            }
        }
        WeeklyFragment.setupWeeklyToDailyListener(listener)
    }

    private fun checkInternet(isHaveInternet: Boolean) {
        lifecycleScope.launchWhenResumed {
            if (isHaveInternet)
                Toast.makeText(
                    requireContext(),
                    "Have some error with retrofit request",
                    Toast.LENGTH_SHORT
                ).show()
            else
                findNavController().navigate(R.id.internetErrorFragment)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}