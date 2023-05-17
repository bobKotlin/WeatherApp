package com.example.testweather.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.testweather.R
import com.example.testweather.databinding.FragmentSplashScreenBinding
import com.example.testweather.permission.LocationPermissionAsker
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


    lateinit var locationPermissionAsker : LocationPermissionAsker

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
        locationPermissionAsker = LocationPermissionAsker(requireActivity() as AppCompatActivity)

        if (sharedPrefs.locationPermissionIsGranted) {
            viewModel.getLocation()
        } else {
            locationPermissionAsker.showSystemDialog { isGranted ->
                sharedPrefs.locationPermissionIsGranted = isGranted
                if (isGranted) {
                    viewModel.getLocation()
                }
            }
        }
    }

    private fun setupViewModel() {

        lifecycleScope.launch {
            viewModel.process.collect {
                if (it)
                    findNavController().navigate(R.id.action_splashScreenFragment_to_mainFragment)
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}