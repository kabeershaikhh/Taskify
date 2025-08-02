package com.example.taskify

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskify.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        firebaseAuth = FirebaseAuth.getInstance()
        binding.loginButton.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString()
            val password = binding.editTextTextPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
                    if (it.isSuccessful){
                        Toast.makeText(this,"Successfully logged in",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        val errorMessage = when (val exception = it.exception) {
                            is FirebaseAuthInvalidCredentialsException -> "Invalid credentials. Please check your email and password."
                            is FirebaseAuthInvalidUserException -> "No account found with this email. Please sign up first."
                            else -> "Login failed: ${exception?.localizedMessage}"
                        }
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()

                    }
                }
            }    else{
            Toast.makeText(this,"Fields cannot be empty",Toast.LENGTH_SHORT).show()
            }
        }
        binding.signUp.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}