package com.example.oech_app_new.presentation.screen.forgotPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.oech_app_new.R
import com.example.oech_app_new.databinding.FragmentForgotPasswordBinding
import com.example.oech_app_new.presentation.utils.DialogError

class ForgotPasswordFragment : Fragment() {
    private lateinit var binding: FragmentForgotPasswordBinding
    private val viewModel by viewModels<ForgotPasswordViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentForgotPasswordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE

            DialogError("Some server error", it).show(
                requireActivity().supportFragmentManager,
                "DialogError",
            )
        }

        viewModel.networkLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE
            DialogError("Out of connections", "Check your network").show(
                requireActivity().supportFragmentManager,
                "DialogError",
            )
        }

        viewModel.sendOtpLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE

            val bundle = Bundle()
            bundle.putString("email", binding.TIETEmail.text.toString())
            findNavController().navigate(R.id.action_forgotPasswordFragment_to_otpVerificationFragment, bundle)
        }
        binding.materialButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            viewModel.sendOtp(binding.TIETEmail.text.toString())
        }
    }
}
