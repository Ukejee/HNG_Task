package com.ukejee.hngtask

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.android.material.snackbar.Snackbar
import com.ukejee.hngtask.database.AppDatabase
import com.ukejee.hngtask.database.UserDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    var createAccount:TextView? = null
    var loginBtn: LinearLayout? = null
    var emailField: EditText? = null
    var passwordField: EditText? = null
    var forgotPassword: TextView? = null

    companion object{

        var db:AppDatabase? = null
    }



    lateinit var rootView: View

    val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        db = Room.databaseBuilder(applicationContext,
            AppDatabase::class.java,
            "HNG_STAGE_ONE_DB").build()

    }

    private fun initView(){

        createAccount = findViewById(R.id.create_account)

        createAccount?.isClickable = true

        rootView = findViewById(R.id.signup_layout)

        forgotPassword = findViewById(R.id.forgot_password_text)

        loginBtn = findViewById(R.id.login_btn)
        emailField = findViewById(R.id.email_input_field)
        passwordField = findViewById(R.id.password_input_field)

        loginBtn!!.isClickable = true
        loginBtn!!.isFocusable = true

        forgotPassword?.setOnClickListener {

            val intent = Intent(applicationContext, ComingSoonActivity::class.java)
            startActivity(intent)
        }

        loginBtn?.setOnClickListener {

            Log.d("TRIMMED EMAIL TEXT: ", emailField!!.text.toString().trim())

            if(emailField!!.text.toString().isNotBlank() && passwordField!!.text.toString().isNotBlank()){

                disposable.add(
                    db!!.userDao().getPasswordByEmail(emailField!!.text.toString().trim())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object: DisposableSingleObserver<String>(){
                            override fun onSuccess(t: String) {
                                if(t.equals(passwordField!!.text.toString())){

                                    val intent = Intent(applicationContext, DashboardActivity::class.java)

                                    intent.putExtra("LOGGED_IN_EMAIL", emailField!!.text.toString().trim())
                                    intent.putExtra("FROM_ACTIVITY", "LOGIN")
                                    startActivity(intent)
                                }
                                else{
                                    Snackbar.make(rootView, "Password Invalid", Snackbar.LENGTH_LONG).show()
                                }
                            }

                            override fun onError(e: Throwable) {
                                Snackbar.make(rootView, "No User with that email", Snackbar.LENGTH_LONG).show()
                            }

                        })
                )
            }
            else{
                Snackbar.make(rootView, "Please Enter Username and Password", Snackbar.LENGTH_LONG).show()
            }

        }

        createAccount?.setOnClickListener {

            val intent = Intent(this, SignUpActivity::class.java)

            startActivity(intent)

        }

    }

    override fun onBackPressed() {
        moveTaskToBack(true)
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(1)
    }
}
