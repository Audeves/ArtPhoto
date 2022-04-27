package audeves.luis.artphoto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ActivityPrimerInicio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_primer_inicio)
        var btnInicioSesion = findViewById<Button>(R.id.btn_iniciar_sesion)
        btnInicioSesion.setOnClickListener {
            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
        var btnCrear = findViewById<Button>(R.id.btn_crear_cuenta)
        btnCrear.setOnClickListener {
            val intent: Intent = Intent(this, ActivityCrearCuenta::class.java)
            startActivity(intent)

        }

    }
}