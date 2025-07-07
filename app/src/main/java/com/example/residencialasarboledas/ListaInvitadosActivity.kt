
package com.example.residencialasarboledas

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_invitados)  // Aquí va el layout principal de la Activity

        recyclerView = findViewById(R.id.recyclerInvitados)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = InvitadosAdapter(listaInvitados)
        recyclerView.adapter = adapter

        // Recibe idUsuario desde Intent
        val idUsuario = intent.getIntExtra("idUsuario", -1)
        if (idUsuario == -1) {
            Toast.makeText(this, "Id de usuario no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        obtenerInvitadosPorUsuario(idUsuario)
    }

    private fun obtenerInvitadosPorUsuario(idUsuario: Int) {
        val url = "http://TU_IP/api/Invitados/porUsuario/$idUsuario"  // Cambia TU_IP

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
