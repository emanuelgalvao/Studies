package com.emanuelgalvao.studies.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.emanuelgalvao.studies.R
import com.emanuelgalvao.studies.databinding.ActivityLoginBinding
import com.emanuelgalvao.studies.util.AlertUtils
import com.emanuelgalvao.studies.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        listeners()
        observers()
    }

    private fun listeners() {
        binding.textEnter.setOnClickListener(this)
        binding.textRegister.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.text_enter -> login()
            R.id.text_register -> startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun login() {
        val email = binding.editEmail.text.toString().trim()
        val password = binding.editPassword.text.toString().trim()

        binding.textEnter.isVisible = false
        binding.progressLogin.isVisible = true

        mViewModel.login(email, password)

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun observers() {
        mViewModel.login.observe(this, {
            if (it.isSucess()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                binding.textEnter.isVisible = true
                binding.progressLogin.isVisible = false
                AlertUtils.showSnackbar(binding.root, it.getMessage(), getColor(R.color.snack_red))
            }
        })
    }
}