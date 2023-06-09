package com.example.testweather.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.testweather.R
import com.example.testweather.databinding.FragmentSplashScreenBinding
import com.example.testweather.permission.LocationPermissionAsker
import com.example.testweather.utils.Constants
import com.example.testweather.utils.SharedPrefs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenFragment : Fragment() {

    private val viewModel: SplashScreenViewModel by viewModels()

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPrefs: SharedPrefs


    lateinit var locationPermissionAsker: LocationPermissionAsker

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        requestWeatherService()
    }

    private fun requestWeatherService() {
        if (sharedPrefs.locationPermissionIsGranted) {
            val cityName = getCityNameFromArguments()
            viewModel.updateWeather(cityName) { isHaveInternet ->
                checkInternet(isHaveInternet)
            }
        } else {
            locationPermissionAsker =
                LocationPermissionAsker(requireActivity() as AppCompatActivity)

            locationPermissionAsker.showSystemDialog { isGranted ->
                sharedPrefs.locationPermissionIsGranted = isGranted
                if (isGranted) {
                    viewModel.updateWeather { isHaveInternet ->
                        checkInternet(isHaveInternet)
                    }
                }
            }
        }
    }

    private fun getCityNameFromArguments():String? {
        return   arguments?.getString(Constants.SEND_WEATHER_WEATHER_FOR_DAY_KEY)
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

    private fun setupViewModel() {
        lifecycleScope.launch {
            viewModel.isLoaded.collect {
                if (it)
                    findNavController().navigate(R.id.mainFragment)

            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}