package audeves.luis.artphoto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RecuperarContra : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_contra)

        auth = Firebase.auth
        var tv_volver = findViewById<TextView>(R.id.volver)
        val edt_correo= findViewById<EditText>(R.id.edt_correo)
        val btn_enviar_correo = findViewById<Button>(R.id.button)

        btn_enviar_correo.setOnClickListener {
            var correo: String = edt_correo.text.toString().trim()
            if (correo.isNullOrEmpty()) {
                Toast.makeText(this, "Campo Vacio", Toast.LENGTH_SHORT).show()
            } else {
                auth.sendPasswordResetEmail(correo)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Correo de recuperación enviado",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(this, "No se envio el correo", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }

        tv_volver.setOnClickListener {
            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}