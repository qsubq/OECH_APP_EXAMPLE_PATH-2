package com.example.oech_app_new.presentation.screen.signUp

import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.oech_app_new.R
import com.example.oech_app_new.databinding.FragmentSignUpBinding
import com.example.oech_app_new.presentation.utils.DialogError

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private val viewModel by viewModels<SignUpViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.materialButton.isEnabled = false

        binding.TIETEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.TIETEmail.text.isNullOrEmpty()) {
                    binding.TILEmail.helperText = null
                    binding.materialButton.isEnabled = false
                } else {
                    binding.TILEmail.helperText = submitEmail()

                    if (binding.TILEmail.helperText.isNullOrEmpty() && binding.appCompatCheckBox.isChecked) {
                        binding.materialButton.isEnabled = true
                    }
                }
            }
        })

        binding.materialButton.setOnClickListener {
            signUp()
        }

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

        viewModel.signUpLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE
            this.findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        val spannableTextSignin =
            SpannableStringBuilder(requireContext().getString(R.string.sign_in))
        val spanSignIn = object : ClickableSpan() {
            override fun onClick(widget: View) {
                findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
            }
        }
        spannableTextSignin.setSpan(
            spanSignIn,
            0,
            requireContext().getString(R.string.sign_in).length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
        )
        binding.tvSignIn.setText(spannableTextSignin, TextView.BufferType.SPANNABLE)
        binding.tvSignIn.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun signUp() {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.signUp(
            binding.TIETEmail.text.toString(),
            binding.TIETPassword.text.toString(),
            binding.TIETPhone.text.toString().toInt(),
            binding.TIETName.text.toString(),
        )
    }

    private fun submitEmail(): String? {
        return if (Patterns.EMAIL_ADDRESS.matcher(binding.TIETEmail.text.toString())
                .matches() || binding.TIETEmail.text!!.contains("A-Z".toRegex())
        ) {
            null
        } else {
            "Invalid email"
        }
    }
}
