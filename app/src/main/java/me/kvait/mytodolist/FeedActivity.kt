package me.kvait.mytodolist

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_create_task.*
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.item_standard_task.*
import kotlinx.coroutines.*
import me.kvait.mytodolist.adapter.TaskAdapter
import me.kvait.mytodolist.api.ApiRepository
import me.kvait.mytodolist.data.dto.TaskResponseDto.Companion.toModel
import me.kvait.mytodolist.utils.API_RESPONSE_SHARED_FILE
import me.kvait.mytodolist.utils.AUTHENTICATED_ID
import me.kvait.mytodolist.utils.AUTHENTICATED_SHARED_KEY
import me.kvait.mytodolist.utils.myProgressDialog
import splitties.activities.start
import splitties.toast.toast

class FeedActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private val repo = ApiRepository
    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        supportActionBar?.hide()

        topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.btn_logOut -> {
                    getSharedPreferences(API_RESPONSE_SHARED_FILE, Context.MODE_PRIVATE)
                        .edit()
                        .clear()
                        .apply()

                    val mainIntent = Intent(this@FeedActivity, MainActivity::class.java)
                    startActivity(mainIntent)
                    finish()
                    true
                }
                else -> false
            }
        }

        fab.setOnClickListener {
            start<CreateTaskActivity>()
        }
    }

    override fun onStart() {
        super.onStart()
        val token = getSharedPreferences(API_RESPONSE_SHARED_FILE, MODE_PRIVATE)
            .getString(AUTHENTICATED_SHARED_KEY, "")
        val userId = getSharedPreferences(API_RESPONSE_SHARED_FILE, MODE_PRIVATE)
            .getInt(AUTHENTICATED_ID, 0)
        ApiRepository.createRetrofitWithAuth(token!!)

        lifecycleScope.launch {
            dialog = myProgressDialog()
            val tasksList = repo.mGetAllTasks().map { it.toModel() }
            dialog?.dismiss()

            if (tasksList.isNotEmpty()) {
                with(rvItemTask) {
                    layoutManager = LinearLayoutManager(this@FeedActivity)
                    adapter = TaskAdapter(tasksList)
                }
            } else {
                with(rvItemTask) {
                    layoutManager = LinearLayoutManager(this@FeedActivity)
                    adapter = TaskAdapter(emptyList())
                }
                toast(R.string.error_download_posts)
            }
        }
    }
}