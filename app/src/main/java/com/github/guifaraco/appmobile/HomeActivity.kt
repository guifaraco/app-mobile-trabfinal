package com.github.guifaraco.appmobile

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

        // Referenciar os elementos do layout
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        // Configurar o botão de login
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()

            if (username.isEmpty()) {
                Toast.makeText(this, "Por favor, insira seu nome de usuário", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Fazer login
            performLogin(username)
        }
    }

    private fun performLogin(username: String) {
        val apiService = ApiClient.instance.create(ApiService::class.java)
        val call = apiService.login(LoginRequest(username))

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    Toast.makeText(this@ActivityHome, "Login bem-sucedido! Token: $token", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@ActivityHome, "Falha no login: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@ActivityHome, "Erro na conexão: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}