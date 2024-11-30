package com.github.guifaraco.appmobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.github.guifaraco.appmobile.api.ApiService
import com.github.guifaraco.appmobile.api.ApiClient
import com.github.guifaraco.appmobile.api.LoginRequest
import com.github.guifaraco.appmobile.api.LoginResponse
import com.github.guifaraco.appmobile.R

class ActivityHome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Inicializa os campos de entrada e o botão de login
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()

            // Valida se o campo de nome de usuário está vazio
            if (username.isEmpty()) {
                Toast.makeText(this, "Por favor, insira seu nome de usuário", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Chama a função de login
            performLogin(username)
        }
    }

    // Função que realiza o login na API
    private fun performLogin(username: String) {
        val apiService = ApiClient.instance.create(ApiService::class.java)
        val call = apiService.login(LoginRequest(username))

        // Faz a requisição para a API
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                // Se a resposta for bem-sucedida, salva o token e segue para a tela de lista
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    if (!token.isNullOrEmpty()) {
                        val sharedPreferences = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
                        sharedPreferences.edit().putString("auth_token", token).apply()

                        Toast.makeText(this@ActivityHome, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@ActivityHome, ListActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@ActivityHome, "Token não recebido", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ActivityHome, "Falha no login: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            // Caso ocorra um erro de conexão
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@ActivityHome, "Erro na conexão: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
