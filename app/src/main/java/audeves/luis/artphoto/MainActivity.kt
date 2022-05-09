package audeves.luis.artphoto

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var iniciarSesion: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_sesion)

        var btnIniciarSesion = findViewById<Button>(R.id.button)


        auth = Firebase.auth

        val edt_correo: EditText = findViewById(R.id.edt_correo)
        val edt_contraseña: EditText = findViewById(R.id.edt_contraseña)


        btnIniciarSesion.setOnClickListener {
            val intent: Intent = Intent(this, HomeActivity::class.java)

            var edt_usuario = findViewById<EditText>(R.id.edt_correo)
            var edt_contra = findViewById<EditText>(R.id.edt_contraseña)
            auth = Firebase.auth
            btnIniciarSesion.setOnClickListener {
                var correo: String = edt_usuario.text.toString().trim();
                var contra: String = edt_contra.text.toString().trim();
                if (correo.isNullOrEmpty() || contra.isNullOrEmpty()) {
                    Toast.makeText(this, "Campos vacios", Toast.LENGTH_LONG).show()
                } else {
                    auth.signInWithEmailAndPassword(correo, contra)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Credenciales aceptadas.", Toast.LENGTH_SHORT)
                                    .show()
                                val intent: Intent = Intent(this, HomeActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    baseContext, "Correo o contraseña incorrectos.",
                                    Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                }

            }


            var btnCrearCuenta1 = findViewById<Button>(R.id.btn_crear_cuenta)
            btnCrearCuenta1.setOnClickListener {
                val intent: Intent = Intent(this, ActivityCrearCuenta::class.java)
                startActivity(intent)

            }
        }
    }

        fun abrirInicio() {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

}