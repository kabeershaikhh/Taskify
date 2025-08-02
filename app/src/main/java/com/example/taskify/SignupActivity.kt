package com.example.taskify

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskify.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        firebaseAuth = FirebaseAuth.getInstance()
        binding.signUpButton.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString()
            val password = binding.editTextTextPassword.text.toString()
            val confirmPassword = binding.editTextTextPassword1.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password== confirmPassword){
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Successfully Registered", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            val errorMessage = when (val exception = it.exception) {
                                is FirebaseAuthWeakPasswordException -> "Password should be at least 6 characters."
                                is FirebaseAuthInvalidCredentialsException -> "Invalid email format."
                                is FirebaseAuthUserCollisionException -> "An account with this email already exists."
                                else -> "Signup failed. Please try again."
                            }
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()

                        }
                    }
                } else{
                    Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this,"Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        binding.logIn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}