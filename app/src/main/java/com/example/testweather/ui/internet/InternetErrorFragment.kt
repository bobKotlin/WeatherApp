package com.example.testweather.ui.internet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.testweather.R
import com.example.testweather.databinding.FragmentInternetErrorBinding
import com.example.testweather.other.InternetChecking
import com.example.testweather.permission.LocationPermissionAsker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InternetErrorFragment : Fragment() {

    private var _binding: FragmentInternetErrorBinding? = null
    private val binding get() = _binding!!


    @Inject
    lateinit var internetChecking: InternetChecking

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInternetErrorBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnOffline.setOnClickListener {
            findNavController().navigate(R.id.mainFragment)
        }

        binding.btnRefresh.setOnClickListener {
            if (internetChecking.isInternetConnected())
                findNavController().navigate(R.id.splashScreenFragment)
            else
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_turn_on_the_internet),
                    Toast.LENGTH_SHORT
                ).show()
        }
    }
}