package com.ukejee.hngtask

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import com.ukejee.hngtask.database.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.*

class DashboardActivity : AppCompatActivity() {

    lateinit var userName: TextView
    lateinit var userEmail:TextView
    lateinit var userPhone: TextView
    var logoutBtn: Button? = null

    lateinit var menuItemOne: ConstraintLayout
    lateinit var menuItemTwo: ConstraintLayout
    lateinit var menuItemThree: ConstraintLayout
    lateinit var menuItemFour: ConstraintLayout
    lateinit var menuItemFive: ConstraintLayout
    lateinit var menuItemSix: ConstraintLayout

    lateinit var user: User

    var loggedInEmail: String? = null

    val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        userName = findViewById(R.id.profile_name)
        userEmail = findViewById(R.id.profile_email)
        userPhone = findViewById(R.id.profile_phoneNo)
        logoutBtn = findViewById(R.id.logout_button)

        menuItemOne = findViewById(R.id.menu_one)
        menuItemTwo = findViewById(R.id.menu_two)
        menuItemThree = findViewById(R.id.menu_three)
        menuItemFour = findViewById(R.id.menu_four)
        menuItemFive = findViewById(R.id.menu_five)
        menuItemSix = findViewById(R.id.menu_six)

        setUpMenuButtons(menuItemOne)
        setUpMenuButtons(menuItemTwo)
        setUpMenuButtons(menuItemThree)
        setUpMenuButtons(menuItemFour)
        setUpMenuButtons(menuItemFive)
        setUpMenuButtons(menuItemSix)

        logoutBtn?.setOnClickListener {

           createDialog()
        }

        setUpDashboard()


    }

    private fun createDialog(){

        try {
            val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.myDialog))
            builder.setTitle("Log Out")
            builder.setMessage("Are you sure you want to log out")
            builder.setPositiveButton("OK"){_,_ ->
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }
            builder.setNegativeButton("Cancel", null)
            builder.show()

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onBackPressed() {
        createDialog()
    }

    private fun setUpDashboard(){

        val previousActivity = intent.getStringExtra("FROM_ACTIVITY")

        if(previousActivity.equals("SIGNUP")){

            user = intent.getSerializableExtra("NEW_USER") as User
            userName.text = "Hi ${user.firstName} ${user.lastName}"
            userPhone.text = user.phoneNumber
            userEmail.text = user.email

        }
        else if(previousActivity.equals("LOGIN")){

            loggedInEmail = intent.getStringExtra("LOGGED_IN_EMAIL")

            disposable.add(
                MainActivity.db!!.userDao().findByEmail(loggedInEmail!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object: DisposableSingleObserver<User>(){
                        override fun onSuccess(t: User) {
                            userName.text = "Hi ${t.firstName} ${t.lastName}"
                            userPhone.text = t.phoneNumber
                            userEmail.text = t.email
                        }

                        override fun onError(e: Throwable) {
                            Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                        }

                    })
            )
        }



    }

    private fun setUpMenuButtons(menuItem: ConstraintLayout){

        menuItem.setOnClickListener {
            val intent = Intent(applicationContext, ComingSoonActivity::class.java)
            startActivity(intent)
        }
    }

}
