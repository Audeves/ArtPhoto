package audeves.luis.artphoto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    lateinit var iniciarSesion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_sesion)
        var btnIniciarSesion = findViewById<Button>(R.id.button)
        btnIniciarSesion.setOnClickListener {
            val intent: Intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

        }
        var btnCrearCuenta1 = findViewById<Button>(R.id.btn_crear_cuenta)
        btnCrearCuenta1.setOnClickListener {
            val intent: Intent = Intent(this, ActivityCrearCuenta::class.java)
            startActivity(intent)

        }
    }

    fun abrirInicio(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}