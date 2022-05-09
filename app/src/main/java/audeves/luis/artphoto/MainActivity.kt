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

            var correo: String = edt_correo.text.toString().trim()
            var contraseña: String = edt_contraseña.text.toString().trim()


            if (correo.isNullOrEmpty() || contraseña.isNullOrEmpty()) {
                Toast.makeText(this, "favor de llenar los campos", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(correo, contraseña)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            startActivity(intent)

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("inicioSesion", "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()

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

    fun abrirInicio() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}