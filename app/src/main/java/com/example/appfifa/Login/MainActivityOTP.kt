package com.example.appfifa.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.appfifa.MainActivity
import com.example.appfifa.R
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit
import kotlin.math.sign

class MainActivityOTP : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var xacnhan : Button
    private lateinit var guilaiOTP: TextView
    private lateinit var inputOP1 : EditText
    private lateinit var inputOP2 : EditText
    private lateinit var inputOP3 : EditText
    private lateinit var inputOP4 : EditText
    private lateinit var inputOP5 : EditText
    private lateinit var inputOP6 : EditText

     private lateinit var OTP :String
     private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
     private lateinit var phoneNumber: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_otp)

        OTP = intent.getStringExtra("OTP").toString()
        resendToken = intent.getParcelableExtra("resendToken")!!
        phoneNumber = intent.getStringExtra("PhoneNuber" )!!

        intit()
        addTextChangeListener()
        resendOTPvVisibility()
        guilaiOTP.setOnClickListener(){
            resendVerificationCode()
            resendOTPvVisibility()
        }
        xacnhan.setOnClickListener(){
            val typeOTP = inputOP1.text.toString() + inputOP2.text.toString() +
                    inputOP3.text.toString() + inputOP4.text.toString() +
                    inputOP5.text.toString() + inputOP6.text.toString()
            if(typeOTP.isEmpty()){
                if (typeOTP.length == 6){
                    val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                        OTP, typeOTP
                    )
                    signInWithPhoneAuthCredential(credential)
                }else{
                    Toast.makeText(this,"Please Enter Corect OTP",Toast.LENGTH_SHORT).show()

                }

            }else{
                Toast.makeText(this,"Please Enter OTP",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun resendVerificationCode(){
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .setForceResendingToken(resendToken)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this,"Authenticate Successfully",Toast.LENGTH_SHORT).show()
                    senToMain()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d("TAG","signInWithPhoneAuthCredential: ${task.exception.toString()}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid

                    }
                    // Update UI
                }
            }
    }
    private fun senToMain(){
        startActivity(Intent(this,MainActivity::class.java))
    }
    private fun addTextChangeListener(){
        inputOP1.addTextChangedListener(EditTextWatcher(inputOP1))
        inputOP2.addTextChangedListener(EditTextWatcher(inputOP2))
        inputOP3.addTextChangedListener(EditTextWatcher(inputOP3))
        inputOP4.addTextChangedListener(EditTextWatcher(inputOP4))
        inputOP5.addTextChangedListener(EditTextWatcher(inputOP5))
        inputOP6.addTextChangedListener(EditTextWatcher(inputOP6))

    }
    private fun intit() {
        auth = FirebaseAuth.getInstance()
        xacnhan = findViewById(R.id.xacnhan)
        guilaiOTP = findViewById(R.id.guima)
        inputOP1 = findViewById(R.id.OTP1)
        inputOP2 = findViewById(R.id.OTP2)
        inputOP3 = findViewById(R.id.OTP3)
        inputOP4 = findViewById(R.id.OTP4)
        inputOP5 = findViewById(R.id.OTP5)
        inputOP6 = findViewById(R.id.OTP6)
    }
    private fun resendOTPvVisibility(){
        inputOP1.setText("")
        inputOP2.setText("")
        inputOP3.setText("")
        inputOP4.setText("")
        inputOP5.setText("")
        inputOP6.setText("")
        guilaiOTP.visibility = View.INVISIBLE
        guilaiOTP.isEnabled = false
        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            guilaiOTP.visibility = View.VISIBLE
            guilaiOTP.isEnabled = true
        },60000)

    }
    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.d("TAG","onVerificationFailed: ${e.toString()}")
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.d("TAG","onVerificationFailed: ${e.toString()}")
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            // Save verification ID and resending token so we can use them later
            OTP = verificationId
            resendToken = token
        }
    }
    inner class EditTextWatcher(private val view: View ):TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(p0: Editable?) {
            val  text = p0.toString()
            when(view.id){
                R.id.OTP1 -> if(text.length == 1 ) inputOP2.requestFocus()
                R.id.OTP2 -> if(text.length == 1 ) inputOP3.requestFocus() else if (text.isEmpty()) inputOP1.requestFocus()
                R.id.OTP3 -> if(text.length == 1 ) inputOP4.requestFocus() else if (text.isEmpty()) inputOP2.requestFocus()
                R.id.OTP4 -> if(text.length == 1 ) inputOP5.requestFocus() else if (text.isEmpty()) inputOP3.requestFocus()
                R.id.OTP5 -> if(text.length == 1 ) inputOP6.requestFocus() else if (text.isEmpty()) inputOP4.requestFocus()
                R.id.OTP6 -> if(text.isEmpty()) inputOP5.requestFocus()
            }
        }
    }
}