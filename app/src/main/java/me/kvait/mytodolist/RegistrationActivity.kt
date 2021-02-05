package me.kvait.mytodolist

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import me.kvait.mytodolist.api.ApiRepository
import me.kvait.mytodolist.utils.isValidEmail
import me.kvait.mytodolist.utils.myProgressDialog
import me.kvait.mytodolist.utils.isValidPassword
import splitties.toast.toast

class RegistrationActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        supportActionBar?.hide()

        btn_confirm_reg.setOnClickListener {
            case_password_reg_repeat.error = null
            case_login_reg.error = null

            if (edit_password_reg.text.toString() == edit_password_reg_repeat.text.toString()
                && isValidPassword(edit_password_reg.text.toString())
                && isValidEmail(edit_login_reg.text.toString())
            ) {



                progressDialog = myProgressDialog()

                launch {
                    val response = ApiRepository.registration(
                        edit_login_reg.text.toString(),
                        edit_password_reg.text.toString()
                    )
                    if (response.isSuccessful) {
                        progressDialog?.dismiss()
                        toast(R.string.good_reg)
                        finish()
                    } else {
                        progressDialog?.dismiss()
                        case_login_reg.error = getString(R.string.registration_failed)
                    }
                }
            } else {
                if (!isValidEmail(edit_login_reg.text.toString())) {
                    case_login_reg.error = getString(R.string.error_email_not_same)
                } else if (edit_password_reg.text.toString() == edit_password_reg_repeat.text.toString()) {
                    case_password_reg_repeat.error = getString(R.string.error_password_not_same)
                } else {
                    case_password_reg_repeat.error = getString(R.string.error_password_not_same_2)
                }
            }
        }

        btn_cancel.setOnClickListener {
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        if (progressDialog != null) {
            progressDialog!!.cancel()
        }
        cancel()
    }
}
