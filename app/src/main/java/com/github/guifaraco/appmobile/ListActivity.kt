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

        // Obtém o token de autenticação do SharedPreferences
        val sharedPreferences = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        token = sharedPreferences.getString("auth_token", "") ?: ""

        // Verifica se o token está vazio, caso sim, exibe um erro
        if (token.isEmpty()) {
            Toast.makeText(this, "Token de autenticação não encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        // Inicializa o botão de adicionar tarefa
        val addTaskButton = findViewById<Button>(R.id.add_task_button)

        // Configura o RecyclerView para exibir as tarefas
        recyclerView = findViewById(R.id.task_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TaskAdapter(listOf())
        recyclerView.adapter = adapter

        // Chama a função para buscar as tarefas da API
        fetchTasks()

        // Ação do botão para adicionar nova tarefa
        addTaskButton.setOnClickListener {
            val intent = Intent(this, CreateActivity::class.java)
            intent.putExtra("TOKEN", token)  // Envia o token para a tela de criação de tarefa
            startActivityForResult(intent, CREATE_TASK_REQUEST_CODE)
        }
    }

    // Função que realiza a busca das tarefas na API
    private fun fetchTasks() {
        val apiService = ApiClient.instance.create(ApiService::class.java)
        val call = apiService.getTasks("Bearer $token")

        // Faz a requisição para a API
        call.enqueue(object : Callback<List<TaskResponse>> {
            override fun onResponse(call: Call<List<TaskResponse>>, response: Response<List<TaskResponse>>) {
                // Se a resposta for bem-sucedida, atualiza a lista de tarefas
                if (response.isSuccessful) {
                    val tasks = response.body()?.map { it.data.description } ?: emptyList()
                    adapter.updateTasks(tasks)
                } else {
                    Toast.makeText(this@ListActivity, "Erro ao buscar tarefas: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }

            // Caso ocorra um erro de conexão
            override fun onFailure(call: Call<List<TaskResponse>>, t: Throwable) {
                Toast.makeText(this@ListActivity, "Erro na conexão: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Resultado de criação de tarefa, atualiza a lista de tarefas
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CREATE_TASK_REQUEST_CODE && resultCode == RESULT_OK) {
            fetchTasks()
        }
    }

    companion object {
        const val CREATE_TASK_REQUEST_CODE = 1001
    }
}



