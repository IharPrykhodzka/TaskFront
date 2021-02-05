package me.kvait.mytodolist.utils

import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import me.kvait.mytodolist.api.ApiRepository
import java.util.regex.Pattern

fun isValidPassword(password: String) =
    Pattern.compile("(?=.*[A-Z])(?!.*[^a-zA-Z0-9])(.{6,})\$").matcher(password).matches()

fun isValidEmail(email: String): Boolean =
    Pattern.compile("(^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}\$)").matcher(email).matches()

fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
