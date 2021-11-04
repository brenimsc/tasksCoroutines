package com.example.tasks.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.tasks.databinding.ActivityLoginBinding
import com.example.tasks.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        // Inicializa eventos
        setListeners();
        observe()

        verifyLoggedUser()
        mViewModel.isAuthenticationAvaliable()
    }

    private fun verifyLoggedUser() {
        mViewModel.verifyLogged()
    }

    private fun showAuthentication() {

        val executor: Executor = ContextCompat.getMainExecutor(this) // a Thread que ira cuidar disso quando o usuario usar a biometria

        val biometricPrompt: BiometricPrompt = BiometricPrompt(this@LoginActivity, executor, object: BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
        })

        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometria")
            .setSubtitle("Utilize a biometria para logar")
            .setDescription("Coloque o dedo no sensor para identificar o usuário")
            .setNegativeButtonText("Cancelar")
            .build()

        biometricPrompt.authenticate(info)


    }

    private fun setListeners() {
//        button_login.setOnClickListener(this)
//        text_register.setOnClickListener(this)
        binding.buttonLogin.setOnClickListener {
            Log.e("BRENOL", "chamei")
            handleLogin()
        }
        binding.textRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }


    private fun observe() {
        mViewModel.login.observe(this) {
            if (it.status) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Ocorreu uma falha: \n ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        mViewModel.fingerprint.observe(this) {
            if (it) {
                showAuthentication()
            }
        }
        mViewModel.loggedUser.observe(this) {
            if (it) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun handleLogin() {
        val email = edit_email.text.toString()
        val password = edit_password.text.toString()

        mViewModel.doLogin(email, password)
    }

}
