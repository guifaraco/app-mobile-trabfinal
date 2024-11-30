package com.github.guifaraco.appmobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private var tasks: List<String>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val taskDescription: TextView = view.findViewById(R.id.task_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.taskDescription.text = tasks[position]
    }

    override fun getItemCount(): Int = tasks.size

    fun updateTasks(newTasks: List<String>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}

