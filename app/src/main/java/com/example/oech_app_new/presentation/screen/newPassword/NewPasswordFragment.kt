package com.example.oech_app_new.presentation.screen.newPassword

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.oech_app_new.R
import com.example.oech_app_new.databinding.FragmentNewPasswordBinding
import com.example.oech_app_new.presentation.utils.DialogError

class NewPasswordFragment : Fragment() {
    private lateinit var binding: FragmentNewPasswordBinding
    private val viewModel by viewModels<NewPasswordViewModel>()
    private val email by lazy {
        arguments?.getString("email")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNewPasswordBinding.inflate(layoutInflater, container, false)
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

        viewModel.changePasswordLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE

            val bundle = Bundle()
            bundle.putString("email", email)
            findNavController().navigate(R.id.action_newPasswordFragment_to_homeFragment, bundle)
        }
        binding.materialButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE

            email?.let { it1 ->
                viewModel.changePassword(
                    binding.TIETPassword.text.toString(),
                    it1,
                )
            }
        }

        binding.materialButton.isEnabled = false

        binding.TIETConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.TILConfirmPassword.helperText = submitPassword()
                binding.TILPassword.helperText = submitPassword()

                if (binding.TILPassword.helperText == null) {
                    binding.materialButton.isEnabled = true
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun submitPassword(): String? {
        return if (binding.TIETPassword.text.toString() == binding.TIETConfirmPassword.text.toString()) {
            null
        } else {
            "Пароли не равны"
        }
    }
}
