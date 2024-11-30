package com.github.guifaraco.appmobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.guifaraco.appmobile.api.ApiClient
import com.github.guifaraco.appmobile.api.ApiService
import com.github.guifaraco.appmobile.api.TaskResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        // Obtendo o token passado pela Intent
        token = intent.getStringExtra("token") ?: ""

        val addTaskButton = findViewById<Button>(R.id.add_task_button)

        // Configurando o RecyclerView
        recyclerView = findViewById(R.id.task_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TaskAdapter(listOf())
        recyclerView.adapter = adapter

        // Buscar tarefas ao carregar a tela
        fetchTasks()

        addTaskButton.setOnClickListener {
            val intent = Intent(this, CreateActivity::class.java)
            intent.putExtra("token", token)
            startActivity(intent)
        }
        
    }

    private fun fetchTasks() {
        val sharedPreferences = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Token de autenticação não encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        val apiService = ApiClient.instance.create(ApiService::class.java)
        val call = apiService.getTasks("Bearer $token")

        call.enqueue(object : Callback<List<TaskResponse>> {
            override fun onResponse(call: Call<List<TaskResponse>>, response: Response<List<TaskResponse>>) {
                if (response.isSuccessful) {
                    val tasks = response.body()?.map { it.data.description } ?: emptyList()
                    adapter.updateTasks(tasks)
                } else {
                    Toast.makeText(this@ListActivity, "Erro ao buscar tarefas: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<TaskResponse>>, t: Throwable) {
                Toast.makeText(this@ListActivity, "Erro na conexão: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
