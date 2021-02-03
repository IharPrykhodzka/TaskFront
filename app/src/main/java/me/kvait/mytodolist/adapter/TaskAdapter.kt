package me.kvait.mytodolist.adapter

import android.content.Intent
import android.os.Bundle
import android.text.style.UpdateAppearance
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_create_task.*
import kotlinx.android.synthetic.main.item_standard_task.view.*
import me.kvait.mytodolist.CreateTaskActivity
import me.kvait.mytodolist.R
import me.kvait.mytodolist.data.model.TaskModel
import me.kvait.mytodolist.utils.MY_LOG
import me.kvait.mytodolist.utils.UPDATE_KEY
import me.kvait.mytodolist.utils.formatter.DateFormatter

class TaskAdapter(private val tasksList: List<TaskModel>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @RequiresApi
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_standard_task, parent, false)
        return TaskViewHolder(this, view)
    }

    @RequiresApi
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.v(MY_LOG, "position is $position")
        with(holder as TaskViewHolder) {
            bind(tasksList[position])
        }

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            val task = tasksList[position]
            bundle.putSerializable(UPDATE_KEY, task)

            val intent = Intent(it.context ,CreateTaskActivity::class.java)
            intent.putExtra(UPDATE_KEY, bundle)
            it.context.startActivity(intent)
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