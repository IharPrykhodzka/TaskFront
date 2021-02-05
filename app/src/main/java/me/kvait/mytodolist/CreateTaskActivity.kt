package me.kvait.mytodolist

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_create_task.*
import kotlinx.coroutines.launch
import me.kvait.mytodolist.api.ApiRepository
import me.kvait.mytodolist.data.dto.TaskRequestDto
import me.kvait.mytodolist.data.dto.TaskResponseDto
import me.kvait.mytodolist.data.model.TaskModel
import me.kvait.mytodolist.utils.*
import splitties.toast.toast
import java.util.*

class CreateTaskActivity : AppCompatActivity() {
    private val repo = ApiRepository
    private var dialog: ProgressDialog? = null
    lateinit var task: TaskModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)

        supportActionBar?.hide()

        val token = getSharedPreferences(API_RESPONSE_SHARED_FILE, MODE_PRIVATE)
            .getString(AUTHENTICATED_SHARED_KEY, "")
        val userId = getSharedPreferences(API_RESPONSE_SHARED_FILE, MODE_PRIVATE)
            .getInt(AUTHENTICATED_ID, 0)
        ApiRepository.createRetrofitWithAuth(token!!)

        val bundle = intent.getBundleExtra(UPDATE_KEY)

        if (bundle != null) {
            task = bundle.getSerializable(UPDATE_KEY) as TaskModel
            btn_new_task.visibility = View.GONE
            btn_update_task.visibility = View.VISIBLE
            create_task_title.editText?.text = task.title.toEditable()
            create_task_content.editText?.text = task.content.toEditable()

        } else{
            btn_new_task.visibility = View.VISIBLE
            btn_update_task.visibility = View.GONE

        }

        btn_new_task.setOnClickListener {
            lifecycleScope.launch {
                if (edit_crTitle.length() > 0 && edit_crContent.length() > 0) {
                    val newTask = TaskRequestDto(
                        id = 0,
                        user_id = userId,
                        title = create_task_title.editText?.text.toString(),
                        content = create_task_content.editText?.text.toString(),
                        createdDate = Date().time.toString()
                    )

                    dialog = myProgressDialog()
                    val result = repo.mSaveTask(newTask)
                    if (result.isSuccessful) {
                        dialog?.dismiss()
                        toast(R.string.task_created_successfully)
                        finish()
                    } else {
                        dialog?.dismiss()
                        toast(getString(R.string.error_connect))
                    }


                } else {
                    toast(R.string.error_create_task_empty)
                }
            }
        }

        btn_update_task.setOnClickListener {
            lifecycleScope.launch {
                if (edit_crTitle.length() > 0 && edit_crContent.length() > 0) {

                    val newTask = TaskRequestDto(
                        id = task.id,
                        user_id = task.user_id,
                        title = create_task_title.editText?.text.toString(),
                        content = create_task_content.editText?.text.toString(),
                        createdDate = Date().time.toString()
                    )


                    dialog = myProgressDialog()
                    Log.d(MY_LOG, "\n" + newTask.toString() + "\n")
                    val result = repo.mUpdateTaskById(newTask)

                        if (result.isSuccessful) {
                            dialog?.dismiss()
                            toast(R.string.task_updated_successfully)
                            finish()
                        } else {
                            dialog?.dismiss()
                            toast(getString(R.string.error_connect))
                        }
                    bundle?.clear()
                }else {
                    toast(R.string.error_create_task_empty)
                }
            }
        }
    }
}