package com.github.guifaraco.appmobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private var tasks: List<String>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    // ViewHolder que mantém a referência à view de cada item
    inner class TaskViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val taskDescription: TextView = view.findViewById(R.id.task_description)
    }

    // Cria um ViewHolder a partir do layout do item da lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    // Preenche os dados da tarefa no ViewHolder
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.taskDescription.text = tasks[position]
    }

    // Retorna o número de itens na lista
    override fun getItemCount(): Int = tasks.size

    // Atualiza a lista de tarefas
    fun updateTasks(newTasks: List<String>) {
        tasks = newTasks
        notifyDataSetChanged()  // Notifica o RecyclerView para atualizar a lista
    }
}

