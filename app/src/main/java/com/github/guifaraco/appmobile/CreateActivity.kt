package com.github.guifaraco.appmobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.guifaraco.appmobile.api.ApiClient
import com.github.guifaraco.appmobile.api.ApiService
import com.github.guifaraco.appmobile.api.TaskData
import com.github.guifaraco.appmobile.api.TaskResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateActivity : AppCompatActivity() {

    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        val sharedPreferences = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        token = sharedPreferences.getString("auth_token", "") ?: ""

        if (token.isEmpty()) {
            Toast.makeText(this, "Token de autenticação não encontrado", Toast.LENGTH_SHORT).show()
            finish()  // Finaliza a activity se o token não estiver presente
            return
        }

        val taskDescription = findViewById<EditText>(R.id.task_description)
        val saveTaskButton = findViewById<Button>(R.id.save_task_button)

        saveTaskButton.setOnClickListener {
            val description = taskDescription.text.toString()

            if (description.isEmpty()) {
                Toast.makeText(this, "A descrição não pode estar vazia!", Toast.LENGTH_SHORT).show()
            } else {
                saveTask(description)
            }
        }
    }

    private fun saveTask(description: String) {
        val apiService = ApiClient.instance.create(ApiService::class.java)
        val taskData = TaskData(description)

        val call = apiService.saveTask("Bearer $token", taskData)

        call.enqueue(object : Callback<TaskResponse> {
            override fun onResponse(call: Call<TaskResponse>, response: Response<TaskResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CreateActivity, "Tarefa criada com sucesso!", Toast.LENGTH_SHORT).show()

                    // Retornar para a ListActivity com o resultado OK
                    setResult(RESULT_OK)
                    finish()  // Finaliza a activity e volta para a ListActivity
                } else {
                    Toast.makeText(this@CreateActivity, "Erro ao criar tarefa: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TaskResponse>, t: Throwable) {
                Toast.makeText(this@CreateActivity, "Erro na conexão: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}


