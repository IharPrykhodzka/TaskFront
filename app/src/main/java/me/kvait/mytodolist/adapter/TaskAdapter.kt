package me.kvait.mytodolist.adapter

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_standard_task.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.kvait.mytodolist.CreateTaskActivity
import me.kvait.mytodolist.R
import me.kvait.mytodolist.api.ApiRepository
import me.kvait.mytodolist.data.model.TaskModel
import me.kvait.mytodolist.utils.MY_LOG
import me.kvait.mytodolist.utils.UPDATE_KEY
import me.kvait.mytodolist.utils.formatter.DateFormatter
import splitties.systemservices.layoutInflater
import splitties.toast.toast

class TaskAdapter(private val myTasksList: List<TaskModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val repo = ApiRepository
    private var dialog: ProgressDialog? = null
    private var tasksList = myTasksList.toMutableList()

    @RequiresApi
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_standard_task, parent, false)
        return TaskViewHolder(this, view)
    }

    @RequiresApi
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.v(MY_LOG, "position is $position")
        with(holder as TaskViewHolder) {
            bind(tasksList[position])
        }

        with(holder.itemView) {


            setOnClickListener {
                val bundle = Bundle()
                val task = tasksList[position]
                bundle.putSerializable(UPDATE_KEY, task)

                val intent = Intent(it.context, CreateTaskActivity::class.java)
                intent.putExtra(UPDATE_KEY, bundle)
                it.context.startActivity(intent)
            }

            setOnLongClickListener {
                Log.d(MY_LOG, "tisk")

                val builder = AlertDialog.Builder(it.context)
                builder.setIcon(R.drawable.ic_baseline_delete_forever_24)
                builder.setTitle(context.getString(R.string.warning))
                builder.setMessage(context.getString(R.string.question_for_delete_task))

                builder.setPositiveButton(context.getString(R.string.yes)) { dialog, which ->

                    GlobalScope.launch(Dispatchers.Main) {
                        val task = tasksList[position]
                        val result = repo.mDeleteTaskById(task.id)
                        if (result.isSuccessful) {
                            tasksList.removeAt(position)
                            notifyDataSetChanged()
                            toast(R.string.deleted)
                        }else {
                            toast(R.string.error_connect)
                        }
                    }

                }

                builder.setNegativeButton(context.getString(R.string.no)) { dialog, which ->
                    toast(R.string.canceled)
                }

                builder.show()
                true
            }
        }
    }

    @RequiresApi
    override fun getItemCount() =
        tasksList.count()


    class TaskViewHolder(val adapter: TaskAdapter, view: View) : RecyclerView.ViewHolder(view) {
        fun bind(task: TaskModel) {
            with(itemView) {
                title.text = task.title
                content.text = task.content
                date.text = DateFormatter().getFormatDate(task.createdDate.toLong())
            }
        }
    }
}