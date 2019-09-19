package com.ukejee.hngtask

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.material.snackbar.Snackbar
import com.ukejee.hngtask.database.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers

class SignUpActivity : AppCompatActivity() {

    var signupBtn:LinearLayout? = null

    lateinit var backBtn:ImageView
    lateinit var firstNameField: EditText
    lateinit var lastNameField: EditText
    lateinit var phoneNoField: EditText
    lateinit var emailField: EditText
    lateinit var passwordField: EditText
    lateinit var rootView: View

    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signupBtn = findViewById(R.id.signup_button)
        rootView = findViewById(R.id.signup_activity_layout)

        backBtn = findViewById(R.id.back_btn)
        firstNameField = findViewById(R.id.first_name_field)
        lastNameField = findViewById(R.id.last_name_field)
        phoneNoField = findViewById(R.id.phone_no_field)
        emailField = findViewById(R.id.email_field)
        passwordField = findViewById(R.id.password_field)

        backBtn.setOnClickListener { onBackPressed() }

        signupBtn?.setOnClickListener {

            if(areAllFieldFilled()){

                if(isEmailValid(emailField.text.toString())){

                    if(isPhoneNoValid()){

                        registerUser(User(firstNameField.text.toString(),
                            lastNameField.text.toString(),
                            phoneNoField.text.toString(),
                            emailField.text.toString(),
                            passwordField.text.toString()))
                    }
                    else{
                        Snackbar.make(rootView, "Please enter a phone number of valid length", Snackbar.LENGTH_SHORT).show()
                    }

                }
                else{
                    Snackbar.make(rootView, "Please enter a valid email address", Snackbar.LENGTH_SHORT).show()
                }

            }
            else{
                Snackbar.make(rootView, "Please ensure all fields are filled", Snackbar.LENGTH_LONG).show()

            }

        }
    }

    private fun isEmailValid(email: String) = email.contains("@") && email.contains(".com")

    private fun areAllFieldFilled() = firstNameField.text.toString().isNotBlank() && lastNameField.text.toString().isNotBlank() &&
            phoneNoField.text.toString().isNotBlank() &&
            emailField.text.toString().isNotBlank() &&
            passwordField.text.toString().isNotBlank()

    private fun isPhoneNoValid() = phoneNoField.text.toString().trim().length == 11

    private fun registerUser(newUser: User){

        compositeDisposable.add(
            MainActivity.db!!.userDao().insertAll(newUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableCompletableObserver(){

                    override fun onComplete() {

                        val intent = Intent(applicationContext, DashboardActivity::class.java)
                        intent.putExtra("NEW_USER", newUser)
                        intent.putExtra("FROM_ACTIVITY", "SIGNUP")
                        startActivity(intent)
                    }

                    override fun onError(e: Throwable) {
                        Snackbar.make(rootView, e.message!!, Snackbar.LENGTH_LONG).show()
                    }
                })
        )
    }
}
