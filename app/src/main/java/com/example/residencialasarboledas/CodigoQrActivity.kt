package com.example.residencialasarboledas

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class CodigoQrActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etApellidos: EditText
    private lateinit var spinnerTipo: Spinner
    private lateinit var btnGenerarQr: Button
    private lateinit var qrImageView: ImageView
    private lateinit var btnSalir: Button

    // Cambia esto por tu IP real y puerto
    private val urlApi = "http://10.0.2.2:5073/api/Invitados"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_codigoqr)

        // Enlaces a vistas
        etNombre = findViewById(R.id.et_nombre)
        etApellidos = findViewById(R.id.et_apellidos)
        spinnerTipo = findViewById(R.id.spinner_tipo_invitacion)
        btnGenerarQr = findViewById(R.id.btn_generar_qr)
        qrImageView = findViewById(R.id.qrImageView)
        btnSalir = findViewById(R.id.btn_salir)

        // Spinner con valores
        val tipos = arrayOf("Reunión", "Fiesta", "Visita", "Otro")
        spinnerTipo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipos)

        btnGenerarQr.setOnClickListener {
            generarYRegistrarQR()
        }

        btnSalir.setOnClickListener {
            finish()
        }
    }

    private fun generarYRegistrarQR() {
        val nombre = etNombre.text.toString().trim()
        val apellidos = etApellidos.text.toString().trim()
        val tipoInvitacion = spinnerTipo.selectedItem.toString()

        if (nombre.isEmpty() || apellidos.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val codigoGenerado = UUID.randomUUID().toString().substring(0, 8).uppercase()
        val fechaHoy = SimpleDateFormat("yyyy-MM-dd'T'00:00:00", Locale.getDefault()).format(Date())

        val json = JSONObject().apply {
            put("id_invitado", 0)
            put("nombre", nombre)
            put("apellido_paterno", apellidos.split(" ")[0])
            put("apellido_materno", apellidos.split(" ").getOrElse(1) { "" })
            put("tipo_invitacion", tipoInvitacion)
            put("codigo", codigoGenerado)
            put("fecha_validez", fechaHoy)
            put("estado", "Activo")
            put("id_residente", 1) // Aquí coloca el ID real del residente (por ejemplo, Miriam = 1)
        }

        val queue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(
            com.android.volley.Request.Method.POST, urlApi, json,
            { response ->
                generarCodigoQR(codigoGenerado)
                Toast.makeText(this, "Invitado registrado", Toast.LENGTH_SHORT).show()
            },
            { error ->
                Toast.makeText(this, "Error al registrar: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )

        queue.add(request)
    }

    private fun generarCodigoQR(texto: String) {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(texto, BarcodeFormat.QR_CODE, 200, 200)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }

        qrImageView.setImageBitmap(bitmap)
    }
}
