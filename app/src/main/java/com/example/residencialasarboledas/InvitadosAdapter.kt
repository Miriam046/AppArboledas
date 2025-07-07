package com.example.residencialasarboledas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InvitadosAdapter(private val invitados: List<Invitado>) :
    RecyclerView.Adapter<InvitadosAdapter.InvitadoViewHolder>() {

    class InvitadoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreCompleto: TextView = itemView.findViewById(R.id.tvNombreCompleto)
        val tipoInvitacion: TextView = itemView.findViewById(R.id.tvTipoInvitacion)
        val estado: TextView = itemView.findViewById(R.id.tvEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvitadoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_invitado, parent, false)
        return InvitadoViewHolder(view)
    }

    override fun onBindViewHolder(holder: InvitadoViewHolder, position: Int) {
        val invitado = invitados[position]
        holder.nombreCompleto.text = "${invitado.nombre} ${invitado.apellido_paterno} ${invitado.apellido_materno}"
        holder.tipoInvitacion.text = "Tipo: ${invitado.tipo_invitacion ?: "N/A"}"
        holder.estado.text = "Estado: ${invitado.estado ?: "N/A"}"
    }

    override fun getItemCount(): Int = invitados.size
}
