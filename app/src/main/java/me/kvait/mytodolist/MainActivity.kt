package me.kvait.mytodolist

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.kvait.mytodolist.api.ApiRepository
import me.kvait.mytodolist.api.ApiRepository.createRetrofitWithAuth
import me.kvait.mytodolist.data.Token
import me.kvait.mytodolist.utils.*
import splitties.toast.toast

class MainActivity : AppCompatActivity() {

    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        btn_login.setOnClickListener {
            case_password.error = null

            lifecycleScope.launch {
                val response = runCatching {
                    ApiRepository.authentication(
                            edit_login.text.toString(),
                            edit_password.text.toString()
                    )
                }

                dialog = myProgressDialog()

                delay(1000)
                Log.d(MY_LOG, """
                    
                    
                    ${response.exceptionOrNull()}
                    """)

                response.getOrNull()?.body()?.let {
                    dialog?.dismiss()

                    setUserAuth(it)
                    Log.d(MY_LOG, it.id.toString() + " " + it.token)
                    toast(R.string.success)
                    startFeedActivity()
                } ?: run {
                    dialog?.dismiss()
                    case_password.error = getString(R.string.authentication_failed)
                }
            }
        }

        btn_registration.setOnClickListener {
            val regIntent = Intent(this@MainActivity, RegistrationActivity::class.java)
            startActivity(regIntent)
        }
    }

    override fun onStart() {
        super.onStart()
        if (!isAuthenticated()) {
            val token = getSharedPreferences(API_RESPONSE_SHARED_FILE, Context.MODE_PRIVATE).getString(
                    AUTHENTICATED_SHARED_KEY, "")
            Log.d(MY_LOG, "$token")
            createRetrofitWithAuth(token!!)
            startFeedActivity()
        }
    }

    private fun setUserAuth(token: Token) =
            getSharedPreferences(API_RESPONSE_SHARED_FILE, Context.MODE_PRIVATE)
                    .edit()
                    .putInt(AUTHENTICATED_ID, token.id)
                    .putString(AUTHENTICATED_SHARED_KEY, token.token)
                    .apply()

    private fun isAuthenticated() =
            getSharedPreferences(API_RESPONSE_SHARED_FILE, Context.MODE_PRIVATE).getString(
                    AUTHENTICATED_SHARED_KEY, "")?.isEmpty() ?: false

    private fun startFeedActivity() {
        val feedIntent = Intent(this@MainActivity, FeedActivity::class.java)
        startActivity(feedIntent)
        finish()
    }
}