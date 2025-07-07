package com.example.residencialasarboledas
data class Invitado(
    val id_invitado: Int,
    val nombre: String,
    val apellido_paterno: String,
    val apellido_materno: String,
    val tipo_invitacion: String,
    val codigo: String,
    val fecha_validez: String,  // Puede ser Date, pero para JSON fácil usar String
    val estado: String,
    val id_residente: Int
)
