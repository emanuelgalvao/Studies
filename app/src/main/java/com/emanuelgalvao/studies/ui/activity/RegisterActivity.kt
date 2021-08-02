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
import com.emanuelgalvao.studies.databinding.ActivityRegisterBinding
import com.emanuelgalvao.studies.util.AlertUtils
import com.emanuelgalvao.studies.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var mViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        listeners()
        observers()
    }

    private fun listeners() {
        binding.textRegister.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.text_register -> register()
        }
    }

    private fun register() {
        val name = binding.editName.text.toString().trim()
        val email = binding.editEmail.text.toString().trim()
        val password = binding.editPassword.text.toString().trim()

        binding.textRegister.isVisible = false
        binding.progressRegister.isVisible = true

        mViewModel.register(name, email, password)

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun observers() {
        mViewModel.register.observe(this, {
            if (it.isSucess()) {
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            } else {
                binding.textRegister.isVisible = true
                binding.progressRegister.isVisible = false
                AlertUtils.showSnackbar(binding.root, it.getMessage(), getColor(R.color.snack_red))
            }
        })
    }

}