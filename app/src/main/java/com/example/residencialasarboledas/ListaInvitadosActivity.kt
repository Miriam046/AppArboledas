package com.example.residencialasarboledas
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class ListaInvitadosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InvitadosAdapter
    private val listaInvitados = mutableListOf<Invitado>()
    private lateinit var btnSalir: Button  // <-- Añadido botón salir

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_invitados)

        recyclerView = findViewById(R.id.recyclerInvitados)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = InvitadosAdapter(listaInvitados)
        recyclerView.adapter = adapter

        // Inicializar botón salir


        btnSalir = findViewById(R.id.btnSalir)
        btnSalir.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()  // 🔒 Cierra la actividad actual para evitar regresar con el botón atrás
        }


        val idUsuario = intent.getIntExtra("idUsuario", -1)
        if (idUsuario == -1) {
            Toast.makeText(this, "Id de usuario no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        obtenerInvitadosPorUsuario(idUsuario)
    }

    private fun obtenerInvitadosPorUsuario(idUsuario: Int) {
        val url = "http://10.0.2.2:5073/api/Invitados/porUsuario/$idUsuario"

        val queue = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                listaInvitados.clear()

                for (i in 0 until response.length()) {
                    val invitadoJson = response.getJSONObject(i)
                    val invitado = Invitado(
                        id_invitado = invitadoJson.getInt("id_invitado"),
                        nombre = invitadoJson.getString("nombre"),
                        apellido_paterno = invitadoJson.getString("apellido_paterno"),
                        apellido_materno = invitadoJson.getString("apellido_materno"),
                        tipo_invitacion = invitadoJson.optString("tipo_invitacion", null),
                        codigo = invitadoJson.optString("codigo", null),
                        fecha_validez = invitadoJson.optString("fecha_validez", null),
                        estado = invitadoJson.optString("estado", null),
                        id_residente = invitadoJson.getInt("id_residente")
                    )
                    listaInvitados.add(invitado)
                }

                adapter.notifyDataSetChanged()
            },
            { error ->
                Toast.makeText(this, "Error al obtener invitados: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )

        queue.add(jsonArrayRequest)
    }
}

