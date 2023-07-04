package com.example.oech_app_new.presentation.screen.otpVerification

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.oech_app_new.R
import com.example.oech_app_new.databinding.FragmentOtpVerificationBinding
import com.example.oech_app_new.presentation.utils.DialogError

class OtpVerificationFragment : Fragment() {
    private lateinit var binding: FragmentOtpVerificationBinding
    private val viewModel by viewModels<OtpVerificationViewModel>()

    private val email by lazy {
        arguments?.getString("email")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentOtpVerificationBinding.inflate(layoutInflater, container, false)
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
            DialogError("Out of connection", "Check your network").show(
                requireActivity().supportFragmentManager,
                "DialogError",
            )
        }

        viewModel.verificationLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE

            val bundle = Bundle()
            bundle.putString("email", email)
            findNavController().navigate(
                R.id.action_otpVerificationFragment_to_newPasswordFragment,
                bundle,
            )
        }

        viewModel.resendOtpLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE

            Toast.makeText(requireContext(), "Успешно отправлено", Toast.LENGTH_SHORT).show()
        }

        binding.btnSetNewPassword.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE

            email?.let { it1 -> viewModel.verificationOtp(it1, binding.TIETCode.text.toString()) }
        }

        val spannableTextResend =
            SpannableStringBuilder(requireContext().getString(R.string.resend))
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                binding.progressBar.visibility = View.VISIBLE
                email?.let { viewModel.resendOtp(it) }
            }
        }
        spannableTextResend.setSpan(
            clickableSpan,
            0,
            requireContext().getString(R.string.resend).length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
        )

        binding.tvSignIn.setText(spannableTextResend, TextView.BufferType.SPANNABLE)
        binding.tvSignIn.movementMethod = LinkMovementMethod.getInstance()
    }
}
