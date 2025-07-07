package com.example.residencialasarboledas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsuario: EditText
    private lateinit var etContraseña: EditText
    private lateinit var btnRegistrarse: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etUsuario = findViewById(R.id.editTextText)
        etContraseña = findViewById(R.id.editTextTextPassword)
        btnRegistrarse = findViewById(R.id.button)

        btnRegistrarse.setOnClickListener {
            val usuario = etUsuario.text.toString()
            val contraseña = etContraseña.text.toString()

            if (usuario.isEmpty() || contraseña.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa usuario y contraseña", Toast.LENGTH_SHORT).show()
            } else {
                realizarLogin(usuario, contraseña)
            }
        }
    }

    private fun realizarLogin(usuario: String, contraseña: String) {
        val url = "http://10.0.2.2:5073/api/UsuariosResidentes/login"

        val jsonBody = JSONObject().apply {
            put("usuarioNombre", usuario)
            put("password", contraseña)
        }

        val request = JsonObjectRequest(
            Request.Method.POST,
            url,
            jsonBody,
            { response ->
                val mensaje = response.optString("message", "Inicio de sesión exitoso")
                val nombre = response.optString("nombre", "Usuario")
                val idUsuario = response.optInt("id_usuario", -1)

                Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()

                if (idUsuario != -1) {
                    // 👉 Enviar a InicioActivity con nombre e ID del usuario
                    val intent = Intent(this, InicioActivity::class.java)
                    intent.putExtra("nombreUsuario", nombre)
                    intent.putExtra("idUsuario", idUsuario)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "ID de usuario no recibido del servidor", Toast.LENGTH_LONG).show()
                }
            },
            { error ->
                val code = error.networkResponse?.statusCode ?: -1
                if (code == 401) {
                    Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error de conexión con servidor (código $code)", Toast.LENGTH_LONG).show()
                }
            }
        )

        Volley.newRequestQueue(this).add(request)
    }
}
