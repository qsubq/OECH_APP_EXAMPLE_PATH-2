package com.example.oech_app_new.presentation.screen.signIn

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.oech_app_new.R
import com.example.oech_app_new.databinding.FragmentSignInBinding
import com.example.oech_app_new.presentation.utils.DialogError

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private val viewModel by viewModels<SignInViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spannableTextForgotPassword =
            SpannableStringBuilder(requireContext().getString(R.string.forgot_password))
        val spanForgotPassword = object : ClickableSpan() {
            override fun onClick(widget: View) {
                findNavController().navigate(R.id.action_signInFragment_to_forgotPasswordFragment)
            }
        }

        spannableTextForgotPassword.setSpan(
            spanForgotPassword,
            0,
            requireContext().getString(R.string.forgot_password).length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
        )
        binding.tvForgotPassword.setText(spannableTextForgotPassword, TextView.BufferType.SPANNABLE)
        binding.tvForgotPassword.movementMethod = LinkMovementMethod.getInstance()

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

        viewModel.signInLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE

            if (binding.appCompatCheckBox.isChecked) {
                val masterKey = MasterKey.Builder(requireContext())
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build()




                val secureSharedPref = EncryptedSharedPreferences(
                    requireContext(),
                    "fileNam",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
                )

                secureSharedPref.edit().putString("string", binding.TIETPassword.text.toString())
                    .apply()
            }

            val bundle = Bundle()
            bundle.putString("email", binding.TIETEmail.text.toString())
            findNavController().navigate(R.id.action_signInFragment_to_homeFragment, bundle)
        }
        binding.materialButton.setOnClickListener {
            signIn(binding.TIETEmail.text.toString(), binding.TIETPassword.text.toString())
        }
    }

    private fun signIn(email: String, password: String) {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.signIn(email, password)
    }
}
