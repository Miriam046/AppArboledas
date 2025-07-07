package com.example.residencialasarboledas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InicioActivity : AppCompatActivity() {

    private lateinit var textViewNombreUsuario: TextView
    private lateinit var buttonGenerarQR: Button
    private lateinit var buttonInvitados: Button
    private lateinit var buttonCerrarSesion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio) // Asegúrate que así se llama tu XML

        // Referencias
        textViewNombreUsuario = findViewById(R.id.textViewNombreUsuario)
        buttonGenerarQR = findViewById(R.id.buttonGenerarQR)
        buttonInvitados = findViewById(R.id.buttonInvitados)
        buttonCerrarSesion = findViewById(R.id.buttonCerrarSesion)

        // Datos recibidos desde LoginActivity
        val nombreUsuario = intent.getStringExtra("nombreUsuario")
        val idUsuario = intent.getIntExtra("idUsuario", -1)

        textViewNombreUsuario.text = nombreUsuario ?: "Usuario desconocido"

        // Abrir pantalla de QR
        buttonGenerarQR.setOnClickListener {
            // Puedes cambiar esto según la pantalla que tengas para generar QR
            // startActivity(Intent(this, GenerarQRActivity::class.java))
        }

        // Ir a la lista de invitados
        buttonInvitados.setOnClickListener {
            val intent = Intent(this, ListaInvitadosActivity::class.java)
            intent.putExtra("idUsuario", idUsuario)
            startActivity(intent)
        }

        // Cerrar sesión
        buttonCerrarSesion.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
