package com.example.ksji833


import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ksji833.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var UserInfoRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        database = FirebaseDatabase.getInstance()
        UserInfoRef = database.getReference(Common.USER_INFO_REFERENCE)

        binding.signUpButton.setOnClickListener {

            val mName = binding.nameEditText.text.toString()
            val mEmail = binding.emailEditText.text.toString()
            val mPassword = binding.passwordEditText.text.toString()
            val mRepeatPassword = binding.repeatPasswordEditText.text.toString()

            val passwordRegex = Pattern.compile("^" +
                    "(?=.*[-@#$%^&+=])" +     // Al menos 1 carácter especial
                    ".{6,}" +                // Al menos 4 caracteres
                    "$")

            if (mName.isEmpty()){
                Toast.makeText(this, "Field Required",Toast.LENGTH_SHORT).show()
            }else if (mEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                Toast.makeText(this, "Enter a Vaild Email",
                    Toast.LENGTH_SHORT).show()
            } else if (mPassword.isEmpty() || !passwordRegex.matcher(mPassword).matches()){
                Toast.makeText(this, "Weak Password.",
                    Toast.LENGTH_SHORT).show()
            } else if (mPassword != mRepeatPassword){
                Toast.makeText(this, "Confirm Password",
                    Toast.LENGTH_SHORT).show()

            }
            else {
                createAccount(mName,mEmail, mPassword)
            }

        }







        binding.backImageView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            this.startActivity(intent)
        }

    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            if(currentUser.isEmailVerified){
                val intent = Intent(this, MainActivity::class.java)
                this.startActivity(intent)
            } else {
                val intent = Intent(this, CheckEmailActivity::class.java)
                this.startActivity(intent)
            }
        }
    }

    private fun createAccount(name:String,email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserToDatabase(name,email,auth.currentUser?.uid!!)
                    val intent = Intent(this, CheckEmailActivity::class.java)
                    this.startActivity(intent)
                } else {
                    Toast.makeText(this, "Failed to create Account. Try again",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(name: String,email: String, uid:String){
        UserInfoRef = FirebaseDatabase.getInstance().getReference()
        UserInfoRef.child("User").child(uid).setValue(UserInfoModel(name,email,uid))

    }



}
