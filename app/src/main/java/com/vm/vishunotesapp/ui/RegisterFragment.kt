package com.vm.vishunotesapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vm.vishunotesapp.R
import com.vm.vishunotesapp.databinding.FragmentRegisterBinding
import com.vm.vishunotesapp.models.UserRequest
import com.vm.vishunotesapp.ui.viewmodel.AuthViewModel
import com.vm.vishunotesapp.utils.NetworkResult
import com.vm.vishunotesapp.utils.UserTokenStore
import com.vm.vishunotesapp.utils.UserValidationResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()

    @Inject lateinit var userTokenStore: UserTokenStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (userTokenStore.getToken() != null) {
            findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
        }

        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSignUpBtn()
        setupLoginBtn()
        observeUserResponseLiveData()
    }

    fun setupSignUpBtn() {
        binding.btnSignUp.setOnClickListener {
            val userRequest = UserRequest(
                binding.txtEmail.text.toString(),
                binding.txtPassword.text.toString(),
                binding.txtUsername.text.toString(),
            )
            val userValidationResult = authViewModel.validateUserRequest(userRequest)
            if (userValidationResult is UserValidationResult.Error) {
                binding.txtError.text = userValidationResult.message
                return@setOnClickListener
            }
            authViewModel.signUpUser(userRequest)
        }
    }

    fun setupLoginBtn() {
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun observeUserResponseLiveData() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner) {
            binding.txtError.text = ""
            binding.progressBar.visibility = View.GONE
            when (it) {
                is NetworkResult.Success -> {
                    userTokenStore.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                }
                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        System.out.println("****Vishesh: RegisterFragment - onCreate")
//    }
//
//    override fun onStart() {
//        super.onStart()
//        System.out.println("****Vishesh: RegisterFragment - onStart")
//    }
//
//    override fun onResume() {
//        super.onResume()
//        System.out.println("****Vishesh: RegisterFragment - onResume")
//    }
//
//    override fun onPause() {
//        super.onPause()
//        System.out.println("****Vishesh: RegisterFragment - onPause")
//    }
//
//    override fun onStop() {
//        super.onStop()
//        System.out.println("****Vishesh: RegisterFragment - onStop")
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        System.out.println("****Vishesh: RegisterFragment - onDestroy")
//    }
}