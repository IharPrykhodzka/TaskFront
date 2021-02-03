package me.kvait.mytodolist.utils

import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import me.kvait.mytodolist.api.ApiRepository
import java.util.regex.Pattern

fun isValid(password: String) = Pattern.compile("(?=.*[A-Z])(?!.*[^a-zA-Z0-9])(.{6,})\$").matcher(password).matches()

fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
